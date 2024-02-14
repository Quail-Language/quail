package me.tapeline.quailj.runtime.std.cli;

import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.cli.argparser.ArgLexer;
import me.tapeline.quailj.runtime.std.cli.argparser.ArgParser;
import me.tapeline.quailj.runtime.std.cli.argparser.ArgToken;
import me.tapeline.quailj.runtime.std.cli.argparser.ArgTokenType;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class CliParseConsoleArgs extends QBuiltinFunc {

    public CliParseConsoleArgs(Runtime runtime) {
        super(
                "parseConsoleArgs",
                Arrays.asList(
                        new FuncArgument(
                               "argString",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        ArgLexer lexer = new ArgLexer(args.get("argString").strValue());
        try {
            List<ArgToken> tokens = lexer.scan();
            ArgParser parser = new ArgParser(tokens);
            parser.parse();
            HashMap<String, QObject> kwargs = new HashMap<>();
            for (Map.Entry<String, Object> entry : parser.getKwargs().entrySet()) {
                if (entry.getValue() instanceof Double)
                    kwargs.put(entry.getKey(), Val((Double) entry.getValue()));
                if (entry.getValue() instanceof String)
                    kwargs.put(entry.getKey(), Val((String) entry.getValue()));
                if (entry.getValue() instanceof Boolean)
                    kwargs.put(entry.getKey(), Val((Boolean) entry.getValue()));
            }
            HashMap<String, QObject> flags = new HashMap<>();
            for (Map.Entry<String, Boolean> entry : parser.getBoolFlags().entrySet())
                flags.put(entry.getKey(), Val(entry.getValue()));
            List<QObject> arguments = parser.getArguments().stream()
                    .map(arg -> {
                        if (arg instanceof Double)
                            return Val((Double) arg);
                        if (arg instanceof String)
                            return Val((String) arg);
                        if (arg instanceof Boolean)
                            return Val((Boolean) arg);
                        return Val();
                    })
                    .collect(Collectors.toList());
            return Val(Dict.make(
                    new Pair<>("kwargs", Val(kwargs)),
                    new Pair<>("flags", Val(flags)),
                    new Pair<>("args", Val(arguments))
            ));
        } catch (LexerException e) {
            return Val(Dict.make(
                    new Pair<>("kwargs", Val(new HashMap<>())),
                    new Pair<>("flags", Val(new HashMap<>())),
                    new Pair<>("args", Val(new ArrayList<>()))
            ));
        }
    }

}
