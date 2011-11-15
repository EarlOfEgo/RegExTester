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
			
			"... and has a reference to the RegExModelBase" in {
				c.remba mustEqual base
			}
		}
		
		"be referenced in RegExTui ..." in {
			var tui = new RegExTui(c)
			
			"... and shouldn't be null" in {
				tui.rec_ mustEqual c
			}
			
			"... and has a reference to the RegExTui" in {
				c.views.exists(t => t == tui) must beTrue
			}
			
			" ... and is able to deal with another RegExTui by ... " in {
				var newTui = new RegExTui(c)
				
				"... adding it to its internal view list ..." in {
					c addView newTui
					c.views.exists(t => t == newTui) must beTrue
				}
		
				"... and removing it from this list again" in {
					c removeView newTui
					c.views.exists(t => t == newTui) must beFalse
				}
			}
		}
	}
}