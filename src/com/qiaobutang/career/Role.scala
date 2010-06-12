package com.qiaobutang.career

abstract class Role {
	protected var stamina = 100
	protected var mood = 100
	
	require(stamina >= 0 && stamina <= 100)
	require(mood >= 0 && mood <= 100)
}

class CustomerRole(
	private val productCatalog:String,
	private val publicKB:KB,
	private val privateKB:KB
) extends Role {
	
//	val vocs = (Map[String, VOC]() /: product.competitors) {
//		(map, sym) => map + (sym -> new VOC(product.criteria))
//	}
}

class SellerRole extends Role {
	
}
