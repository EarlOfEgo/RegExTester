//package code
//package comet
//
//import net.liftweb._
//import http._
//import actor._
//import com.regextester.RegExModelBase
//import com.regextester.RegExController
//import net.liftweb.http.js.JsCmds.SetValById
//
//object RegExTesterServer extends LiftActor with ListenerManager {
//	private var regExes = ""
//	private var strings = ""
//
//	def createUpdate = regExes
//	var controller = new RegExController()
//  	var model = new RegExModelBase(controller)
//	
//	def regExesVector = regExes
//	def stringsVector = strings
//
//	override def lowPriority = {
//		case string: String => strings = string; updateListeners();
//	}	
//	
//	
//	def check {
//		regExes.last
//		val result = model.checkWholeExpression(regExes, strings)
//		SetValById("result_output", result.toString())
//	}
//
//}