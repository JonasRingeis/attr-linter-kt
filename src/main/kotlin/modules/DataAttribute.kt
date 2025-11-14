package modules

import Cli
import modules.internal.LinterModule
import modules.internal.LinterModuleResult
import modules.internal.getFullLineByMatchRange
import modules.internal.getLineNumberByMatch
import java.io.File

class DataAttribute : LinterModule {

    val allowedAttributeExceptions = arrayOf("data-autotab", "data-int", "data-pinfo", "data-sheet-title", "data-value", "data-qa")

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
                if (match.value == "data-id" && !options.forbidDataId) {
                    continue
                }
                if (match.value.startsWith(allowedDataAttrPrefix)) {
                    continue
                }

                val lineNumber = getLineNumberByMatch(match, fileContent)
                val fullLine = getFullLineByMatchRange(match, fileContent)

                findings.add(LinterModuleResult(match.value, file.name, lineNumber, fullLine))
            }
        }
        return findings.toTypedArray()
    }

}