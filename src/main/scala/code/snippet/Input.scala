package code.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml.NodeSeq
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.http.js.JsCmds.SetValById
import com.regextester._
import com.regextester.controller.RegExController
import com.regextester.model.RegExModelBase

object Input {
	private object regex extends RequestVar("")
	private object string extends RequestVar("")
	private object whence extends RequestVar(S.referer openOr "/")
	private val controller = new RegExController()
	val model = new RegExModelBase(controller)

	def render =
		"name=regex_in" #> SHtml.textElem(regex) &
			"name=string_in" #> SHtml.textElem(string) &
			"#check" #> SHtml.onSubmitUnit(process)

	private def process() = {
		Result.result = Vector()
		if (regex.isEmpty() || string.isEmpty())
			Output.result = "Enter a regular expression and a string"
		else {
			var output = regex + " checked with " + string + "\n"
			var result = model.checkWholeExpression(regex, string)
			var fail = result.filter(s => s._2.size == 0)
			if (!fail.isEmpty) {
				Output.win = false
				Output.result = output + " doesn't match!"
				History.history :+= output + " and doesn't match!"

				result.filter(s => s._2.size > 0).foreach(t => {
					Result.win = true
					Result.result :+= t._1 + " matched with " + t._2
				})
				fail.foreach(f => {
					Result.win = false
					Result.result :+= f._1 + " DOESN'T MATCH"
				})

			} else {
				result.foreach(t => {
					Result.win = true
					Result.result :+= t._1 + " matched with " + t._2
				})
				Output.win = true
				Output.result = output
				History.history :+= output + " and matched"
			}
		}
		S.notice("String: " + regex)
		S.redirectTo(whence)
	}
}