package com.regextester.view

trait SystemLookAndFeel {
	import javax.swing.UIManager._
	try setLookAndFeel(getSystemLookAndFeelClassName)
	catch { case _ => setLookAndFeel(getCrossPlatformLookAndFeelClassName) }
}