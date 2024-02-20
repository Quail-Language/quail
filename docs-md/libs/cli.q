#?toc-entry getConsoleArgString
#?toc-entry parseConsoleArgs
#?toc-entry setPrefix
#?toc-entry setUnknownCommandMessage
#?toc-entry runApp
#?toc-entry stopApp
#?toc-html <h4><br>Decorators</h4>
#?toc-entry command
#?toc-entry tick

#?html <h2><code>lang/cli</code></h2>
#?html <hr>
#? This library allows to create simple console applications

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
    #? "flags"=dict of bool flags, "args"=list of regular arguments
}

void setPrefix(string prefix) {
    #? Sets prefix in console
    #? E.g. > [Here user can write] or command> [Here user can write]
}

void setUnknownCommandMessage(string prefix) {
    #? Sets message to display when unknown command executed
}

void runApp() {
    #? Start the main app loop
}

void stopApp() {
    #? Stop the main app loop
}

function command(f, string commandName) {
    #?badge-yellow Decorator
    #? Registers provided function as a command listener to specific console command
}

function tick(f) {
    #?badge-yellow Decorator
    #? Registers provided function as a ticking functions
}