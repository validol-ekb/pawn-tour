name := "pawn-tour"

version := "0.1"

scalaVersion := "2.12.8"

mainClass in (Compile, run) := Some("ekb.validol.pawn.tour.Boot")

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.mockito"   %  "mockito-core" % "1.9.5" % "test"
)