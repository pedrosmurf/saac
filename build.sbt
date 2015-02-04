name := """saac"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

resolvers ++= Seq(
  "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "fwbrasil.net" at "http://fwbrasil.net/maven/"
)

libraryDependencies ++= Seq(
	"org.postgresql"      %   "postgresql"          % "9.3-1101-jdbc41",
	"com.typesafe.slick" %% "slick" % "2.1.0"
)
