name := "RegExTester"

version := "1.0"

seq(webSettings: _*)

resolvers ++= Seq(
	"Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "1.6.1",
	"org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
	"org.scala-lang" % "scala-swing" % "2.9.1",
  	"net.liftweb" %% "lift-webkit" % "latest.release",
	"org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
)

scalacOptions ++= Seq(
	"-unchecked",
	"-deprecation"
)
