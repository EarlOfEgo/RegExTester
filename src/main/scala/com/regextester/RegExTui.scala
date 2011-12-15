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

import scala.collection.mutable.HashMap
import scala.Console._
import java.io._
import scala.Console

class RegExTui(rec : RegExController) extends RegExView {
	
	/**
	 * variable for manipulating the input sequence
	 */
	var run = true
	
	/**
	 * by default the output to the console isn't colored
	 */
	implicit val defaultColor = RESET
	
	/**
	 * introducing view with controller and vice versa
	 */
	init(rec)
	
	/**
	 * writes the output, if desired in a colored context
	 * */
	def writeOutput(output: String)(implicit color: String) = println(color + output + RESET)
	
	/**
	 * Reads the input
	 * */
	def readInput() = {
		
		while(run) {
			rec_ inputChange(readLine("\033[34mREGEXTESTER:\033[0m "))
		}
	}
	
	/**
	 * prints the initial text in the prompt
	 */
	def printMenu() = {
		writeOutput("******REGEXTESTER******")(BLUE)
		writeOutput("type :help for information and :quit for exiting the super RegExTester")(BLUE)
	}
	
	/**
	 * method for indicating changes on the view, represented as console output, by delegating its arguments to writeOutput
	 */
	def update(s: String)(color: String) = writeOutput(s)(color)
	
	/**
	 * method for setting the run variable
	 */
	def setRun(b : Boolean) = run = b
	
	/**
	 * creates Strings that represent the content of the matchedReg- and matchedStr-Lists and passes them to writeOutput
	 */
	def listContent(matchedReg : List[String], matchedStr : List[String]) = {
		var idxStr = 1
		var idxReg = 1
		writeOutput("The following Strings are ready to be matched:")
		if(matchedStr.size > 0) {
			matchedStr.foreach(s => {
				writeOutput(idxStr + ": " + s)
				idxStr = idxStr + 1 
			})
		}
		else writeOutput("Presently no Strings were typed in yet...")(YELLOW)
		
		writeOutput("")
		writeOutput("against the following Regular Expressions:")
		
		if(matchedReg.size > 0) {
			matchedReg.foreach(r => {
				writeOutput(idxReg + ": " + r)
				idxReg = idxReg + 1
			})
		}
		else writeOutput("Presently no Regular Expressions were typed in yet...")(YELLOW)
		writeOutput("")
	}
	
	/**
	 * prints the help screen that tells the user how to use the regextester
	 */
	def printHelpScreen = {
		writeOutput("The following commands are available:\n")
		writeOutput(":help; :h\t\t\tcommands that print this screen")
		writeOutput(":quit; :q; :exit\t\tfor exiting the regextester")
		writeOutput(":s (string)\t\t\tadding a String (string) for matching")
		writeOutput(":r (regex)\t\t\tadding a Regular Expression (regex) for matching")
		writeOutput(":l\t\t\t\tlist all available Strings and RegExes")
		writeOutput(":c (Idx(s)) (Idx(r))\t\tchoose a String and RegEx to match")
		writeOutput("\t\t\t\tby their indeces in the List (see command ':l')")
		writeOutput(":m\t\t\t\tprints the string-regex pairs to the console")
		writeOutput("\t\t\t\tthat have already matched from a previous run")
		writeOutput("\t\t\t\tof the Regextester\n")
	}
	
	/**
	 * prints all matched string-regex pairs that are included in the "matches.txt"-file
	 */
	def printMatches(s : String) = {
		writeOutput(s)
	}
}