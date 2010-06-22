package com.qiaobutang.career

import scala.swing._
import scala.swing.event._
import java.awt.Toolkit

object Main {
	
	def main(args: Array[String]) {
		val productCatalog = "computer"
		val kb = new KB("prolog/backgrounds.pl")
		
		val customer = new CustomerRole("客户", productCatalog, kb, new KB("prolog/customer.pl"))
		val seller = new SellerRole("小堂", productCatalog, kb, new KB("prolog/seller.pl"))
		
		def actionSelected(sellerAction:Action) = {
			sellerAction()
			
			val customerAction = customer.determine(sellerAction).head
			customerAction()
			
			seller.determine(customerAction)
		}
		
		val initActions = List(new GreetAction(seller), new ComplimentAction(seller), new ThankAction(seller))
		
		
		val app = new SimpleSwingApplication {
			val textArea = new TextArea() {
				lineWrap = true
				editable = false
			}
			Output.default = new TextAreaOutput(textArea)
			val optionsContainer = new BoxPanel(Orientation.Vertical) {
				border = Swing.EmptyBorder(8, 0, 20, 0)
				var options = new ButtonGroup
				def clear {
					contents.clear()
					options = new ButtonGroup
				}
			}
			val button = new Button {
				text = "确定"
			}
			listenTo(button)
			reactions += {
				case ButtonClicked(_) => buttonClicked
			}
			fillOptions(initActions)
			
			class ActionRadioButton(val target:Action) extends RadioButton(target.title)
			
			def buttonClicked {
				val selectedAction = optionsContainer.options.selected match {
					case Some(radio) => radio.asInstanceOf[ActionRadioButton].target
					case None => return
				}
				
				button.enabled = false
				fillOptions(actionSelected(selectedAction))
				button.enabled = true
			}
			
			def fillOptions(actions:List[Action]) {
				optionsContainer.clear
				actions.foreach(
					action => {
						val radio = new ActionRadioButton(action)
						optionsContainer.contents += radio
						optionsContainer.options.buttons += radio
					}
				)
				optionsContainer.revalidate()
				optionsContainer.repaint()
			}
			
			
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
		
		app.main(args)
	}
	
}
