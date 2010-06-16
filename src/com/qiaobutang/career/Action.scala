package com.qiaobutang.career

class Action {
	def apply() {
		
	}
	
	val rand = new java.util.Random().nextInt
	def name = "action_" + rand
	def label = "行动_" + rand
}
