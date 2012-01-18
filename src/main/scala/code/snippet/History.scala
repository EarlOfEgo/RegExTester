package code.snippet

import net.liftweb._
import util._
import Helpers._

object History {
	var history: Vector[String] = Vector()
	def render = "li *" #> history
}