import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.response.respondWrite
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.withContext
import kotlinx.html.body
import kotlinx.html.p
import java.io.File

class StaticModule() {
  fun getModuleFunction(): (Application.() -> Unit) {
    return {
      routing {
        get("/static/{name}") {
          call.respondWrite(contentType = ContentType.Text.Html) {
            val name = call.parameters["name"]
            val istr = javaClass.getResourceAsStream("/static/$name.html")
            istr.bufferedReader().lines().forEach {
              write(it + "\n")
            }
          }
        }
      }
    }
  }
}