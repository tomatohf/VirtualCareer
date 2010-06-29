package com.qiaobutang.career

import Helper._
import scala.collection.JavaConversions._
import alice.tuprolog.Prolog
import alice.tuprolog.Struct
import alice.tuprolog.Theory
import alice.tuprolog.SolveInfo
import java.io.FileInputStream

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
			def collect(info:SolveInfo, count:Int):List[Map[String, String]] = {
				if (count == 0) return Nil
				
				val one = (Map[String, String]() /: varNames) {
					(map, varName) => map + (
						varName -> {
							val term = info.getVarValue(varName)
							val struct = if (term.isInstanceOf[Struct]) term.asInstanceOf[Struct] else null
							if(struct != null && struct.getArity <= 0)
								struct.getName
							else
								term.toString
						}
					)
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
	
	protected def getAllVarValues(query:String, varNames:List[String]) = getVarValues(-1, query ,varNames)
	protected def getFirstVarValues(query:String, varNames:List[String]) = getVarValues(1, query ,varNames)
	protected def handleFirstVarValues[ResultType](query:String, varNames:List[String])(handler:Map[String, String] => ResultType) = {
		val values = getFirstVarValues(query ,varNames)
		if (values.size > 0) Some(handler(values.head)) else None
	}
	
	protected def erase(clause:String) {
		engine.solve("retractall(" + clause + ").")
	}
}

class PublicKB(protected val location:String) extends KB {
	def collectProducts(catalog:String) = 
		getAllVarValues("product(" + catalog + ", X).", List("X")).map(_("X"))
		
	def collectCriteria(catalog:String) = 
		getAllVarValues("criteria(" + catalog + ", X).", List("X")).map(_("X"))
}

class PrivateKB(protected val location:String) extends KB {
	def collectWeights(catalog:String) = {
		(
			Map[String, Int]() /: getAllVarValues(
				"criteria_weight(" + catalog + ", X, Y).",
				List("X", "Y")
			)
		) {
			(map, varValues) => map + (varValues("X") -> varValues("Y").toInt)
		}
	}
	
	def name(roleId:String) = handleFirstVarValues("name(" + roleId + ", X, Y).", List("X", "Y")) {
		map => {
			val familyName = map("X")
			val y = map("Y")
			val givenName = if (y != "Y") y else getGender(roleId) match {
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
}
