package com.qiaobutang.career

import Helper._
import scala.collection.JavaConversions._
import scala.collection.immutable.WrappedString
import java.io.FileInputStream
import alice.tuprolog.{Prolog, Theory, Term, Struct, SolveInfo}

abstract class KB {
	protected val location:String
	
	protected val engine = new Prolog(
		Array(
			"alice.tuprolog.lib.BasicLibrary",
			// "alice.tuprolog.lib.IOLibrary",
			// "alice.tuprolog.lib.JavaLibrary",
			"alice.tuprolog.lib.ISOLibrary"
		)
	)
	engine.setTheory(new Theory(new FileInputStream(location)))
	
	/**
	 * @param count - Negative number, such as -1, means to get all
	 */
	protected def getVarValues(count:Int, query:String, varNames:List[String]) = {
		val result = engine.solve(query)
		if (result.isSuccess) {
			def collect(info:SolveInfo, count:Int):List[Map[String, Term]] = {
				if (count == 0) return Nil
				
				val one = (Map[String, Term]() /: varNames) {
					(map, varName) => map + (varName -> info.getVarValue(varName))
				}
				if (info.hasOpenAlternatives)
					one :: collect(engine.solveNext, count - 1)
				else
					one :: Nil
			}
			collect(result, count)
		}
		else
			Nil
	}
	implicit def termAsString(term:Term) = if (term.isInstanceOf[Struct]) {
		val struct = term.asInstanceOf[Struct]
		if (struct.getArity > 0) struct.toString else struct.getName
	}
	else
		term.toString
	implicit def termAsWrappedString(term:Term) = new WrappedString(termAsString(term))
	implicit def termAsList(term:Term):List[String] = if (term.isInstanceOf[Struct]) {
		val struct = term.asInstanceOf[Struct]
		if (struct.isList)
			struct.listIterator.toList.map(term => termAsString(term.asInstanceOf[Term]))
		else
			List(termAsString(struct))
	}
	else
		List(term.toString)
	
	protected def getAllVarValues(query:String, varNames:List[String]) = getVarValues(-1, query ,varNames)
	protected def getFirstVarValues(query:String, varNames:List[String]) = getVarValues(1, query ,varNames)
	protected def handleFirstVarValues[ResultType](query:String, varNames:List[String])(handler:Map[String, Term] => ResultType) = {
		val values = getFirstVarValues(query ,varNames)
		if (values.size > 0) Some(handler(values.head)) else None
	}
	
	protected def erase(clause:String) {
		engine.solve("retractall(" + clause + ").")
	}
}

class PublicKB(protected val location:String) extends KB {
	def collectProducts(catalog:String) = 
		getAllVarValues("product(" + catalog + ", X).", List("X")).map(
			result => termAsString(result("X"))
		)
		
	def collectCriteria(catalog:String) = 
		getAllVarValues("criteria(" + catalog + ", X).", List("X")).map(
			result => termAsString(result("X"))
		)
}

class PrivateKB(protected val location:String) extends KB {
	def collectWeights(catalog:String) = {
		(
			Map[String, Int]() /: getAllVarValues(
				"criteria_weight(" + catalog + ", X, Y).",
				List("X", "Y")
			)
		) {
			(map, varValues) => map + (termAsString(varValues("X")) -> varValues("Y").toInt)
		}
	}
	
	def name(roleId:String) = handleFirstVarValues("name(" + roleId + ", X, Y).", List("X", "Y")) {
		map => {
			val familyName = map("X")
			val y = map("Y")
			val givenName = if (y.toString != "Y") y else getGender(roleId) match {
				case Some(gender) => gender_title(gender)
				case None => ""
			} 
			familyName + givenName
		}
	}
	
	def getGender(roleId:String) = handleFirstVarValues("gender(" + roleId + ", X).", List("X")) {
		_("X").toInt > 0
	}
	def setGender(roleId:String, gender:Boolean) {
		erase("gender(" + roleId + ", _)")
		engine.addTheory(
			new Theory("gender(" + roleId + ", " + (if (gender) 1 else 0) + ").")
		)
	}
	
	def handleStateActions[ItemType] (stateName:String) (handler:(String, List[String]) => ItemType) = {
		getAllVarValues("state_action(" + stateName + ", X, Y).", List("X", "Y")).map {
			result => {
				val args = result("Y")
				handler(result("X"), if(args.toString == "Y") List() else args)
			}
		}
	}
	
	def handleFirstStateTransition[ItemType] (stateName:String) (handler:(String, List[String]) => ItemType) = {
		handleFirstVarValues("state_transition(" + stateName + ", X, Y).", List("X", "Y")) {
			result => {
				val args = result("Y")
				handler(result("X"), if(args.toString == "Y") List() else args)
			}
		}
	}
}
