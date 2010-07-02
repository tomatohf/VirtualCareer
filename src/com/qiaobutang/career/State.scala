package com.qiaobutang.career

import Helper._

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

object State {
	val PACKAGE_NAME = "com.qiaobutang.career"
	val POSTFIX = "State"
		
	def apply(name:String, args:Any *) = 
		Class.forName(PACKAGE_NAME + "." + camelize(name) + POSTFIX).getDeclaredConstructor(
			classOf[Array[Any]]
		).newInstance(args.toArray).asInstanceOf[State]
}

abstract class State {
	def enter {}
	def exit {}
	def determine:List[Action]
	
	def name = underscore(this.getClass.getSimpleName).dropRight(State.POSTFIX.size)
}

object IdleState extends State {
	def determine = List()
}

class HierarchyState extends State with StateMachine


class SellerRoleState(args:Array[Any]) extends State {
	val seller = args(0).asInstanceOf[SellerRole]
	
	def determine = {
		List(
			Action(seller, "wait_continue")
		)
	}
}

class GreetState(args:Array[Any]) extends State {
	def determine = {
		List(
			
		)
	}
}
