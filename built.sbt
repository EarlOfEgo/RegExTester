name := "RegExTester"

version := "1.0"

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "1.6.1",
	"org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
	"org.scala-lang" % "scala-swing" % "2.9.1"
)

scalacOptions ++= Seq(
	"-unchecked",
	"-deprecation"
)
