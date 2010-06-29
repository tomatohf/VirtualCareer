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
	
	def determine(takenAction:Action):List[Action]
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
	
	def determine(takenAction:Action) = {
		List(new ThankAction(this))
	}
}

class SellerRole(
	val id:String,
	private val productCatalog:String,
	val publicKB:PublicKB,
	val privateKB:PrivateKB
) extends Role {
	val label = "销售员"
	
	def determine(takenAction:Action) = {
		takenAction.effect(this)
		
		takenAction match {
			case AppearAction(role) => changeState(new GreetState)
			case _ => // do nothing
		}
		
		List(new GreetAction(this), new ComplimentAction(this), new ThankAction(this))
	}
}
