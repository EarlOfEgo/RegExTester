package com.regextester

import scala.collection.mutable.HashMap

abstract class RegExModelBaseA {
	
	/**
	 * reference to the controller
	 */
	var rec_ : RegExController = _
	
	/**
	 * Lists that contain the Regular Expressions and Strings typed in in the prompt
	 */
	val matchedReg = new HashMap[Int, String]
	val matchedStr = new HashMap[Int, String]
	
	/**
	 * for incrementing the key-values in the HashMaps
	 */
	var regMapKey = 0
	var strMapKey = 0
	
	/**
	 * method for introducing model with controller and vice versa
	 */
	def init(rec : RegExController) = {
		rec_ = rec
		rec remba = this
	}
	
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
	def notifyC : Unit = rec_ notifyViews
	
	/**
	 * match and with the input String
	 */
	def matchString(s : String) = {
		val reg = ":r (.*)".r
		val str = ":s (.*)".r
		
		s match {
			case ":quit" => rec_ invokeSetRun(false)
			case reg(v) =>
			case str(v) => 
			case ":a" => 
			case ":s" => 
			case _ => rec_ notifyViews
		}
		true
	}
	
	/**
	 * Checks whole expression with whole string
	 * */
	def checkWholeExpression(regEx: String, toMatch: String)
	
	/**
	 * passes the matchedReg- and matchedStr-HashMaps to the controller
	 */
	def moveHashMapsToC = rec_.moveHashMapsToV(matchedReg, matchedStr)
}