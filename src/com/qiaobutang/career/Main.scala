package com.qiaobutang.career

import scala.swing._
import scala.swing.event._
import java.awt.Toolkit

object Main {
	
	def main(args: Array[String]) {
		val productCatalog = "computer"
		val kb = new KB("prolog/backgrounds.pl")
		
		val customer = new CustomerRole(productCatalog, kb, new KB("prolog/customer.pl"))
		val seller = new SellerRole(productCatalog, kb, new KB("prolog/seller.pl"))
		
		
		new SimpleSwingApplication {
			def top = new MainFrame {
				title = "虚拟职场 原型展示 之 卖电脑"
				val frameWidth = 640
				val frameHeight = 480
				val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
				location = new Point(
					(screenSize.width - frameWidth) / 2,
					(screenSize.height - frameHeight) / 2
				)
				
				val textArea = new TextArea() {
					lineWrap = true
					editable = false
				}
				val button = new Button {
					text = "请点我, 谢谢"
				}
				
				contents = new SplitPane(
					Orientation.Vertical,
					new ScrollPane(textArea) {
						verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
						horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
					},
					button
				) {
					dividerLocation = frameWidth * 3 / 5
					oneTouchExpandable = false
				}
				size = new Dimension(frameWidth, frameHeight)
			}
			
			main(args)
		}
	}
	
}
