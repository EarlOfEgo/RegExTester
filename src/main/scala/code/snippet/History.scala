package code.snippet

import net.liftweb._
import util._
import Helpers._

object History {
	var history = Vector[String]()
	def render = "li *" #> history
}