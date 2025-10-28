import com.github.ajalt.clikt.core.PrintMessage
import modules.DataAttribute
import modules.LinterModule
import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class Linter(
    val options: Cli,
    val linterModules: Array<LinterModule> = arrayOf(DataAttribute()),
) {

    fun lint() {
        val allFiles = getAllFiles()
        runModules(allFiles)
    }

    private fun runModules(allFiles: ArrayList<File>) {
        var anyMatches = false
        for (module in linterModules) {
            val moduleMatches = module.check(allFiles, options)
            if (moduleMatches.isNotEmpty()) {
                anyMatches = true
            }

            PrintMessage(moduleMatches.joinToString("\n\n"), 1, true)
            println(moduleMatches.joinToString("\n\n"))
        }
        exitProcess(if (anyMatches) 1 else 0)
    }

    fun getAllFiles(): ArrayList<File> {
        val dir = File(options.path)
        val fileFilter = FileFilter({ file ->
            (options.ignoreHidden && !file.isHidden)
            && (file.isDirectory || file.name.endsWith(".twig"))
        })
        return getAllFilesRec(dir, fileFilter)
    }

    fun getAllFilesRec(dir: File, filter: FileFilter): ArrayList<File> {
        if (!dir.exists()) {
            throw FileNotFoundException("Directory '${dir.name}' does not exist")
        }

        val dirChildren = dir.listFiles(filter) ?: arrayOf<File>()
        val files = arrayListOf<File>()
        for (file in dirChildren) {
            if (file.isFile) {
                files.add(file)
            } else if (file.isDirectory) {
                files.addAll(getAllFilesRec(file, filter))
            }
        }
        return files
    }
}

//fun getIgnoredDirectories(): List<String> {
//
//}

