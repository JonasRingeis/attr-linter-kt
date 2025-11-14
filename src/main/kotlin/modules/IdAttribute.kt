package modules

import Cli
import modules.internal.LinterModule
import modules.internal.LinterModuleResult
import modules.internal.getFilesWithExtensions
import modules.internal.getFullLineByMatchRange
import modules.internal.getLineNumberByMatch
import java.io.File

class IdAttribute : LinterModule {

    val idRegex = Regex(" id=\"[A-z0-9-_]+\"")
    val allowedTagsWithId = listOf("input")

    override fun check(
        files: ArrayList<File>,
        options: Cli
    ): Array<LinterModuleResult> {
        if (!options.forbidId) {
            return emptyArray()
        }

        val templateFiles = getFilesWithExtensions(listOf("html", "twig"), files)

        return templateFiles.flatMap { file ->
            val fileContent = file.readText()
            idRegex.findAll(fileContent).mapNotNull { match -> checkMatch(match, fileContent, file) }
        }.toTypedArray()
    }

    private fun checkMatch(
        match: MatchResult,
        fileContent: String,
        file: File
    ): LinterModuleResult? {
        val tag = getTagByMatch(match, fileContent)

        if (allowedTagsWithId.contains(tag)) {
            return null
        }

        val lineNumber = getLineNumberByMatch(match, fileContent)
        val fullLine = getFullLineByMatchRange(match, fileContent)

        return LinterModuleResult("id", file.name, lineNumber, fullLine)
    }


    private fun getTagByMatch(match: MatchResult, fileContent: String): String {
        val tagStartIndex = fileContent.take(match.range.last).lastIndexOf("<")
        val tagLine = fileContent.substring(tagStartIndex + 1, match.range.last)
        val tag = tagLine.take(tagLine.indexOf(" "))
        return tag
    }
}