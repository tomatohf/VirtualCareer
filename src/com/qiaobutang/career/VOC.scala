package com.qiaobutang.career

import scala.collection.mutable.{Map, HashMap}

class VOC(criteria:List[String]) {
	case class View(value:Int, possibility:Int, weight:Int) {
		require(value >= 0 && value <= 100)
		require(possibility >= 0 && possibility <= 1)
		require(weight >= 0)
	}
	
	val views = (Map[String, View]() /: criteria) {
		(map, sym) => map + (sym -> View(0, 1, 0))
	}
	
	
	def weightedAverage = {
		val (values, weights) = ((0, 0) /: views) {
			(sum, voc) => {
				val View(value, possibility, weight) = voc._2
				(sum._1 + (value*possibility*weight), sum._2 + weight)
			}
		}
		values.toFloat / weights
	}
	
	def setWeight(criteria:String, weight:Int) {
		val view = views(criteria)
		views(criteria) = View(view.value, view.possibility, weight)
		this
	}
}
