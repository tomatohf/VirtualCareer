package com.qiaobutang.career

object Helper {
	def gender_title(gender:Boolean) = if (gender) "先生" else "小姐"
		
	def camelize(underscored:String) = underscored.split("_").map(_.capitalize).mkString
	def underscore(camelcased:String) = """[A-Z][^A-Z]*""".r.findAllIn(camelcased).toArray.mkString("_").toLowerCase
}
