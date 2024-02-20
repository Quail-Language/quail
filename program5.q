use "lang/cli" = cli

@cli.command("exit")
function exitCommand() {
    cli.stopApp()
}

@cli.command("help")
function helpCommand() {
    print("Help")
}

@cli.command("set")
function setCommand(args) {
    print(args)
}

cli.runApp()
