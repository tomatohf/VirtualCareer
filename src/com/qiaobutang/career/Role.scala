package com.qiaobutang.career

abstract class Role extends StateMachine {
	def label:String
	def id:String
	def publicKB:PublicKB
	def privateKB:PrivateKB
	
	protected var stamina = 100
	protected var mood = 100
	require(stamina >= 0 && stamina <= 100)
	require(mood >= 0 && mood <= 100)
}

class CustomerRole(
	val id:String,
	private val productCatalog:String,
	val publicKB:PublicKB,
	val privateKB:PrivateKB
) extends Role {
	val label = "客户"
	private val vocs = {
		val criteria = publicKB.collectCriteria(productCatalog)
		var weights = privateKB.collectWeights(productCatalog)
		(Map[String, VOC]() /: publicKB.collectProducts(productCatalog)) {
			(map, sym) => {
				val voc = new VOC(criteria)
				weights.foreach(t => voc.setWeight(t._1, t._2))
				map + (sym -> voc)
			}
		}
	}
	
	override def determine = {
		List(Action(this, "thank"))
	}
}

class SellerRole(
	val id:String,
	private val productCatalog:String,
	val publicKB:PublicKB,
	val privateKB:PrivateKB
) extends Role {
	val label = "销售员"
	globalState = State(this, "seller_role")
	
	override def determine = {
		val state = privateKB.handleFirstStateTransition[State] (currentState.name) {
			State(this, _, _:_*)
		}
		if(!state.isEmpty) changeState(state.get)
		
		super.determine
	}
}
