//name := "template-scala-sbt"
//organization := "dev.vgerasimov"
//version := "0.1.0"
//scalaVersion := "3.0.0-RC1"
//crossScalaVersions ++= Seq("2.13.4", "3.0.0-M3")

idePackagePrefix := Some("dev.vgerasimov.example")

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

//libraryDependencies += "org.scalatra.scalate" %% "scalate-core" % "1.9.6"
libraryDependencies += "com.hubspot.jinjava" % "jinjava" % "2.5.6"

//scalacOptions ++= { if (isDotty.value) Seq("-source:3.0-migration") else Nil }

lazy val root = project
  .in(file("."))
  .settings(
    name := "template-scala-sbt",
    organization := "dev.vgerasimov",
    description := "Example sbt project that compiles using Scala 3",
    version := "0.1.0",

    scalaVersion := "3.0.0-RC1",

    useScala3doc := true,
  )
