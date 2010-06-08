package com.qiaobutang.career

abstract class Role

class CustomerRole(val product:Product) extends Role {
	var voc = new VOC(product.criteria)
}

class SellerRole extends Role {
	
}
