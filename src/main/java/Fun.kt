import kotlinx.coroutines.experimental.delay
import java.io.File

object Fun {
  suspend fun waitForFileChange(filename: String) {
    var lastSize  = -99L
    while(true) {
      val file = File(filename)
      val curSize = file.length()
      if (lastSize != -99L && curSize != 0L && curSize != lastSize)
        break
      lastSize = curSize
      delay(50)
    }
  }

}