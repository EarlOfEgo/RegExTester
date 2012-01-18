//package code
//package comet
//
//import net.liftweb._
//import http._
//import util._
//import Helpers._
//import net.liftweb.http.js.JsCmds.SetValById
//
//class RegExTester extends CometActor with CometListener {
//
//	private var regExes = ""
//	
//	def registerWith = RegExTesterServer
//	
//	override def lowPriority = {
//		case v:String => regExes = v; reRender()
//  }
//	
//	def render = "li *" #> regExes & ClearClearable
////	def render = {
////		SetValById("result_output","hallo")
////	}
//}