use "lang/cli" = cli

argString = cli.getConsoleArgString()
parameters = cli.parseConsoleArgs(argString)
parameters.flags
parameters.kwargs
parameters.args

class ConsoleApp {
    @Decorator @Linked function command(this, f, string commandName) {
        #? Registers provided function as a command listener to specific console command
    }
}

@cli.command("help")
function helpCommand(args...) {

}

@cli.command("set")


string getConsoleArgString() {
    #? Returns string of all arguments that were passed to Quail script
    #? E.g. java -jar qre.jar main.q abc def --t="2" -Dfd
    #? <code>abc def --t="2" -D</code> will be returned
}

dict parseConsoleArgs(string argString) {
    #? Parses console arguments (supports bool, num and string types of args):
    #? Sequences starting with -- will be treated like keyword arguments.
    #? Sequences starting with - will be treated like false bool flag sequences
    #? Sequences starting with + will be treated like true bool flag sequences
    #? Any other sequence will be treated as a regular argument (string)
    #? Resulting dict will contain: "kwargs"=dict of keyword arguments,
    #? "flags"=dict of bool flags, "args"=list<string> of regular arguments
}

