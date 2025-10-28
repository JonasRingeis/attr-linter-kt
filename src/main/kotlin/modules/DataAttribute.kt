package modules

import Cli
import java.io.File

class DataAttribute : LinterModule {

    val allowedAttributeExceptions = arrayOf("data-autotab", "data-int", "data-pinfo", "data-sheet-title", "data-value")

    override fun check(files: ArrayList<File>, options: Cli): Array<LinterModuleResult> {
        val findings = arrayListOf<LinterModuleResult>()
        val allDataAttrRegex = Regex("data-[a-z0-9\\-]+")
        val allowedDataAttrPrefix = "data-fti-${options.projectAlias}"

        for (file in files) {
            val fileContent = file.readText()

            val allDataAttrs = allDataAttrRegex.findAll(fileContent)
            for (match in allDataAttrs) {
                if (allowedAttributeExceptions.contains(match.value)) {
                    continue
                }
                if (match.value.startsWith(allowedDataAttrPrefix)) {
                    continue
                }

                val beforeMatch = fileContent.take(match.range.first)
                val lineNumber = beforeMatch.count { it == '\n' } + 1
                val fullLine = getFullLineByMatchRange(fileContent, match)

                findings.add(LinterModuleResult(match.value, file.name, lineNumber, fullLine))
            }
        }
        return findings.toTypedArray()
    }

    private fun getFullLineByMatchRange(fileContent: String, match: MatchResult): String {
        val lineStart = fileContent.take(match.range.last).lastIndexOf('\n') + 1
        val lineEnd = fileContent.indexOf('\n', lineStart)
        return fileContent.substring(lineStart, lineEnd).trim()
    }
}