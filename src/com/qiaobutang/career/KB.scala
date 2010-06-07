package com.qiaobutang.career

import jpl.Query

class KB {
	def testProlog {
		val t1 = "consult('prolog/backgrounds.pl')"
		val q1 = new Query(t1)

		println(q1.hasSolution())

		//--------------------------------------------------

		val t2 = "child_of(joe, ralf)"
		val q2 = new Query(t2)

		println(q2.hasSolution())

		//--------------------------------------------------

		val t3 = "descendent_of(steve, ralf)"
		val q3 = new Query(t3)

		println(q3.hasSolution())

		//--------------------------------------------------

		val t4 = "descendent_of(X, ralf)"
		val q4 = new Query(t4)

		println( "first solution of " + t4 + ": X = " + q4.oneSolution().get("X"))
	}
}
