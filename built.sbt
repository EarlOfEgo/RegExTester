name := "RegExTester"

version := "1.0"

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "1.6.1",
	"org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"
)

scalacOptions ++= Seq(
	"-unchecked",
	"-deprecation"
)
