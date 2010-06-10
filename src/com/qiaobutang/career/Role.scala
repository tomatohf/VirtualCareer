package com.qiaobutang.career

abstract class Role

class CustomerRole(val product:Product) extends Role {
	val vocs = (Map[Symbol, VOC]() /: product.competitors) {
		(map, sym) => map + (sym -> new VOC(product.criteria))
	}
}

class SellerRole extends Role {
	
}
