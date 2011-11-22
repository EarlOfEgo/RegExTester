package com.regextester

import scala.collection.mutable.HashMap

abstract class RegExView {
	
	/**
	 * reference to the controller
	 */
	var rec_ : RegExController = _
	
	/**
	 *  method for introducing view with controller and vice versa
	 */
	def init(rec : RegExController) = {
		rec_ = rec
		rec addView(this)
	}
	
	/**
	 * update method stub
	 */
	def update(s: String)
	
	/**
	 * setRun method stub
	 */
	def setRun(b : Boolean)
	
	/**
	 * listContent method stub
	 */
	def listContent(matchedReg : HashMap[Int, String], matchedStr : HashMap[Int, String])
	
	/**
	 * printHelpScreen method stub
	 */
	def printHelpScreen
		
	/**
	 * printMatches method stub
	 */
	def printMatches(s : String)
}