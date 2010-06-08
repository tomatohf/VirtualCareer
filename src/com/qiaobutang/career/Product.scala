package com.qiaobutang.career

abstract class Product {
	def criteria:List[Symbol]
}

object Computer extends Product {
	val criteria = List(
		'price, 'brand, 'memory, 'disk, 'battery,
		'quality, 'service, 'screen, 'color, 'fitting
	)
}