package com.regextester.test

import scala.collection.mutable.HashMap

import org.specs2.mutable.Specification

import com.regextester.controller.RegExController
import com.regextester.model.RegExModelBase

class RegExModelBaseSpecTest extends Specification {

	val controller = new RegExController
	val model = new RegExModelBase(controller)

	"A RegExController" should {
		"not be Null" in {
			controller must not beNull
		}
	}

	
	"A RegExModelBase" should {
		"not be Null" in {
			model must not beNull
		}
	}
	/*
	 * For testing the function cutRegEx
	 */
	"The mechanism for cuttin a regular expression" should {

		"identify all metacharacters with quantifiers" in {
			val a = List("\\w*", "\\W?", "\\s+", "\\S*", "\\d?", "\\D+")
			model cutRegEx ("\\w*\\W?\\s+\\S*\\d?\\D+") must beEqualTo(a)
		}

		"identify text in a regular expression" in {
			val a = List("\\w*", "Hello?", "World+")
			model cutRegEx ("\\w*Hello?World+") must beEqualTo(a)
		}

		"identify amount quantifiers" in {
			val a = List("\\w{0,1}", "\\w{1}", "\\w{1,}")
			model cutRegEx ("\\w{0,1}\\w{1}\\w{1,}") must beEqualTo(a)
		}

		"identify different quantifiers in a regular expression" in {
			val a = List("Hi?", "My*", "Name+", "iS{1}")
			model cutRegEx ("Hi?My*Name+iS{1}") must beEqualTo(a)
		}

		"identify square brackets in a regular expression" in {
			val a = List("[a-z]?", "\\w*", "[Hallo]{1}")
			model cutRegEx ("[a-z]?\\w*[Hallo]{1}") must beEqualTo(a)
		}

		"identify parentheses in a regular expression" in {
			val a = List("(a|b)?", "\\w*", "(Hallo){1}")
			model cutRegEx ("(a|b)?\\w*(Hallo){1}") must beEqualTo(a)
		}

		"identify the except sign in regex" in {
			model cutRegEx ("[^a]?") must beEqualTo(List("[^a]?"))
			model cutRegEx ("[^a-z]*") must beEqualTo(List("[^a-z]*"))
			model cutRegEx ("[^a^z]{0,99}") must beEqualTo(List("[^a^z]{0,99}"))
		}
	}

	/*
	 * For testing the function getFirstMatched
	 */
	"The mechanism for getting the first match" should {

		"succeed with all identifiers and quantifiers" in {
			model getFirstMatched ("\\w*", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched ("\\W?", "-Hallo") must beEqualTo("-")
			model getFirstMatched ("\\s+", " Hallo") must beEqualTo(" ")
			model getFirstMatched ("\\S{1}", "Hallo") must beEqualTo("H")
			model getFirstMatched ("\\d{2,4}", "12Hallo") must beEqualTo("12")
			model getFirstMatched ("\\D{1,}", "Hallo123") must beEqualTo("Hallo")
		}

		"succeed with a regular expression using no identifiers" in {
			model getFirstMatched ("H*", "Hallo") must beEqualTo("H")
			model getFirstMatched ("My{1}", "MyName") must beEqualTo("My")
			model getFirstMatched ("My?", "MyMyName") must beEqualTo("My")
		}

		"succeed with using square brackets" in {
			model getFirstMatched ("[H]*", "Hallo") must beEqualTo("H")
			model getFirstMatched ("[a-z]*", "hallo") must beEqualTo("hallo")
			model getFirstMatched ("[0-9h]*", "123hallo") must beEqualTo("123h")
		}

		"succeed using the except sign in regex" in {
			model getFirstMatched ("[^a]?", "Hallo") must beEqualTo("H")
			model getFirstMatched ("[^a-z]*", "123Hallo") must beEqualTo("123H")
			model getFirstMatched ("[^a^z]*", "Hallo") must beEqualTo("H")
		}

		"succeed using parentheses in regex" in {
			model getFirstMatched ("(a|b)?", "allo") must beEqualTo("a")
			model getFirstMatched ("(\\w|\\d)*", "Hallo") must beEqualTo("Hallo")
			model getFirstMatched ("(H|a|l)*", "Hallo") must beEqualTo("Hall")
		}
	}
	/*
	 * For testing the function checkWholeExpression
	 */
	"The mechanism for checking a whole regular expression" should {

		"succeed with all identifiers and quantifiers" in {
			val a = List(("\\w{1}", "H"),
				("\\W+", "-"),
				("\\S{0,1}", "a"),
				("\\s{1}", " "),
				("\\D*", "llo"),
				("\\d?", "1"))
			model checkWholeExpression ("\\w{1}\\W+\\S{0,1}\\s{1}\\D*\\d?", "H-a llo1") must beEqualTo(a)
		}

		"succeed with square brackets and parentheses" in {
			val a = List(("(a|b)?", "a"),
				("[^a^c]{1}", "b"),
				("[c-f]*", "cdef"))
			model checkWholeExpression ("(a|b)?[^a^c]{1}[c-f]*", "abcdef") must beEqualTo(a)
		}

		"identify an email address" in {
			val a = List(("[a-zA-Z-]+", "my-Awesome"),
				("@{1}", "@"),
				("[a-zA-Z0-9-]+", "email-Adr3ss"),
				("\\.{1}", "."),
				("[a-z]{2,4}", "com"))
			model checkWholeExpression ("[a-zA-Z-]+@{1}[a-zA-Z0-9-]+\\.{1}[a-z]{2,4}", "my-Awesome@email-Adr3ss.com") must beEqualTo(a)
		}

		"identify if an expression is wrong" in {
			val a = List(("\\w{3}", "Reg"),
				("Ex{1}", "Ex"),
				("[^a^b^c]{6}", "Tester"),
				("\\s?", " "),
				("ROCKS{0,100}", ""))
			model checkWholeExpression ("\\w{3}Ex{1}[^a^b^c]{6}\\s?ROCKS{0,100}", "RegExTester SUCKS") must beEqualTo(a)
		}

		"succeed with a very obfuscating regular expression" in {
			val a = List(("\\w{1}", "H"),
				("1+", "1"),
				("\\s?", " "),
				(".{8}", "I'm th3 "),
				("\\d?", "4"),
				("\\w{0,6}", "m4zing"),
				("\\s?", " "),
				("R+", "R"),
				("e+", "e"),
				("g+", "g"),
				("\\S{2}", "3x"),
				("Tester{1}", "Tester"))
			model checkWholeExpression ("""\w{1}1+\s?.{8}\d?\w{0,6}\s?R+e+g+\S{2}Tester{1}""", "H1 I'm th3 4m4zing Reg3xTester") must beEqualTo(a)
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
	}

	/*
	 * For testing the function matchPairByIdx
	 */
	"The function matchPairByIdx" should {
		
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

