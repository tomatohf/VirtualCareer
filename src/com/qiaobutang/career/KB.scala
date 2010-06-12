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
	
	
	def getEngine = engine
	
	def collectProducts(catalog:String) = {
		def extractInfo(info:SolveInfo):List[String] = {
			if (!info.hasOpenAlternatives)
				List(info.getVarValue("X").toString)
			else
				info.getVarValue("X").toString :: extractInfo(engine.solveNext)
		}
		
		val info = engine.solve("product(" + catalog + ", X).")
		extractInfo(info)
	}
}
