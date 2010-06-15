package com.qiaobutang.career

class Action {
	def apply() {
		
	}
	
	val now = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date())
	def name = "action_" + now
	def label = "行动_" + now
}
