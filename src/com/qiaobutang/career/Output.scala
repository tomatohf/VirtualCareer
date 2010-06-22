package com.qiaobutang.career

object Output {
	val newline = "\n"
	var default:Output = ConsoleOutput
}

abstract class Output {
	def append(text:String)
}

object ConsoleOutput extends Output {
	def append(text:String) { Console.println(text) }
}

import scala.swing.TextArea
class TextAreaOutput(private val textArea:TextArea) extends Output {
	def append(text:String) { textArea.append(text + Output.newline + Output.newline) }
}
