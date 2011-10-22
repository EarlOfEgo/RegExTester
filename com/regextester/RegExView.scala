package com.regextester

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
	def update
	
	/**
	 * setRun method stub
	 */
	def setRun(b : Boolean)
}