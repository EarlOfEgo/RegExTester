package com.regextester.view
import scala.io.Source

trait LoadAboutHTMLContent {

	/**
	 * List of Strings that includes the lines read from the "about.html"-file
	 */
	var linesFromAboutHtmlList = List[String]()
  
	/**
	 * opens the about.html file and loads its lines into the linesFromAboutHtmlList
	 */
	def loadLinesFromAboutHtml = {
		val separator = System.getProperty("file.separator")
		val aboutSource = Source.fromFile(
		    "src" + separator + "main" + separator +
		    "webapp" + separator + "about.html")
		
		val noMetaTag = """<(meta).+/>""".r
		aboutSource.getLines.foreach(line => line match {
			case "<!DOCTYPE html>" =>
			case noMetaTag(m) =>
			case _ => linesFromAboutHtmlList = linesFromAboutHtmlList :+ line + "\n"
		})
	}
}