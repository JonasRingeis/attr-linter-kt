import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.boolean

class Cli : CliktCommand() {
    val projectAlias: String by option().required().help("Project alias used in 'data-fti-xx-' attributes")

    val ignoreHidden: Boolean by option().boolean().default(true).help("Ignore all files and directories that hidden")
    val forbidDataId: Boolean by option().boolean().default(true).help("Forbid the use of the data-id attribute")

    val path: String by option().default("./").help("The path the linter is checking")


    override fun run() {
        Linter(this).lint()
    }
}

fun main(vararg args: String) = Cli().main(args.toList())
