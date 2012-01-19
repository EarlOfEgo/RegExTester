package com.regextester.model

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

import scala.Console.RED
import scala.Console.RESET
import scala.io.BufferedSource
import scala.io.Source

import com.regextester.model._

class FileOpsOnMatchesTxt(model : RegExModelBase) {
  
  	/**
	 * by default the output to the console isn't colored
	 */
	implicit val defaultColor = RESET

	/**
	 * List of Tuple2[String, String]-elements that holds the regex-string pairs from the "matches.txt"-file
	 */
	var linesFromMatchesTxtList = List[(String, String)]()
  
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
	def loadMatches():Unit = {
		val f = new File("matches.txt")
		var s : BufferedSource = null
		if(!f.exists) {
			f.createNewFile
			model.notifyC("Error")(RED)
			model.notifyC("No previous string-regex pairs found!")
		}
		else {
			s = Source.fromFile("matches.txt")
			if(s.isEmpty) {
				model.notifyC("Error")(RED)
				model.notifyC("No previous string-regex pairs found!")
			}
			else {
				model.notifyC("The following string-regex pairs have matched already:")
				s.getLines.foreach(line => if(line != "") model.sendMatchesFromFileToC(line))
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