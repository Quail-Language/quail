package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.expression.AssignNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class LiteralFunction extends Node {

    public static class Argument {

        public static final int POSITIONAL = 0;
        public static final int POSITIONAL_CONSUMER = 1;
        public static final int KEYWORD_CONSUMER = 2;

        public String name;
        public int type;
        public Node defaultValue;
        public int[] modifiers;

        public Argument(String name, int type, Node defaultValue, int[] modifiers) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.modifiers = modifiers;
        }

        public Argument(String name) {
            this(name, POSITIONAL, new LiteralNull(Token.UNDEFINED), new int[0]);
        }

        public Argument(String name, Node defaultValue) {
            this(name, POSITIONAL, defaultValue, new int[0]);
        }

        public Argument(String name, int type) {
            this(name, type, new LiteralNull(Token.UNDEFINED), new int[0]);
        }

        public static Argument fromVariable(VariableNode node) {
            return new Argument(
                    node.name,
                    node.isKwargConsumer?
                        KEYWORD_CONSUMER :
                    node.isArgConsumer?
                        POSITIONAL_CONSUMER :
                    POSITIONAL,
                    new LiteralNull(Token.UNDEFINED),
                    node.modifiers
            );
        }

        public static Argument fromAssignment(AssignNode node) {
            VariableNode var = ((VariableNode) node.variable);
            return new Argument(
                    var.name,
                    var.isKwargConsumer?
                            KEYWORD_CONSUMER :
                            var.isArgConsumer?
                                    POSITIONAL_CONSUMER :
                                    POSITIONAL,
                    node.value,
                    var.modifiers
            );
        }
    }

    public String name;
    public List<Argument> args;
    public Node code;
    public boolean isStatic;
    public int funcModifier = 0;

    public LiteralFunction(Token token, String name, List<Argument> args, Node code) {
        this(token, name, args, code, false);
    }

    public LiteralFunction(Token token, String name, List<Argument> args, Node code, boolean isStatic) {
        super(token);
        this.name = name;
        this.args = args;
        this.code = code;
        this.isStatic = isStatic;
    }

    @Override
    public String stringRepr() {
        return "function{" + name + " " + TextUtils.argumentListToStringRepr(args)
                + " " + code.stringRepr() + "}";
    }

}
