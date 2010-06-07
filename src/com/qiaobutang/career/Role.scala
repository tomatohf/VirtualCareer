package com.qiaobutang.career

trait VOC {
	case class View(v:Int, w:Int, p:Int)
	
	// var voc:Map[Symbol, View]
	
	def normalize = {
		1
	}
}

abstract class Role

class CustomerRole extends Role with VOC {
	
}

class SellerRole extends Role {
	
}

class Product {
	
}
