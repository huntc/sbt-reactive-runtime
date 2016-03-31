import org.scalatest.Matchers._
import ByteConversions._

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

name := "sandbox-ports-basic"

version := "0.1.0-SNAPSHOT"

// ConductR bundle keys
BundleKeys.nrOfCpus := 1.0
BundleKeys.memory := 64.MiB
BundleKeys.diskSpace := 10.MB
BundleKeys.endpoints := Map("other" -> Endpoint("http", services = Set(URI("http://:9001/other-service"))))

/**
  * Check ports after 'sandbox run' command
  */
val checkPorts = taskKey[Unit]("Check that the specified ports are exposed to docker. Debug port should not be exposed.")
checkPorts := {
  for (i <- 0 to 2) {
    val content = s"docker port cond-$i".!!
    val expectedLines = i match {
      case 0 =>
        Set(
          """9004/tcp -> 0.0.0.0:9004""",
          """9005/tcp -> 0.0.0.0:9005""",
          """9006/tcp -> 0.0.0.0:9006""",
          """9200/tcp -> 0.0.0.0:9200""",
          """9999/tcp -> 0.0.0.0:9999""",
          """1111/tcp -> 0.0.0.0:1111""",
          """2222/tcp -> 0.0.0.0:2222""",
          """9001/tcp -> 0.0.0.0:9001"""
        )

      case 1 =>
        Set(
          """9004/tcp -> 0.0.0.0:9014""",
          """9005/tcp -> 0.0.0.0:9015""",
          """9006/tcp -> 0.0.0.0:9016""",
          """9200/tcp -> 0.0.0.0:9210""",
          """9999/tcp -> 0.0.0.0:9909""",
          """1111/tcp -> 0.0.0.0:1121""",
          """2222/tcp -> 0.0.0.0:2232""",
          """9001/tcp -> 0.0.0.0:9011"""
        )

      case 2 =>
        Set(
          """9004/tcp -> 0.0.0.0:9024""",
          """9005/tcp -> 0.0.0.0:9025""",
          """9006/tcp -> 0.0.0.0:9026""",
          """9200/tcp -> 0.0.0.0:9220""",
          """9999/tcp -> 0.0.0.0:9919""",
          """1111/tcp -> 0.0.0.0:1131""",
          """2222/tcp -> 0.0.0.0:2242""",
          """9001/tcp -> 0.0.0.0:9021"""
        )
    }

    expectedLines.foreach(line => content should include(line))
  }
}

val checkConductRIsStopped = taskKey[Unit]("")
checkConductRIsStopped := {
  """docker ps --quiet --filter name=cond""".lines_! should have size 0
}