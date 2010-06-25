package com.qiaobutang.career

import Helper._

object Action {
	import scala.collection.mutable.HashMap
	type Creator = (Role, Array[String]) => Action
	private val registration = HashMap[String, Creator]()
	def register (name:String) (creator: Creator) { registration.put(name, creator) }
	def unregister (name:String) { registration.remove(name) }
	
	register ("appear") { (role, args) => new AppearAction(role) }
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
	
	def output(text:String) {
		if (Output.default.isInstanceOf[RoleTextAreaOutput])
			Output.default.asInstanceOf[RoleTextAreaOutput].appendBy(text, role)
		else
			Output.default.append(role.privateKB.name(role.id).getOrElse(role.label) + ":    " + text)
	}
	
	def perform
	def apply() {
		perform
	}
}


class AppearAction(val role:Role) extends Action {
	val title = "出现"
	def perform {
		val gender = role.privateKB.gender(role.id)
		if (gender.isEmpty) error("One must know own gender")
		
		output("(一位年轻的" + gender_title(gender.get) + ")走进店里")
	}
}

class GreetAction(val role:Role) extends Action {
	val title = "打招呼"
	def perform {
		output(title + " executed")
	}
}

class ComplimentAction(val role:Role) extends Action {
	val title = "称赞对方"
	def perform {
		output(title + " executed")
	}
}

class ThankAction(val role:Role) extends Action {
	val title = "感谢对方"
	def perform {
		output(title + " executed")
	}
}
