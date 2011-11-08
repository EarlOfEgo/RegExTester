package scala.test

import org.specs2.mutable._
import com.regextester.RegExController
import com.regextester.RegExModelBase

//import main.scala.com.regextester.RegExModelBase

class RegExModelBaseSpecTest extends Specification {
	val controller = new RegExController
	
	"A RegExController" should {
		"not be Null" in {
			controller must not beNull
		}
	}
	
	val model = new RegExModelBase(controller)
	"A RegExModelBase" should {
		"be not Null" in {
			model must not beNull
		}
		
		"cuts a regex right" in {
			val a = List("\\w*", "\\d?", "\\s+")
			model.cutRegEx("\\w*\\d?\\s+") must beEqualTo (a)
		}
		
		"cuts a regex right again" in {
			val a = List("\\w*", "Hello?", "World+")
			model.cutRegEx("\\w*Hello?World+") must beEqualTo (a)
		}
		
		"cuts a regex with digits" in {
			val a = List("\\w{0,1}", "\\w{1}", "\\w{1,}")
			model.cutRegEx("\\w{0,1}\\w{1}\\w{1,}") must beEqualTo (a)
		}
		
		"cuts a regex without any identifier" in {
			val a = List("Hi?", "My*", "Name+", "iS{1}")
			model.cutRegEx("Hi?My*Name+iS{1}") must beEqualTo (a)
		}
	}
}
