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

class HierarchyState extends State with StateMachine {
	def beforeEnter {}
	def afterEnter {}
	override def enter {
		beforeEnter
		currentState.enter
		afterEnter
	}
	
	def beforeExit {}
	def afterExit {}
	override def exit {
		beforeExit
		currentState.exit
		afterExit
	}
	
	def beforeApply {}
	def afterApply {}
	override def apply {
		beforeApply
		super.apply
		afterApply
	}
}


class GreetState extends State {
	def apply {
		
	}
}
