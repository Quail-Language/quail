use "lang/fs" = fs
use "lang/cli" = cli

cwd = fs.absolutePath("/")
cli.setPrefix(cwd + ">")

@cli.command("exit")
function exitCommand() {
    cli.stopApp()
}

@cli.command("ls")
function lsCommand() {
    print("\n".inBetweenOf(fs.filesIn(cwd)))
}

cli.runApp()
