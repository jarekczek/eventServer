import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.withContext
import kotlinx.html.body
import kotlinx.html.p

class EventsModule(val workDir: String) {
  val coroutinesCtx = newFixedThreadPoolContext(1, "jctx")

  fun getModuleFunction(): (Application.() -> Unit) {
    return {
      routing {
        get("/events/read") {
          val code = call.parameters["code"]
          val last = call.parameters["last"]
          if (code?.matches(Regex("^[0-9a-z_]+$")) != true)
            throw RuntimeException("code must consist of letters, digits and underscore only.")
          val stor = EventStorage(code, workDir)
          withContext(coroutinesCtx) {
            val resp = if (last == null) {
              stor.readNew()
            } else
              stor.readLast()
            call.respondText("" + resp)
          }
        }

        get("/events/write") {
          val code = call.parameters["code"]
          val data = call.parameters["data"]
          if (code?.matches(Regex("^[0-9a-z_]+$")) != true)
            throw RuntimeException("code must consist of letters, digits and underscore only.")
          val stor = EventStorage(code, workDir)
          stor.write(data!!)
          call.respondHtml {
            body {
              p { text("code: $code") }
            }
          }
        }
      }
    }
  }
}