package com.regextester

import org.specs2.mutable._
import scala.collection.mutable.HashMap


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
		
		/*
		 * For testing the function cutRegEx
		 */
		"cut a regex right" in {
			val a = List("\\w*", "\\d?", "\\s+")
			model cutRegEx("\\w*\\d?\\s+") must beEqualTo (a)
		}
		
		"cut a regex right again" in {
			val a = List("\\w*", "Hello?", "World+")
			model cutRegEx("\\w*Hello?World+") must beEqualTo (a)
		}
		
		"cut a regex right with digits" in {
			val a = List("\\w{0,1}", "\\w{1}", "\\w{1,}")
			model cutRegEx("\\w{0,1}\\w{1}\\w{1,}") must beEqualTo (a)
		}
		
		"cut a regex right without any identifier" in {
			val a = List("Hi?", "My*", "Name+", "iS{1}")
			model cutRegEx("Hi?My*Name+iS{1}") must beEqualTo (a)
		}
		
		"cut a regex right with brackets" in {
			val a = List("[a-z]?", "\\w*", "[Hallo]{1}")
			model cutRegEx("[a-z]?\\w*[Hallo]{1}") must beEqualTo (a)
		}
		
		
		/*
		 * For testing the function getFirstMatched
		 */
		"check one regex with a string right" in {
			model getFirstMatched("\\w*", "Hallo") must beEqualTo ("Hallo")
			model getFirstMatched("\\w?", "Hallo") must beEqualTo("H")
			model getFirstMatched("\\w+", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched("\\w{1}", "Hallo") must beEqualTo("H")
			model getFirstMatched("\\w{2,4}", "Hallo") must beEqualTo("Hall")
			model getFirstMatched("\\w{1,}", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched("\\d*", "Hallo123") must beEqualTo("")
			model getFirstMatched("\\d*","42isTheAnswer") must beEqualTo("42")
			model getFirstMatched("\\d{2}", "42isTheAnswer") must beEqualTo("42")
			model getFirstMatched("[0-4]{2}", "42isTheAnswer") must beEqualTo("42")
		}
		
		/*
		 * For testing the function checkWholeExpression
		 */
		"check a string with a whole regex" in {
			model checkWholeExpression("\\w*","Hallo") must beEqualTo(List(("\\w*", "Hallo")))
			model checkWholeExpression("\\d*\\w*","42isTheAnswer") must beEqualTo(List(("\\d*", "42"),("\\w*","isTheAnswer")))
			model checkWholeExpression("\\d+\\w{2}\\w*", "42isTheAnswer") must beEqualTo(List(("\\d+", "42"),("\\w{2}", "is"), ("\\w*", "TheAnswer")))
			model checkWholeExpression("\\D{2}\\w*", "43isTheAnswer") must beEqualTo(List(("\\D{2}",""), ("\\w*","")))
			
		}
		
		"check if the right regex and string is chosen" in {
			val regexes = new HashMap[Int, String]
			val strings = new HashMap[Int, String]
			val indexes = new Tuple2(1,2)
			regexes += 1 -> "\\w*\\d*\\s*"
			regexes += 2 -> "\\d*[a-z]?"
			strings += 1 -> "Hallo"
			strings += 2 -> "42isTheAnswer"
			model chooseTheRegExAndString(regexes, strings, indexes) must contain("\\w*\\d*\\s*", "42isTheAnswer").only
			model chooseTheRegExAndString(regexes, strings, indexes) must not contain("\\d*[a-z]?", "Hallo")
		}
	}
}

