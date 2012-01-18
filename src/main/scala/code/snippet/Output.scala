package code.snippet

import net.liftweb._
import util._
import Helpers._

object Output {
	var result = ""
	var win = true
	def render = {
		if(win)"#output" #> result & "td [class]" #> "winheadline"
		else "#output" #> result & "td [class]" #> "failheadline"
	}
}