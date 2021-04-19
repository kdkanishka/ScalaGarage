name := "ScalaGarage"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

resolvers += Resolver.bintrayRepo("loicdescotte", "Hamsters")
libraryDependencies += "io.github.scala-hamsters" %% "monad-transformers" % "1.0.0"
