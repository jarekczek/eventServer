import io.ktor.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
  val dynamicPort = try {
    Integer.parseInt(System.getenv("PORT"))
  } catch(e: Exception) {
    8080
  }

  val env = applicationEngineEnvironment {
    connector {
      port = dynamicPort
    }
    modules.add(EventsModule(System.getenv("WORK_DIR")).getModuleFunction())
  }

  val server = embeddedServer(Netty, env)
  println("starting server on port $dynamicPort")
  server.start(wait = false)
  println("server started")
  try {
    Runtime.getRuntime().exec("c:\\program_files\\espeak\\command_line\\espeak.exe -s 300 server")
  } catch (e: Exception) {}
  //println("stoping server")
  //server.stop(1, 1, TimeUnit.SECONDS)
  //println("server stoped")
}

