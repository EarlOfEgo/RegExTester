package test.scala

import org.specs2.mutable._
import com.regextester.RegExController
import com.regextester.RegExModelBase
import com.regextester.RegExTui

class RegExControllerSpecTest extends Specification {
	"RegExController" should {
		var c = new RegExController
		
		"be referenced in RegExModelBase ..." in {
			var base = new RegExModelBase(c)
		
			"... and shouldn't be null" in {
				base.rec_ mustEqual c
			}
			
			"... and have a reference to the RegExModelBase" in {
				c.remba mustEqual base
			}
		}
		
		"be referenced in RegExTui ..." in {
			var tui = new RegExTui(c)
			
			"... and shouldn't be null" in {
				tui.rec_ mustEqual c
			}
			
			"... and have a reference to the RegExTui" in {
				c.views.exists(t => t == tui)
			}
		
			"... and be able to add RegExTui to its internal view list" in {
				c addView tui
				c.views.exists(t => t == tui) must beTrue
			}
		
			"... and be able to remove RegExTui to its internal view list" in {
				c removeView tui
				c.views.exists(t => t == tui) must beFalse
			}
		}
	}
}