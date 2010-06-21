package com.qiaobutang.career

object Action {
	import scala.collection.mutable.HashMap
	type Creator = (Role, Array[String]) => Action
	private val registration = HashMap[String, Creator]()
	def register (name:String) (creator: Creator) { registration.put(name, creator) }
	def unregister (name:String) { registration.remove(name) }
	
	register ("greet") { (role, args) => new GreetAction(role) }
	register ("compliment") { (role, args) => new ComplimentAction(role) }
	register ("thank") { (role, args) => new ThankAction(role) }
	
	def apply(name:String, role:Role, args:String *) = {
		registration.get(name) match {
			case Some(creator) => creator(role, args.toArray)
			case None => error("Unregistered action: " + name)
		}
	}
}

abstract class Action {
	def role:Role
	def title:String
	
	def perform
	def apply() {
		perform
	}
}


class GreetAction(val role:Role) extends Action {
	val title = "打招呼"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}

class ComplimentAction(val role:Role) extends Action {
	val title = "称赞对方"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}

class ThankAction(val role:Role) extends Action {
	val title = "感谢对方"
	val descriptions = List()
	def perform {
		println(title + " executed")
	}
}
