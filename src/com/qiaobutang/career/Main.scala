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
		
		def actionSelected() = {
			val sellerAction = new Action()
			sellerAction()
			
			val customerAction = customer.determine(sellerAction)
			customerAction()
			
			seller.determine(customerAction).map(action => Array(action.name, action.label))
		}
		
		
		new SimpleSwingApplication {
			val textArea = new TextArea() {
				lineWrap = true
				editable = false
			}
			val optionsContainer = new BoxPanel(Orientation.Vertical) {
				border = Swing.EmptyBorder(8, 0, 20, 0)
				val options = new ButtonGroup
			}
			val button = new Button {
				text = "确定"
			}
			listenTo(button)
			reactions += {
				case ButtonClicked(_) => buttonClicked
			}
			
			def buttonClicked {
				button.enabled = false
				println("selected radio: " + optionsContainer.options.selected)
				fillOptions(actionSelected())
				button.enabled = true
			}
			
			def fillOptions(options:List[Array[String]]) {
				optionsContainer.contents.clear()
				optionsContainer.options.buttons.clear()
				options.foreach(
					option => {
						val radio = new RadioButton(option(1))
						optionsContainer.contents += radio
						optionsContainer.options.buttons += radio
					}
				)
				optionsContainer.revalidate()
				optionsContainer.repaint()
			}
			
			main(args)
			
			def top = new MainFrame {
				title = "虚拟职场 原型展示 之 卖电脑"
				val frameWidth = 960
				val frameHeight = 600
				val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
				location = new Point(
					(screenSize.width - frameWidth) / 2,
					(screenSize.height - frameHeight) / 2
				)
				
				contents = new SplitPane(
					Orientation.Vertical,
					new ScrollPane(textArea) {
						verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
						horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
					},
					new ScrollPane(
						new BoxPanel(Orientation.Vertical) {
							border = Swing.EmptyBorder(10, 5, 10, 5)
							contents += new Label("选择你的行动:")
							contents += optionsContainer
							contents += button
						}
					)
				) {
					dividerLocation = frameWidth / 2
					oneTouchExpandable = false
				}
				size = new Dimension(frameWidth, frameHeight)
			}
		}
	}
	
}
