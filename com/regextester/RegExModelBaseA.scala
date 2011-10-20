package com.regextester

abstract class RegExModelBaseA {
	
	/**
	 * reference to the controller
	 */
	var rec_ : RegExController = _
	
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
}