package com.qiaobutang.career

trait StateMachine {
	protected var globalState:State = IdleState
	protected var currentState:State = IdleState
	protected var previousState:State = IdleState
	
	def determine = globalState.determine ++ currentState.determine
	
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

abstract class State {
	def enter {}
	def exit {}
	def determine:List[Action]
}

object IdleState extends State { def determine = List() }

class HierarchyState extends State with StateMachine


class SellerRoleState(val seller:SellerRole) extends State {
	def determine = {
		List(
			Action("wait_continue", seller)
		)
	}
}

class GreetState extends State {
	def determine = {
		List(
			
		)
	}
}
