package com.qiaobutang.career

import Helper._
import scala.collection.JavaConversions._
import scala.collection.immutable.WrappedString
import java.io.FileInputStream
import alice.tuprolog.{Prolog, Theory, Term, Struct, SolveInfo}

abstract class KB {
	protected def locations:List[String]
	
	protected val engine = new Prolog(
		Array(
			"alice.tuprolog.lib.BasicLibrary",
			// "alice.tuprolog.lib.IOLibrary",
			// "alice.tuprolog.lib.JavaLibrary",
			"alice.tuprolog.lib.ISOLibrary"
		)
	)
	engine.setTheory(
		(new Theory("") /: locations) {
			(theory, filePath) => {
				theory.append(new Theory(new FileInputStream(filePath)))
				theory
			}
		}
	)
	
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
	
	protected def forget(clause:String) { engine.solve("retractall(" + clause + ").") }
	protected def remember(clause:String) { engine.addTheory(new Theory(clause)) }
}

class PublicKB(val location:String) extends {
	protected val locations = List(location)
} with KB {
	def collectProducts(catalog:String) = {
		getAllVarValues("product(" + catalog + ", X).", List("X")).map {
			result => termAsString(result("X"))
		}
	}
		
	def collectCriteria(catalog:String) = {
		getAllVarValues("criteria(" + catalog + ", X).", List("X")).map {
			result => termAsString(result("X"))
		}
	}
}

class PrivateKB(val location:String) extends {
	protected val locations = List("prolog/base/private.pl", location)
} with KB {
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
			map("X") + {
				val y:String = map("Y")
				if (y != "Y") y else getGender(roleId) match {
					case Some(gender) => gender_title(gender)
					case None => ""
				}
			}
		}
	}
	
	def getGender(roleId:String) = handleFirstVarValues("gender(" + roleId + ", X).", List("X")) {
		_("X").toInt > 0
	}
	def setGender(roleId:String, gender:Boolean) {
		forget("gender(" + roleId + ", _)")
		remember("gender(" + roleId + ", " + (if (gender) 1 else 0) + ").")
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
	
	protected def counter = handleFirstVarValues("counter(X).", List("X"))(_("X").toInt).getOrElse(0)
	protected def increase_counter = handleFirstVarValues("increase_counter(X).", List("X"))(_("X").toInt).get
	
	def record_action_done(roleId:String, action_name:String, args:String*) {
		remember(
			"action_done(" + 
				Array(increase_counter, roleId, action_name, "["+args.mkString(",")+"]").mkString(",") + 
			")."
		)
	}
}
