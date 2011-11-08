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


class RegExController {
	/**
	 * reference to the model
	 */
	var remba : RegExModelBaseA = _
	
	/**
	 * list that includes all registered views
	 */
	var views : List[RegExView] = Nil
	
	/**
	 * method for registrating views
	 */
	def addView(rev : RegExView) = views = rev :: views
	
	/**
	 * method for deregistering views
	 */
	def removeView(rev : RegExView) = views = views.filter(v => v != rev)
	
	/**
	 * method that notifies every registered view in the list
	 */
	def notifyViews : Unit = {
		val it = views.iterator
		views.foreach(i => i.update)
	}
	
	/**
	 * method that redirects incoming inputs from a view to the model
	 */
	def inputChange(s : String) = remba matchString(s)
	
	/**
	 * method that invokes corresponding method on the view side
	 */
	def invokeSetRun(b : Boolean) = {
		val it = views.iterator
		views.foreach(i => i.setRun(b))
	}
}