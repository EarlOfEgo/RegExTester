package com.regextester.view

import javax.swing.UIManager

trait SystemLookAndFeel {
	try {
  		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  	}
}