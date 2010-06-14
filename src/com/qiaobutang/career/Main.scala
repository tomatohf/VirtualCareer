package com.qiaobutang.career

import scala.swing._
import scala.swing.event._

object Main {
	
	def main(args: Array[String]) {
		val productCatalog = "computer"
		val kb = new KB("prolog/backgrounds.pl")
		
		val customer = new CustomerRole(productCatalog, kb, new KB("prolog/customer.pl"))
		val seller = new SellerRole(productCatalog, kb, new KB("prolog/seller.pl"))
		
		
		object app extends SimpleSwingApplication {
			def top = new MainFrame {
				title = "First Swing App"
				val button = new Button { text = "Click me" }
				button.text = "请点我, 谢谢"
				val label = new Label { text = "没点过" }
				contents = new BoxPanel(Orientation.Vertical) {
					contents += button
					contents += label
					border = Swing.EmptyBorder(30, 30, 10, 30)
				}
				var nClicks = 0
				reactions += {
					case ButtonClicked(_) =>
						nClicks += 1
						label.text = "已点击 " + nClicks + " 次"
				}
				listenTo(button)
			}
		}
		app.main(args)
	}
	
}
