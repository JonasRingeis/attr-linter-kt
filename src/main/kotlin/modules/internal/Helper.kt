package modules.internal

import java.io.File
import kotlin.collections.forEach

fun getLineNumberByMatch(match: MatchResult, fileContent: String): Int {
    val beforeMatch = fileContent.take(match.range.first)
    return beforeMatch.count { it == '\n' } + 1
}

fun getFullLineByMatchRange(match: MatchResult, fileContent: String): String {
    val lineStart = fileContent.take(match.range.last).lastIndexOf('\n') + 1
    val lineEnd = fileContent.indexOf('\n', lineStart)
    return fileContent.substring(lineStart, lineEnd).trim()
}

fun getFilesWithExtensions(extensions: List<String>, allFiles: ArrayList<File>): List<File> {
    val filesWithExtension = mutableListOf<File>()

    allFiles.forEach {
        val fileExtension = it.extension

        if (extensions.contains(fileExtension)) {
            filesWithExtension.add(it)
        }
    }

    return filesWithExtension.toList()
}