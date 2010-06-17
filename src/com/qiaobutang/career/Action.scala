package com.qiaobutang.career

import java.util.Random

object Action {
	def apply(name:String, args:String *) = {
		
	}
}

abstract class Action {
	def title:String
	
	protected def descriptions:List[String]
	protected def description = descriptions((new Random()).nextInt(descriptions.size))
	
	def perform
	def apply() = {
		perform
		description
	}
}


class GreetAction extends Action {
	val title = "打招呼"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}

class ComplimentAction extends Action {
	val title = "称赞对方"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}

class ThankAction extends Action {
	val title = "感谢对方"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}
