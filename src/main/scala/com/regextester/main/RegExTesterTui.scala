package com.regextester.main
import com.regextester.controller.RegExController
import com.regextester.view.RegExTui
import com.regextester.model.RegExModelBase

object RegExTesterTui {
	
  def main(args: Array[String]): Unit = {
  	var controller = new RegExController()
  	var model = new RegExModelBase(controller)
  	var view = new RegExTui(model)
  }
}