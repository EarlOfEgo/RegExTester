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


class RegExController {
  
	// init Model
	var modelBase = new RegExModelBase()
	// init View tui
	var tui = new RegExTui()
	
	tui.printMenu()
	
  
	/**
	 * Checks the RegEx
	 * */
	def checkExpStep(regEx: String, toMatch: String) = {
			  
	}
	
	/**
	 * Checks whole expression with whole string
	 * */
	def checkWholeExpression(regEx: String, toMatch: String) = {
		true	  
	}
	
	/**
	 * Cuts the string into pieces
	 * */
	def cutString(statement: String) = {
	  
	}
	
	/**
	 * Saves the result in an external file
	 **/
	def saveResult() = {
	  
	}
	
	/**
	 * Cuts the regex into pieces
	 * */
	def cutRegEx(regEx: String) = {
		val digit = """(\\d)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val nonDigit = """(\\D)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val word = """(\\w)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val nonWord = """(\\W)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val space = """(\\s)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val nonSpace = """(\\S)(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		val other = """.*(*|?|+|{\d+}|{\d+,\d*})?.*""".r
		regEx match {
			case digit(v1, v2) => 
			case nonDigit(v1, v2) =>
			case word(v1, v2) => 
			case nonWord(v1, v2) =>
			case space(v1, v2) => 
			case nonSpace(v1, v2) =>
			case other(v1, v2) =>
			case _ =>
		}
	}
	
	/**
	 * when tui is used
	 * */
	def usingTui() = {
		var run = true
		
		while(run) {
			val reg = ":r (.*)".r
			val str = ":s (.*)".r
			val digit = """(\\d)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?.*""".r
			var input = tui.readInput()
			tui.writeOutput(input)
			input match {
				case ":quit" => run = false
				case reg(v) => modelBase.regEx = v		// Save regex in model
				case str(v) => modelBase.toMatch = v 	// Save string in model
				case ":a" => tui.writeOutput(if(checkWholeExpression(modelBase.regEx, modelBase.toMatch)) "matched" else "not matched") 
				case ":s" => checkExpStep(modelBase.expression1, modelBase.toMatch)
				case digit(v, v2) => println(v + " " +v2)
				case _ => 
			}
			
		}
	}

}