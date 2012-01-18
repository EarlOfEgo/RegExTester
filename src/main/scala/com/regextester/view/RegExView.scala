package com.regextester.view

import com.regextester.controller.RegExController

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
	def update(s: String)(color: String)
	
	/**
	 * setRun method stub
	 */
	def setRun(b : Boolean)
	
	/**
	 * listContent method stub
	 */
	def listContent(matchedReg : List[String], matchedStr : List[String])
	
	/**
	 * printHelpScreen method stub
	 */
	def printHelpScreen
		
	/**
	 * printMatches method stub
	 */
	def printMatches(s : String)
}