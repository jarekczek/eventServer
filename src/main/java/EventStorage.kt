import java.io.File
import kotlinx.coroutines.experimental.delay
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class EventStorage(val code: String, val workDir: String) {
  val eventsDataDir = workDir
  val dateFmt = SimpleDateFormat("yyMMdd")

  private fun filenameForDate(day: Date) = "$eventsDataDir/${code}_" + dateFmt.format(day) + ".events"


  fun write(data: String) {
    val fileName = filenameForDate(Calendar.getInstance().time)
    val f = File(fileName)
    f.appendText(data + "\n\n", Charset.forName("UTF-8"))
  }

  fun readLast(): String? {
    val today =  Calendar.getInstance()!!
    var lastEvent = readLastForDate(today.time)
    if (lastEvent == null) {
      val yesterday = today.clone() as Calendar
      yesterday.add(Calendar.DAY_OF_MONTH, -1)
      lastEvent = readLastForDate(yesterday.time);
    }
    return lastEvent;
  }

  private fun readLastForDate(day: Date): String? {
    val file = File(filenameForDate(day))
    if (!file.exists())
      return null
    var event: String? = null
    file.forEachLine(Charset.forName("UTF-8")) { line ->
      if (line.isNotEmpty())
        event = line
    }
    return event
  }

  suspend fun waitForAnyEvent() {
    var lastSize  = -99L
    while(true) {
      val file = File(filenameForDate(Calendar.getInstance().time))
      val curSize = file.length()
      if (lastSize != -99L && curSize != 0L && curSize != lastSize)
        break
      lastSize = curSize
      delay(50)
    }
  }

  suspend fun readNew(): String {
    waitForAnyEvent()
    return readLast()!!
  }

}
