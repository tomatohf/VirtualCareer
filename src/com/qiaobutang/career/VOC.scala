package com.qiaobutang.career

import scala.collection.mutable.{Map, HashMap}

class VOC(criteria:List[Symbol]) {
	private case class View(v:Int, w:Int, p:Int) {
		require(v >= 0 && v <= 100)
		require(w >= 0)
		require(p >= 0 && p <= 1)
	}
	
	private val views = (Map[Symbol, View]() /: criteria) {
		(map, sym) => map + (sym -> View(0, 0, 1))
	}
	
	def weightedAverage = {
		1
	}
}
