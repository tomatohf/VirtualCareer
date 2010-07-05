package com.qiaobutang.career

import Helper._

trait Determinable {
	def determine:List[Action]
}

trait StateMachine extends Determinable {
	protected var globalState:State = EmptyState
	protected var currentState:State = EmptyState
	protected var previousState:State = EmptyState
	
	override def determine = globalState.determine ++ currentState.determine
	
	def changeState(newState:State) {
		previousState = currentState
		currentState.exit
		currentState = newState
		currentState.enter
	}
	
	def revertState {
		changeState(previousState)
	}
}

object State extends Factory {
	val POSTFIX = "State"
	type InstanceClass = State
}

abstract class State extends Determinable {
	def role:Role
	
	def enter {}
	def exit {}

	def name = State.name(this)
	
	def determine = {
		role.privateKB.handleStateActions[Action] (name) {
			Action(role, _, _:_*)
		}
	}
}

object EmptyState extends State {
	val role = null
	override def determine = List()
}

abstract class HierarchyState extends State with StateMachine


class SellerRoleState(val role:Role, args:Array[String]) extends State {
}

class GreetState(val role:Role, args:Array[String]) extends State {
}
