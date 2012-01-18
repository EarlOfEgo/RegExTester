package com.regextester.view

import javax.swing.UIManager

trait GTKLookAndFeel {
	try {
  		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
  	}
}