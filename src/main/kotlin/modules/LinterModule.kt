package modules

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

        return if (inCi) {
            "Illegal attribute '$attribute' in file '$filename' at line $lineNumber\n$fullLine"
        } else "::error file=$filename,line=$lineNumber::Illegal attribute '$attribute' in file '$filename' at line $lineNumber.\n$fullLine"
    }
}