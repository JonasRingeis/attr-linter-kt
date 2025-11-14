package modules.internal

import Cli
import java.io.File

interface LinterModule {
    fun check(files: ArrayList<File>, options: Cli): Array<LinterModuleResult>
}

data class LinterModuleResult (
    val attribute: String,
    val filename: String,
    val lineNumber: Int,
    val fullLine: String,
) {
    override fun toString(): String {
        val inCi = System.getenv("CI")?.lowercase() == "true"

        val message = "Illegal attribute '$attribute' in file '$filename' at line $lineNumber\n$fullLine"

        return if (inCi) {
            "::error file=$filename,line=$lineNumber::$message"
        } else {
            message
        }
    }
}