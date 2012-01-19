/*  $Id$
 *
 *  Copyright (c) 2011 	Stephan Hagios <stephan.hagios@gmail.com>
 *  					Felix Schmidt <felixsch@web.de>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.regextester.view

import scala.swing.event.Key
import scala.swing.event.KeyPressed
import scala.swing.event.MouseClicked
import scala.swing.event.WindowClosing
import scala.swing.Dimension
import scala.swing.ListView
import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.FlowPanel
import scala.swing.Frame
import scala.swing.Label
import scala.swing.Menu
import scala.swing.MenuBar
import scala.swing.MenuItem
import scala.swing.Orientation
import scala.swing.ScrollPane
import scala.swing.TextField
import scala.swing.TextArea

import com.regextester.model.RegExModelBase

class RegExGui(model: RegExModelBase) extends Frame with SystemLookAndFeel with LoadAboutHTMLContent {
	
	listenTo(model)
	title = "RegExTester"

	val width = 640
	val height = 480
	
	val boldFont = new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 12)
	val plainFont10 = new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.PLAIN, 10)
	val plainFont12 = new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.PLAIN, 12)

	//FOR TESTING
	var listStrings = List("")
	var listRegExes = List("")

	var listViewRegExes = new ListView(listRegExes) {
		font = plainFont12
		foreground = java.awt.Color.WHITE
		background = java.awt.Color.DARK_GRAY
	}
	var scrollListRegEx = new ScrollPane

	var listViewStrings = new ListView(listStrings) {
		font = plainFont12
		foreground = java.awt.Color.WHITE
		background = java.awt.Color.DARK_GRAY
	}
	var scrollListStrings = new ScrollPane
	
	var scrollListResult = new ScrollPane
	var scrollListMatches = new ScrollPane

	val quitAction = Action("Quit") { System.exit(0) }
	val aboutAction = Action("About") {
		val aboutFrame = new Frame {
			title = "About RegExTester"
			
			contents = new BoxPanel(Orientation.Vertical) {
				contents += new FlowPanel {
					contents += new Label {
						var about = ""
						loadLinesFromAboutHtml
						linesFromAboutHtmlList.foreach(e => about = about + e)
						peer.setText(about)
					}
				}
			}
			centerOnScreen
			resizable = false
			visible = true
		}
	}
	
	val historyAction = Action("Show") {
		val historyFrame = new Frame {
			title = "History"
			contents = new BoxPanel(Orientation.Vertical) {
				contents += new FlowPanel {
					contents += new Label("The following regex-string pairs have matched already:") {
						font = boldFont
						preferredSize = new Dimension(400, 25)
					}
				}
				border = javax.swing.border.LineBorder.createBlackLineBorder
				
				model.fo.loadMatches
				model.fo.loadLinesFromMatchesTxt
				var matches = model.fo.linesFromMatchesTxtList
				var modifiedMatches = List[String]()
				var listViewMatches = new ListView(modifiedMatches)
				
				if(matches.isEmpty) {
					modifiedMatches = modifiedMatches :+ "Nothing was added to the History yet!"
						listViewMatches = new ListView(modifiedMatches) {
						font = plainFont12
						foreground = java.awt.Color.YELLOW
						background = java.awt.Color.DARK_GRAY
					}
				}
				else {
					matches = matches.distinct
					matches.foreach(m => modifiedMatches = modifiedMatches :+ m._1 + " -> " + m._2)
					listViewMatches = new ListView(modifiedMatches) {
						font = plainFont12
						foreground = java.awt.Color.WHITE
						background = java.awt.Color.DARK_GRAY
					}
				}
				scrollListMatches.contents = listViewMatches
				contents += scrollListMatches
			}
			centerOnScreen
			preferredSize = new Dimension(300, 300)
			resizable = false
			visible = true
		}
	}

	val okAction = Action("Ok") {
		validateInputSize(stringInput, regExInput)
		okButton.peer.setFocusable(false)
	}

	val okButton : scala.swing.Button = new Button {
		font = boldFont
	  	action = okAction
	}
	
	val clearButton = new Button {
		font = boldFont
		action = Action("Clear Fields") {
			stringInput text = ""
			regExInput text = ""
		}
	}

	var regExInput : TextField = new TextField {
		preferredSize = new Dimension(240, 20)
		text = "Enter Your RegEx here"
		peer.setFont(plainFont10)
		peer.setCaretPosition(text.size)
		peer.setSelectionStart(0)
		peer.setSelectionEnd(text.size)
		peer.setSelectionColor(java.awt.Color.LIGHT_GRAY)
		peer.setSelectedTextColor(java.awt.Color.WHITE)
		
		listenTo(keys)
		reactions += {
		  	case KeyPressed(_, Key.Enter, _, _) => {
		  		enterEvent(stringInput, this)
		  	}
		}
	}

	var stringInput : TextField = new TextField {
		preferredSize = new Dimension(240, 20)
		text = "Enter Your String here"
		peer.setFont(plainFont10)
		peer.setCaretPosition(text.size)
		peer.setSelectionStart(0)
		peer.setSelectionEnd(text.size)
		peer.setSelectionColor(java.awt.Color.LIGHT_GRAY)
		peer.setSelectedTextColor(java.awt.Color.WHITE)
		
		listenTo(keys)
		reactions += {
		  	case KeyPressed(_, Key.Enter, _, _) => {
		  		enterEvent(this, regExInput)
		  	}
		}
	}
	
	def enterEvent(strTF : TextField, regExTF : TextField) = {
		
		validateInputSize(strTF, regExTF)
		  		
  		// to immediately show the result:
  		listViewRegExes = new ListView(listRegExes) {
  			font = plainFont12
  			foreground = java.awt.Color.WHITE
  			background = java.awt.Color.DARK_GRAY
  		}
  		listViewStrings = new ListView(listStrings) {
  			font = plainFont12
  			foreground = java.awt.Color.WHITE
  			background = java.awt.Color.DARK_GRAY
  		}
  		scrollListRegEx.contents = listViewRegExes
		scrollListStrings.contents = listViewStrings
	}
	
	def validateInputSize(strTF : TextField, regExTF : TextField) = {
		if(strTF.text.size > 0 && regExTF.text.size > 0) {
			if(!listRegExes.exists(r => r == regExTF.text))
				listRegExes = regExTF.text :: listRegExes
				
			if(!listStrings.exists(s => s == strTF.text))
				listStrings = strTF.text :: listStrings
		}
	}

	val regExToMatch = new TextField {
		font = plainFont10
		preferredSize = new Dimension(240, 20)
		editable = false
	}

	val stringToMatch = new TextField {
		font = plainFont10
		preferredSize = new Dimension(240, 20)
		editable = false
	}

	contents = new BoxPanel(Orientation.Vertical) {
		menuBar = new MenuBar {
			contents += new Menu("File") {
				font = boldFont
				contents += new MenuItem(quitAction) {
					font = boldFont
				}
			}
			contents += new Menu("History") {
				font = boldFont
				contents += new MenuItem(historyAction) {
					font = boldFont
				}
			}
			contents += new Menu("Help") {
				font = boldFont
				contents += new MenuItem(aboutAction) {
					font = boldFont
				}
			}
		}

		contents += new BoxPanel(Orientation.Horizontal) {
			border = javax.swing.border.LineBorder.createBlackLineBorder
			//LEFT SIDE
			contents += new BoxPanel(Orientation.Vertical) {
				border = javax.swing.border.LineBorder.createBlackLineBorder
				contents += new BoxPanel(Orientation.Vertical) {
					contents += new FlowPanel {
						contents += new Label("RegEx:  ") {
							font = boldFont
						}
						contents += regExInput
					}
					contents += new FlowPanel {
						contents += new Label("ToMatch:") {
							font = boldFont
						}
						contents += stringInput
					}
					maximumSize = new Dimension(width / 2, 100)
				}

				contents += new BorderPanel {
					add(new FlowPanel {
						contents += okButton
						contents += clearButton
					}, BorderPanel.Position.East)
					maximumSize = new Dimension(width / 2, 50)
				}

				contents += new BoxPanel(Orientation.Horizontal) {

					scrollListRegEx.contents = listViewRegExes
					scrollListStrings.contents = listViewStrings

					contents += scrollListRegEx
					contents += scrollListStrings
					listenTo(okButton)
					reactions += {
						case selected => {
							listViewRegExes = new ListView[String]() {
								font = plainFont12
								foreground = java.awt.Color.WHITE
								background = java.awt.Color.DARK_GRAY
								listData = listRegExes
								listenTo(mouse.clicks)
								reactions += {
								  	case e : MouseClicked => listViewRegExes.selection.items.foreach(i => regExToMatch.text = i)
								}
							}
							
							listViewStrings = new ListView[String]() {
								font = plainFont12
								foreground = java.awt.Color.WHITE
								background = java.awt.Color.DARK_GRAY
								listData = listStrings
								listenTo(mouse.clicks)
								reactions += {
								  	case e : MouseClicked => listViewStrings.selection.items.foreach(i => stringToMatch.text = i)
								}
							}
							scrollListStrings.contents = listViewStrings
							scrollListRegEx.contents = listViewRegExes
						}
					}
				}
				maximumSize = new Dimension(width / 2, height - 10)

			}

			//RIGHT SIDE
			contents += new BoxPanel(Orientation.Vertical) {
				border = javax.swing.border.LineBorder.createBlackLineBorder
				contents += new BoxPanel(Orientation.Vertical) {
					contents += new FlowPanel {
						contents += new Label("RegEx:  ") {
							font = boldFont
						}
						contents += regExToMatch
					}
					contents += new FlowPanel {
						contents += new Label("ToMatch:") {
							font = boldFont
						}
						contents += stringToMatch
					}
					maximumSize = new Dimension(width / 2, 100)
				}

				contents += new BorderPanel {
					add(new FlowPanel {
						contents += new Button {
							font = boldFont
							action = Action("Check") {
								if(regExToMatch.text.size > 0 && stringToMatch.text.size > 0) {
									var result = model.checkWholeExpression(regExToMatch.text, stringToMatch.text)
									var fail = result.filter(f => f._2.size == 0)
									if(!fail.isEmpty) {
										var falseResult :List[String] = List()
										result.filter(res => res._2.size > 0).foreach(r => {
											falseResult = falseResult :+ r._1 + " matched with " + r._2
										})
										fail.foreach(f => {
											falseResult = falseResult :+ f._1 + " doesn't matched with " + f._2
										})
										val resultListView = new ListView(falseResult) {
											font = boldFont
											foreground = java.awt.Color.RED
											background = java.awt.Color.DARK_GRAY
										}
										scrollListResult.contents = resultListView
									}
									else {
										var trueResult : List[String] = List()
										result.foreach(res => trueResult = trueResult :+ res._1 + " matched with " + res._2)
										val resultListView = new ListView(trueResult) {
											font = plainFont12
											foreground = java.awt.Color.GREEN
											background = java.awt.Color.DARK_GRAY
										}
										scrollListResult.contents = resultListView
									}
								}
								else if(regExToMatch.text.isEmpty && stringToMatch.text.size > 0) {
									val emptyRegExField = "The 'RegEx'-Field is empty!" :: Nil
									val resultListView = new ListView(emptyRegExField) {
										font = boldFont
										foreground = java.awt.Color.YELLOW
										background = java.awt.Color.DARK_GRAY
									}
									scrollListResult.contents = resultListView
								}
								else if(stringToMatch.text.isEmpty && regExToMatch.text.size > 0) {
									val emptyStringField = "The 'ToMatch'-Field is empty!" :: Nil
									val resultListView = new ListView(emptyStringField) {
										font = boldFont
										foreground = java.awt.Color.YELLOW
										background = java.awt.Color.DARK_GRAY
									}
									scrollListResult.contents = resultListView
								}
								else if(regExToMatch.text.isEmpty && stringToMatch.text.isEmpty) {
									val emptyFields = "Both fields are empty!" :: Nil
									val resultListView = new ListView(emptyFields) {
										font = boldFont
										foreground = java.awt.Color.YELLOW
										background = java.awt.Color.DARK_GRAY
									}
									scrollListResult.contents = resultListView
								}
							}
						}
					}, BorderPanel.Position.East)
					maximumSize = new Dimension(width / 2, 50)
				}

				contents += new BoxPanel(Orientation.Horizontal) {

					val listViewRegExes = new ListView(listRegExes) {
						font = plainFont12
						foreground = java.awt.Color.WHITE
						background = java.awt.Color.DARK_GRAY
					}
					scrollListResult.contents = listViewRegExes
					contents += scrollListResult
				}
				maximumSize = new Dimension(width / 2, height - 10)
			}
		}
	}

	reactions += {
	  case WindowClosing(e) => System.exit(0)
	}
	
	centerOnScreen
	minimumSize = new Dimension(width, height)
	resizable = false
	visible = true
}
