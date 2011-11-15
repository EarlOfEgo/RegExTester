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

class RegExModelBase(rec: RegExController) extends RegExModelBaseA{
	

	
	/**
	 * introducing model with controller and vice versa
	 */
	init(rec)

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
		var matchedRegExes = List(("",""))
		var toCheck = toMatch
		var matches = true
		var found = ""
		cutRegEx(regEx).foreach(singleRegEx => {
			if(matches == true) { //When one regex doesn't  match, there is no need for checking the remaining regexes
				found = getFirstMatched(singleRegEx, toCheck)
				if(found == "") matches = false
				toCheck = toCheck replace(found, "")
			}
			matchedRegExes = matchedRegExes :+ (singleRegEx, found)
		})
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
  
}