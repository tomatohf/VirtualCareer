package com.qiaobutang.career

object Main {
	
	def main(args: Array[String]) {
		val productCatalog = "computer"
		val kb = new KB("prolog/backgrounds.pl")
		
		val customer = new CustomerRole(productCatalog, kb, new KB("prolog/customer.pl"))
		val seller = new SellerRole(productCatalog, kb, new KB("prolog/seller.pl"))
		
		
	}
	
}
