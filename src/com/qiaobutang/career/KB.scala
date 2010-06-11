package com.qiaobutang.career

import alice.tuprolog.Prolog

class KB(private val location:String) {
	private val prolog = new Prolog()
	
	def init {
		println(prolog.isHalted)
	}
}
