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

class RegExTui {
	
	/**
	 * Writes the output... yeah!!!
	 * */
	def writeOutput(output: String) = println(output)
	
	/**
	 * Reads the input
	 * */
	def readInput() = {
		var run = true
		
		while(run) {
			val reg = ":r (.*)".r
			val str = ":s (.*)".r
			var input = readLine
			input match {
				case ":quit" => run = false
				case reg(v) =>
				case str(v) => 
				case ":a" => 
				case ":s" => 
				case _ => 
			}
			
		}
	}
	
	def printMenu() = {
		println("******REGEXTESTER******")
		println("type :help for information and :quit for exiting the super RegExTesters")
	}
	
}