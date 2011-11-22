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

class RegExTui(rec : RegExController) extends RegExView {
	
	/**
	 * variable for manipulating the input sequence
	 */
	var run = true
	
	/**
	 * introducing view with controller and vice versa
	 */
	init(rec)
	
	/**
	 * Writes the output... yeah!!!
	 * */
	def writeOutput(output: String) = println(output)
	
	/**
	 * Reads the input
	 * */
	def readInput() = {
		
		while(run) {
			rec_ inputChange(readLine)
		}
	}
	
	/**
	 * prints the initial text in the prompt
	 */
	def printMenu() = {
		println("******REGEXTESTER******")
		println("type :help for information and :quit for exiting the super RegExTester")
	}
	
	/**
	 * method for indicating changes on the view
	 */
	def update(s: String) = writeOutput(s)
	
	/**
	 * method for setting the run variable
	 */
	def setRun(b : Boolean) = run = b
	
	/**
	 * creates Strings that represent the content of the matchedReg- and matchedStr-HashMaps and passes them to writeOutput
	 */
	def listContent(matchedReg : HashMap[Int, String], matchedStr : HashMap[Int, String]) = {
		writeOutput("The following Strings are ready to be matched:")
		if(matchedStr.size > 0) matchedStr.foreach(s => writeOutput(s._1+ ": " + s._2)) else writeOutput("Presently no Strings were typed in yet...")
		writeOutput("")
		writeOutput("against the following Regular Expressions:")
		if(matchedReg.size > 0) matchedReg.foreach(r => writeOutput(r._1+ ": " + r._2)) else writeOutput("Presently no Regular Expressions were typed in yet...")
		writeOutput("")
	}
	
	/**
	 * prints the help screen that tells the user how to use the regextester
	 */
	def printHelpScreen = {
		writeOutput("The following commands are available:\n")
		writeOutput(":help\t\t\t\tcommand that prints this screen")
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