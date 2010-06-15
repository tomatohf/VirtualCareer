package com.qiaobutang.career

import scala.collection.JavaConversions._
import alice.tuprolog.Prolog
import alice.tuprolog.Theory
import alice.tuprolog.SolveInfo
import java.io.FileInputStream

class KB(private val location:String) {
	private val engine = new Prolog(
		Array(
			"alice.tuprolog.lib.BasicLibrary",
			// "alice.tuprolog.lib.IOLibrary",
			// "alice.tuprolog.lib.JavaLibrary",
			"alice.tuprolog.lib.ISOLibrary"
		)
	)
	engine.setTheory(new Theory(new FileInputStream(location)))
	
	private def getAllVarValues(query:String, varNames:List[String]) = {
		val result = engine.solve(query)
		if (result.isSuccess) {
			def collect(info:SolveInfo):List[Map[String, String]] = {
				val one = (Map[String, String]() /: varNames) {
					(map, varName) => map + (varName -> info.getVarValue(varName).toString)
				}
				if (info.hasOpenAlternatives)
					one :: collect(engine.solveNext)
				else
					one :: Nil
			}
			collect(result)
		}
		else
			Nil
	}
	
	
	def collectProducts(catalog:String) = getAllVarValues("product(" + catalog + ", X).", List("X")).map(_("X"))
	def collectCriteria(catalog:String) = getAllVarValues("criteria(" + catalog + ", X).", List("X")).map(_("X"))
	def collectWeights(catalog:String) = {
		(Map[String, Int]() /: getAllVarValues("criteria_weight(" + catalog + ", X, Y).", List("X", "Y"))) {
			(map, varValues) => map + (varValues("X") -> varValues("Y").toInt)
		}
	}
}
