package com.qiaobutang.career

import Helper._

trait Factory {
	val PACKAGE_NAME = "com.qiaobutang.career"
	val POSTFIX:String
	type InstanceClass <: Object
	
	def apply(role:Role, name:String, args:Any*) = 
		Class.forName(PACKAGE_NAME + "." + camelize(name) + POSTFIX).getDeclaredConstructor(
			classOf[Role], classOf[Array[Any]]
		).newInstance(role, args.toArray).asInstanceOf[InstanceClass]
	
	def name(ins:InstanceClass) = underscore(ins.getClass.getSimpleName).dropRight(POSTFIX.size+1)
}

object Action extends Factory {
	val POSTFIX = "Action"
	type InstanceClass = Action
}

abstract class Action {
	def role:Role
	def title:String
	
	def name = Action.name(this)
	
	def output(text:String) {
		if (Output.default.isInstanceOf[RoleTextAreaOutput])
			Output.default.asInstanceOf[RoleTextAreaOutput].appendBy(text, role)
		else
			Output.default.append(role.privateKB.name(role.id).getOrElse(role.label) + ":    " + text)
	}
	
	def perform
	def effect(to:Role) {}
	def apply(toRoles:Role*) {
		perform
		toRoles.foreach(effect(_))
	}
}


class WaitContinueAction(val role:Role, args:Array[Any]) extends Action {
	val title = "(什么也不做) 等待对方继续"
	def perform {
		output("(什么也没说) ... ...")
	}
}

class AppearAction(val role:Role, args:Array[Any]) extends Action {
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

class GreetAction(role:Role, args:Array[Any]) extends AppearAction(role, args) {
	override val title = "打招呼"
	override def perform {
		output("你好")
	}
	override def effect(to:Role) {
		super.effect(to)
		updateAppearenceInfo(to, role)
	}
}

class ComplimentAction(val role:Role, args:Array[Any]) extends Action {
	val title = "称赞对方"
	def perform {
		output(title + " executed")
	}
}

class ThankAction(val role:Role, args:Array[Any]) extends Action {
	val title = "感谢对方"
	def perform {
		output(title + " executed")
	}
}
