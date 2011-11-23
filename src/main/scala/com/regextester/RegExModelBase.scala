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
import scala.util.matching.Regex
import scala.collection.mutable.HashMap
import java.io._
import scala.io.Source
import scala.io.BufferedSource

class RegExModelBase(rec: RegExController) {
	
	/**
	 * reference to the controller
	 */
	var rec_ : RegExController = _
	
	/**
	 * Lists that contain the Regular Expressions and Strings typed in in the prompt
	 */
	var matchedReg : List[String] = Nil
	var matchedStr : List[String] = Nil
	
	/**
	 * List of Tuple2[String, String]-elements that holds the regex-string pairs from the "matches.txt"-file
	 */
	var linesFromMatchesTxtList = List(("",""))
	
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
	 * invoke method notifyViews on tmatchedPairs = he controller
	 */
	def notifyC(s: String) : Unit = rec_ notifyViews(s)
	
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
			case reg(v) => if(matchedReg.exists(r => r == v)) notifyC("RegEx was already typed in!") else matchedReg = matchedReg :+ v
			case str(v) => if(matchedStr.exists(s => s == v)) notifyC("String was already typed in!") else matchedStr = matchedStr :+ v
			case ":l" => moveListsToC
			case check(v1, v2) => matchPairByIdx(v1.toInt, v2.toInt)
			case ":m" => loadMatches
			case ":quit" => quitInvoke
			case ":q" => quitInvoke
			case ":exit" => quitInvoke
			case ":help" => helpInvoke
			case ":h" => helpInvoke
			case _ => notifyC("Invalid input! Type :help or :h for a list of available commands.")
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
		if(checkWholeExpression(matchedReg(reg-1), matchedStr(str-1)).exists(m => m._2.size == 0)) {
			notifyC("Sorry, the given string-regex pair doesn't match!")
			false
		}
		else {
			notifyC("Success! The string-regex pair has matched!")
			true
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
		saveMatches(matchedRegExes.filter(s => s._2.size > 0))
		matchedRegExes.filter(s => s._1.size > 0) //return just tuples with regexes
	}	
	
	/**
	 * Cuts the regex into pieces and insert them into a list with super funky recursion stuff
	 * */
	def cutRegEx(regEx: String): List[String] = {
		val digit = """(\\d)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val nonDigit = """(\\D)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val word = """(\\w)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val nonWord = """(\\W)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val space = """(\\s)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val nonSpace = """(\\S)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		val everyThing = """(\.)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})?(.*)""".r
		// Everything until a number or a digit, non digit ....
		val otherWithAmount = """(\[?[a-zA-Z0-9-]+\]?)(\*|\?|\+|\{\d+\}|\{\d+,\d*\})(.*)""".r
		
		
		regEx match {
			case digit(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case nonDigit(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case word(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case nonWord(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case space(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case nonSpace(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case everyThing(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case otherWithAmount(v1, v2, v3) => v1 + v2 :: cutRegEx(v3)
			case _ => Nil
		}
	}
	
	/**
	 * saves all matched string-regex pairs in a external file
	 */
	def saveMatches(matches : List[(String, String)]) = {
		val out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream("matches.txt", true)))
		try {
			loadLinesFromMatchesTxt
			matches.foreach(m => {
				if(!(linesFromMatchesTxtList.exists(pair => pair == m))){
					out.write(m._1 + " -> " + m._2 + "\n")
				}
			})
		} finally {
			out close
		}
	}
	
	/**
	 * opens the file that includes the matched string-regex pairs
	 */
	def loadMatches = {
		val f = new File("matches.txt")
		var s : BufferedSource = null
		if(!f.exists) {
			f.createNewFile
			notifyC("Sorry, no previous string-regex pairs found!")
		}
		else {
			s = Source.fromFile("matches.txt")
			if(s.isEmpty) notifyC("Sorry, no previous string-regex pairs found!")
			else {
				notifyC("The following string-regex pairs have matched already:")
				s.getLines.foreach(line => if(line != "") sendMatchesFromFileToC(line))
			}
		}
	}
	
	/**
	 * opens the "matches.txt"-file and loads the single lines, separated into pairs of regexes and Strings, into the linesFromMatchesTxtList
	 */
	def loadLinesFromMatchesTxt = {
		val secondSource = Source.fromFile("matches.txt")
		val separateLine = """(.+)\s->\s(.+)""".r
		secondSource.getLines().foreach(line => line match {
			case separateLine(r, s) => linesFromMatchesTxtList = linesFromMatchesTxtList :+ (r, s)
			case _ => 
		})
	}
}