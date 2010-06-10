package com.qiaobutang.career

import jpl.Query

class KB(private val location:String) {
	def init {
		new Query(
			"reconsult('" + location + "')"
		).hasSolution()
	}
}
