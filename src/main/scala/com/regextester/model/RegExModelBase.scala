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
package com.regextester.model

import scala.Console.GREEN
import scala.Console.RED
import scala.Console.RESET
import scala.Console.YELLOW
import scala.collection.mutable.HashMap
import scala.swing.Publisher

import com.regextester.controller.RegExController
import com.regextester.view.RegExView

class RegExModelBase(rec: RegExController) extends Publisher {
	
	/**
	 * reference to the controller
	 */
	var rec_ : RegExController = _
	
	/**
	 * reference to the file-ops class 'FileOpsOnMatchesTxt'
	 */
	val fo = new FileOpsOnMatchesTxt(this)
	
	/**
	 * Lists that contain the Regular Expressions and Strings typed in in the prompt
	 */
	var matchedReg : List[String] = Nil
	var matchedStr : List[String] = Nil
	
	/**
	 * by default the output to the console isn't colored
	 */
	implicit val defaultColor = RESET
	
	/**
	 * method for introducing model with controller and vice versa
	 */
	def init(rec : RegExController) = {
		rec_ = rec
		rec remb = this
	}
	
	/**
	 * introducing model with controller and vice versa
	 */
	init(rec)
	
	/**
	 * invoke method with same name on the controller
	 */
	def addView(rev : RegExView) : Unit = rec_ addView(rev)
	
	/**
	 * invoke method with same name on the controller
	 */
	def removeView(rev : RegExView) : Unit = rec_ removeView(rev)
	
	/**
	 * invoke method notifyViews on the controller
	 */
	def notifyC(s: String)(implicit color: String) : Unit = rec_.notifyViews(s)(color)
	
	/**
	 * invoke method invokeSetRun on the controller
	 */
	def quitInvoke : Unit = rec_ invokeSetRun false
	
	/**
	 * invoke method invokeHelpScreen on the controller
	 */
	def helpInvoke : Unit = rec_ invokeHelpScreen
	
	/**
	 * delegates a single line of the opened file as String to the controller
	 */
	def sendMatchesFromFileToC(s : String) = rec_ sendMatchesFromCToV(s)
	
	/**
	 * match and with the input String
	 */
	def matchString(s : String) = {
		val reg = ":r (.*)".r
		val str = ":s (.*)".r
		val check = ":c (\\d{1}) (\\d{1})".r
		
		s match {
			case reg(v) => if(matchedReg.exists(r => r == v)) notifyC("RegEx was already typed in!")(YELLOW) else matchedReg = matchedReg :+ v
			case str(v) => if(matchedStr.exists(s => s == v)) notifyC("String was already typed in!")(YELLOW) else matchedStr = matchedStr :+ v
			case ":l" => moveListsToC
			case check(v1, v2) => matchPairByIdx(v1.toInt, v2.toInt)
			case ":m" => fo.loadMatches
			case ":quit" => quitInvoke
			case ":q" => quitInvoke
			case ":exit" => quitInvoke
			case ":help" => helpInvoke
			case ":h" => helpInvoke
			case _ => {
				notifyC("Invalid input!")(RED)
				notifyC("Type :help or :h for a list of available commands.")
			}
		}
		true
	}
	
	/**
	 * passes the matchedReg- and matchedStr-Lists to the controller
	 */
	def moveListsToC = rec_ moveListsToV(matchedReg, matchedStr)
	
	/**
	 * in relation to the index, a corresponding value is passed to the method checkWholeExpression
	 */
	def matchPairByIdx(str: Int, reg: Int) = {
		if(str-1 < matchedStr.size && reg-1 < matchedReg.size) {
			if(checkWholeExpression(matchedReg(reg-1), matchedStr(str-1)).exists(m => m._2.size == 0)) {
				notifyC("Error")(RED)
				notifyC("The given string-regex pair doesn't match!")
				false
			}
			else {
				notifyC("Success!")(GREEN)
				notifyC("The string-regex pair has matched!")
				true
			}
		}
		else {
			notifyC("Error")(RED)
			notifyC("The given indeces doesn't exist!")
			false
		}
	}

	/**
	 * Picks the regex and string which the user has chosen
	 */
	def chooseTheRegExAndString(regexes: HashMap[Int, String], strings: HashMap[Int, String], indexs: Tuple2[Int, Int]):List[String] = {
		List(regexes.get(indexs._1).get, strings.get(indexs._2).get)
	}
	
	/**
	 * Checks one regex with a string
	 * */
	def getFirstMatched(regEx: String, toMatch: String) = {
		var toCheck = ""
		var found = ""
		toMatch.foreach(char =>{
			toCheck += char
			if(toCheck.matches(regEx)){
				found = found replace(found, toCheck)
			}	
		})
		found
	}
	
	/**
	 * Checks whole expression with whole string
	 * */
	def checkWholeExpression(regEx: String, toMatch: String) = {
		var matchedRegExes = List[(String, String)]()
		var toCheck = toMatch
		var matches = true
		var found = ""
		cutRegEx(regEx).foreach(singleRegEx => {
			if(matches == true) { //When one regex doesn't  match, there is no need for checking the remaining regexes
				found = getFirstMatched(singleRegEx, toCheck)
				if(found == "") matches = false
				toCheck = toCheck replaceFirst(found, "")
			}
			matchedRegExes = matchedRegExes :+ (singleRegEx, found)
		})
		fo.saveMatches(matchedRegExes.filter(s => s._2.size > 0))
		matchedRegExes.filter(s => s._1.size > 0) //return just tuples with regexes
	}	
	
	/**
	 * Cuts the regex into pieces and insert them into a list with super funky recursion stuff
	 * */
		def cutRegEx(regEx: String): List[String] = {
		val digit = """(\\d|\\D)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val word = """(\\w|\\W)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val space = """(\\s|\\S)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val everyThing = """(\\.|\.)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val boundery = """(\\b)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val parenthese = """(\(.*\))(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		// Everything until a number or a digit, non digit ....
		val otherWithAmount = """(\[?[\^a-zA-Z0-9@-]+\]?)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})(.*)""".r
		
		
		regEx match {
			case digit(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case word(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case space(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case everyThing(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case boundery(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case parenthese(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case otherWithAmount(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case _ => Nil
		}
	}
}