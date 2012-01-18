package code.snippet
import net.liftweb._
import util._
import Helpers._

object Result {
	var result: Vector[String] = Vector()
	//	def replace = "fail *" #> "win"
	var win = true
	def render = {
		"li *" #> result
//		if (win) "li *" #> result & "li [class]" #> "win"
//		else "li *" #> result & "li [class]" #> "fail"
	}

}