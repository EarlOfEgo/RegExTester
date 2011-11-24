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

package com.regextester
import swing._
import scala.swing.event.Key
import java.awt.GridLayout
import javax.swing.border.LineBorder
import scala.collection.mutable._

class RegExGui(controller: RegExController) extends Frame{
	listenTo(controller)
	title = "RegExTester"
		
	val width = 640
	val height = 480
		
	//FOR TESTING
	var listStrings = List("")
	var listRegExes = List("")
	
	
	var listViewRegExes = new ListView(listRegExes)
	var scrollListRegEx = new ScrollPane()
	
	var listViewStrings = new ListView(listStrings)
	var scrollListStrings = new ScrollPane()
	
	val quitAction = Action("Quit") {System.exit(0)}
	val aboutAction = Action("About") {}
	
	val okAction = Action("Ok") {
		if(stringInput.getInput.size > 0 && regExInput.getInput.size > 0) {
			listRegExes = regExInput.getInput  :: listRegExes
			listStrings = stringInput.getInput :: listStrings 
		}
	}
	
	val okButton = new Button{
							action = okAction
						}
	
	var regExInput = new TextField {
		preferredSize = new Dimension(240,20)
		text = "Enter Your RegEx here"
		def getInput = text
	}
	
	var stringInput = new TextField {
		preferredSize = new Dimension(240,20)
    	text = "Enter Your String here"
    	def getInput = text
    }
	
	var regExToMatch = new TextField {
		preferredSize = new Dimension(240,20)
		editable = false
	}
	
	var stringToMatch = new TextField {
		preferredSize = new Dimension(240,20)
    	editable = false
    }
		
	contents = new BoxPanel(Orientation.Vertical){
	menuBar = new MenuBar{
		contents += new Menu("File"){
			contents += new MenuItem(quitAction)
		}
		contents += new Menu("RegEx"){
		}
		contents += new Menu("History"){
		}
		contents += new Menu("Help"){
			contents += new MenuItem(aboutAction)
		}
	}
		
		contents += new BoxPanel(Orientation.Horizontal){
			border = javax.swing.border.LineBorder.createBlackLineBorder()
			//LEFT SITE
			contents += new BoxPanel(Orientation.Vertical) {
				border = javax.swing.border.LineBorder.createBlackLineBorder()
				contents += new BoxPanel(Orientation.Vertical) {
					contents += new FlowPanel{
						contents += new Label("RegEx:    ")
						contents += regExInput
					}
					contents += new FlowPanel{
						contents += new Label("ToMatch:")
						contents += stringInput
					}
					maximumSize = new Dimension(width/2, 100)
				}
		
				contents += new BorderPanel {
					add(new FlowPanel{
						contents += okButton
						contents += new Button{				
							action = Action("Clear Fields"){
								stringInput text = ""
								regExInput text = ""
							}
						}
					}, BorderPanel.Position.East)
					maximumSize = new Dimension(width/2, 50)
				}
			
				contents += new BoxPanel(Orientation.Horizontal){
					
					
					scrollListRegEx.contents = listViewRegExes
					
					
					scrollListStrings.contents = listViewStrings
					
					contents += scrollListRegEx
					contents += scrollListStrings
					listenTo(okButton)
					reactions += {
						case selected => {
							listViewRegExes = new ListView(listRegExes)
							listViewStrings = new ListView(listStrings)
							scrollListStrings.contents = listViewStrings
							scrollListRegEx.contents = listViewRegExes
						}
					}
				}
				maximumSize = new Dimension(width/2, height-10)
				
			}
			
			//RIGHT SITE
			contents += new BoxPanel(Orientation.Vertical) {
				border = javax.swing.border.LineBorder.createBlackLineBorder()
				contents += new BoxPanel(Orientation.Vertical) {
					contents += new FlowPanel{
						contents += new Label("RegEx:    ")
						contents += regExToMatch
						listenTo(listViewRegExes)
						reactions += {
							case select @ selectionindices => {
								println("->" + select)
								regExToMatch.text = select.toString()
							}
						}
					}
					contents += new FlowPanel{
						contents += new Label("ToMatch:")
						contents += stringToMatch
					}
					maximumSize = new Dimension(width/2, 100)
				}
		
				contents += new BorderPanel {
					add(new FlowPanel{
						contents += new Button{
							action = Action("Check") {
								if(stringInput.getInput.size > 0 && regExInput.getInput.size > 0) {
									
								}
							}	
						}
					}, BorderPanel.Position.East)
					maximumSize = new Dimension(width/2, 50)
				}
							
				contents += new BoxPanel(Orientation.Horizontal){
					
					val listViewRegExes = new ListView(listRegExes)
					val scrollListResult = new ScrollPane()
					scrollListResult.contents = listViewRegExes
					contents += scrollListResult
				}
				maximumSize = new Dimension(width/2, height-10)
			}
		}
		
		contents += new BorderPanel{
			add(new Label("Status: ")
			,BorderPanel.Position.West)
			maximumSize = new Dimension(width, 10)
			background = java.awt.Color.BLUE
		}
	}
	
	centerOnScreen()
	minimumSize = new Dimension(width, height)
	resizable = false
	visible = true
}
