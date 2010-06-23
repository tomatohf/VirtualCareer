package com.qiaobutang.career

abstract class Role extends StateMachine {
	def name:String
	
	protected var stamina = 100
	protected var mood = 100
	require(stamina >= 0 && stamina <= 100)
	require(mood >= 0 && mood <= 100)
	
	def determine(takenAction:Action):List[Action]
}

class CustomerRole(
	val name:String,
	private val productCatalog:String,
	private val publicKB:KB,
	private val privateKB:KB
) extends Role {
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
	val name:String,
	private val productCatalog:String,
	private val publicKB:KB,
	private val privateKB:KB
) extends Role {
	def determine(takenAction:Action) = {
		List(new GreetAction(this), new ComplimentAction(this), new ThankAction(this))
	}
}
