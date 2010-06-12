package com.qiaobutang.career

import scala.collection.JavaConversions._

object Main {
	
	def main(args: Array[String]) {
		val productCatalog = "computer"
		val kb = new KB("prolog/backgrounds.pl")
		
		val customer = new CustomerRole(productCatalog, kb, new KB("prolog/customer.pl"))
		
		kb.collectProducts("computer").foreach(println _)
	}
	
}
