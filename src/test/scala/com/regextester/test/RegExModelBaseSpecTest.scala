package com.regextester.test

import scala.collection.mutable.HashMap

import org.specs2.mutable.Specification

import com.regextester.controller.RegExController
import com.regextester.model.RegExModelBase

class RegExModelBaseSpecTest extends Specification {

	val controller = new RegExController

	"A RegExController" should {
		"not be Null" in {
			controller must not beNull
		}
	}

	val model = new RegExModelBase(controller)
	"A RegExModelBase" should {
		"not be Null" in {
			model must not beNull
		}

		/*
		 * For testing the function cutRegEx
		 */
		"cut a regex right" in {
			val a = List("\\w*", "\\d?", "\\s+")
			model cutRegEx ("\\w*\\d?\\s+") must beEqualTo(a)
		}

		"cut a regex right again" in {
			val a = List("\\w*", "Hello?", "World+")
			model cutRegEx ("\\w*Hello?World+") must beEqualTo(a)
		}

		"cut a regex right with digits" in {
			val a = List("\\w{0,1}", "\\w{1}", "\\w{1,}")
			model cutRegEx ("\\w{0,1}\\w{1}\\w{1,}") must beEqualTo(a)
		}

		"cut a regex right without any identifier" in {
			val a = List("Hi?", "My*", "Name+", "iS{1}")
			model cutRegEx ("Hi?My*Name+iS{1}") must beEqualTo(a)
		}

		"cut a regex right with brackets" in {
			val a = List("[a-z]?", "\\w*", "[Hallo]{1}")
			model cutRegEx ("[a-z]?\\w*[Hallo]{1}") must beEqualTo(a)
		}

		/*
		 * For testing the function getFirstMatched
		 */
		"check one regex with a string right" in {
			model getFirstMatched ("\\w*", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched ("\\w?", "Hallo") must beEqualTo("H")
			model getFirstMatched ("\\w+", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched ("\\w{1}", "Hallo") must beEqualTo("H")
			model getFirstMatched ("\\w{2,4}", "Hallo") must beEqualTo("Hall")
			model getFirstMatched ("\\w{1,}", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched ("\\d*", "Hallo123") must beEqualTo("")
			model getFirstMatched ("\\d*", "42isTheAnswer") must beEqualTo("42")
			model getFirstMatched ("\\d{2}", "42isTheAnswer") must beEqualTo("42")
			model getFirstMatched ("[0-4]{2}", "42isTheAnswer") must beEqualTo("42")
			model getFirstMatched ("\\s{2}", "  Hello") must beEqualTo("  ")
			model getFirstMatched ("\\S{2}", "Hello") must beEqualTo("He")
		}
		
		"recognizes parentheses" in {
			model cutRegEx("(\\d*|\\w*)?\\d*") must beEqualTo(List("(\\d*|\\w*)?", "\\d*"))
		}
		
		"recognizes except sign in regex" in {
			model cutRegEx("[^a]?\\d*") must beEqualTo(List("[^a]?", "\\d*"))
			model cutRegEx("[^a-z]?\\d*") must beEqualTo(List("[^a-z]?", "\\d*"))
			model cutRegEx("\\W{1,3}[^a-z]{0,99}") must beEqualTo(List("\\W{1,3}", "[^a-z]{0,99}"))
		}

		/*
		 * For testing the function checkWholeExpression
		 */
		"check a string with a whole regex" in {
			model checkWholeExpression ("\\w*", "Hallo") must beEqualTo(List(("\\w*", "Hallo")))
			model checkWholeExpression ("\\d*\\w*", "42isTheAnswer") must beEqualTo(List(("\\d*", "42"), ("\\w*", "isTheAnswer")))
			model checkWholeExpression ("\\d+\\w{2}\\w*", "42isTheAnswer") must beEqualTo(List(("\\d+", "42"), ("\\w{2}", "is"), ("\\w*", "TheAnswer")))
			model checkWholeExpression ("\\D{2}\\w*", "43isTheAnswer") must beEqualTo(List(("\\D{2}", ""), ("\\w*", "")))

		}

		"check if the right regex and string is chosen" in {
			val regexes = new HashMap[Int, String]
			val strings = new HashMap[Int, String]
			val indexes = new Tuple2(1, 2)
			regexes += 1 -> "\\w*\\d*\\s*"
			regexes += 2 -> "\\d*[a-z]?"
			strings += 1 -> "Hallo"
			strings += 2 -> "42isTheAnswer"
			model chooseTheRegExAndString (regexes, strings, indexes) must contain("\\w*\\d*\\s*", "42isTheAnswer").only
			model chooseTheRegExAndString (regexes, strings, indexes) must not contain ("\\d*[a-z]?", "Hallo")
		}
		
				"recognizes an email address" in {
//			val a = List(
//					("[a-z]+", "rainer"), ("@{1}", "@"), ("[a-z0-9]+", "unsinn"), 
//					("\\.{1}", "."), ("[a-z]{2,4}", "de"))
//			model checkWholeExpression ("[a-z]+@{1}[a-z0-9]+\\.{1}[a-z]{2,4}", "rainer@unsinn.de") must beEqualTo(a)
		
			val a = List(("\\.{1}", "."))
			model checkWholeExpression ("\\.{1}", ".") must beEqualTo(a)

		}
				
						"checks super regex string" in {
			val a = List(("\\w{1}", "H"), 
					("1+", "1"), ("\\s?", " "), (".{8}", "I'm th3 "))
			model checkWholeExpression ("\\w{1}1+\\s?.{8}", "H1 I'm th3 ") must beEqualTo(a)
		
//			val a = List(("\\.{1}", "."), ("[a-z]{2,4}", "de"))
//			model checkWholeExpression ("\\.{1}[a-z]{2,4}", ".de") must beEqualTo(a)

		}

		/*
		 * For testing the function matchPairByIdx
		 */
		"check whether a string and a regex match by their List-indices" in {
			model.matchedStr = model.matchedStr.drop(model.matchedStr.length)
			model.matchedReg = model.matchedReg.drop(model.matchedReg.length)
			model.matchedStr = model.matchedStr :+ "Hello"
			model.matchedReg = model.matchedReg :+ "\\w*"
			model.matchPairByIdx(1, 1) must beTrue
		}

		"check whether a string and a not corresponding regex doesn't match" in {
			model.matchedReg = model.matchedReg :+ "\\d*"
			model.matchPairByIdx(1, 2) must beFalse
		}
	}
}

