package com.qiaobutang.career

import Helper._

object Action {
	import scala.collection.mutable.HashMap
	type Creator = (Role, Array[String]) => Action
	private val registration = HashMap[String, Creator]()
	def register (name:String) (creator: Creator) { registration.put(name, creator) }
	def unregister (name:String) { registration.remove(name) }
	
	register ("wait_continue") { (role, args) => new WaitContinueAction(role) }
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
	
	def effect(to:Role) {}
}


class WaitContinueAction(val role:Role) extends Action {
	val title = "(什么也不做) 等待对方继续"
	def perform {
		output("(什么也没说) ... ...")
	}
}

class AppearAction(val role:Role) extends Action {
	val title = "出现"
	def perform {
		val gender = role.privateKB.getGender(role.id)
		output(
			"(一位年轻的" + (if (gender.isEmpty) role.label else gender_title(gender.get)) + ")走进店里"
		)
	}
	override def effect(to:Role) {
		updateAppearenceInfo(role, to)
	}
	protected def updateAppearenceInfo(from:Role, to:Role) {
		val gender = from.privateKB.getGender(from.id)
		if (!gender.isEmpty) to.privateKB.setGender(from.id, gender.get)
	}
}

class GreetAction(role:Role) extends AppearAction(role) {
	override val title = "打招呼"
	override def perform {
		output("你好")
	}
	override def effect(to:Role) {
		super.effect(to)
		updateAppearenceInfo(to, role)
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
