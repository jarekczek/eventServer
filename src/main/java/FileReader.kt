import java.io.File
import java.text.SimpleDateFormat

class FileReader(val code: String, val workDir: String) {
  private fun filename() = "$workDir/$code.txt"

  fun readFile() = File(filename()).readText()

  suspend fun readChangedFile(): String {
    Fun.waitForFileChange(filename())
    return readFile()
  }
}