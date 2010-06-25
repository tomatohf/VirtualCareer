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

class RoleTextAreaOutput(
	private val textArea:TextArea,
	private val role:Role
) extends TextAreaOutput(textArea) {
	def appendBy(text:String, by:Role) {
		append(role.privateKB.name(by.id).getOrElse(by.label) + ":    " + text)
	}
}
