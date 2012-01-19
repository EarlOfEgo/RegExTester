package com.regextester.test

import org.specs2.mutable.Specification

import com.regextester.controller.RegExController
import com.regextester.model.RegExModelBase
import com.regextester.view.RegExTui

class RegExControllerSpecTest extends Specification {
	"RegExController" should {
		val c = new RegExController
		val base = new RegExModelBase(c)

		"be referenced in RegExModelBase ..." in {

			"... and shouldn't be null" in {
				base.controller mustEqual c
			}

			"... and has a reference to the RegExModelBase" in {
				c.remb mustEqual base
			}
		}

		//		"be referenced in RegExTui ..." in {
		//			val tui = new RegExTui(base)
		//
		//			"... and shouldn't be null" in {
		//				tui.rec_ mustEqual c
		//			}
		//
		//			"... and has a reference to the RegExTui" in {
		//				c.views.exists(t => t == tui) must beTrue
		//			}

		//			" ... and is able to deal with another RegExTui by ... " in {
		//				val newTui = new RegExTui(base)
		//
		//				"... adding it to its internal view list ..." in {
		//					c.views.contains(newTui) must beTrue
		//				}
		//
		//				"... and removing it from this list again" in {
		//					c removeView newTui
		//					c.views.contains(newTui) must beFalse
		//				}
		//			}
		//		}
	}
}