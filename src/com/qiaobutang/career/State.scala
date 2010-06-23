package com.qiaobutang.career

trait StateMachine {
	protected var globalState:State = IdleState
	protected var currentState:State = IdleState
	protected var previousState:State = IdleState
	
	def apply() {
		globalState()
		currentState()
	}
	
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
	def apply()
}

object IdleState extends State { def apply {} }

class AbcState(protected val role:Role) extends State {
	def apply {
		
	}
}
