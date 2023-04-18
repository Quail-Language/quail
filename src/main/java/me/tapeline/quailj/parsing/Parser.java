package me.tapeline.quailj.parsing;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.effects.AsyncNode;
import me.tapeline.quailj.parsing.nodes.expression.*;
import me.tapeline.quailj.parsing.nodes.generators.*;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.utils.IncompleteModifierNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.tapeline.quailj.lexing.TokenType.*;

public class Parser {

    private final List<Token> tokens;
    private final String sourceCode;
    private int pos = 0;

    public Parser(String code, List<Token> tokens) {
        this.tokens = tokens;
        this.sourceCode = code;
    }

    private void error(String message) throws ParserException {
        throw new ParserException(
                message,
                tokens.get(pos)
        );
    }

    private void error(Token token, String message) throws ParserException {
        throw new ParserException(
                message,
                token
        );
    }

    private boolean reachedEnd() {
        return pos >= tokens.size();
    }

    private boolean reachedEndAt(int pos) {
        return pos >= tokens.size();
    }

    private boolean reachedEndOffset(int offset) {
        return reachedEndAt(pos + offset);
    }

    private Token current() {
        if (reachedEnd()) return null;
        return tokens.get(pos);
    }

    private Token getNext() {
        if (reachedEndOffset(1)) return null;
        return tokens.get(pos + 1);
    }

    private Token getNext(int offset) {
        if (reachedEndOffset(offset)) return null;
        return tokens.get(pos + offset);
    }

    private Token match(TokenType type) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        if (type == current.getType()) {
            pos += increment + 1;
            return current;
        }
        return null;
    }

    private Token matchExactly(TokenType type) {
        if (reachedEnd()) return null;
        Token current = tokens.get(pos);
        if (type.equals(current.getType())) {
            pos++;
            return current;
        }
        return null;
    }

    private Token matchMultiple(TokenType... types) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        for (TokenType acceptable : types) {
            if (acceptable == current.getType()) {
                pos += increment + 1;
                return current;
            }
        }
        return null;
    }

    private Token matchSameLine(TokenType type) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        if (type == current.getType()) {
            pos += increment + 1;
            return current;
        }
        return null;
    }

    private Token consumeAny() {
        if (reachedEnd()) return null;
        return tokens.get(pos++);
    }

    private Token consumeSignificant() {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        pos += increment + 1;
        return current;
    }

    private Token toNextSignificant() {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        pos += increment;
        return current;
    }

    private boolean forseeToken(TokenType type) {
        return getNext() != null && getNext().getType() == type;
    }

    private boolean forseePattern(TokenType... pattern) {
        for (int i = 0; i < pattern.length; i++)
            if (getNext(i) == null || getNext(i).getType() != pattern[i])
                return false;
        return true;
    }

    private Token require(TokenType type) throws ParserException {
        return require(type, null);
    }

    private Token require(TokenType type, String message) throws ParserException {
        Token token = match(type);
        if (reachedEnd())
            error(message == null? "Expected " + type.toString() + " but file ended" : message);
        else if (token == null && getNext() != null)
            error(message == null? "Expected " + type.toString() +
                    " but found " + getNext().getType() : message);
        else
            error(message == null? "Expected " + type.toString() + " but found none" : message);
        return token;
    }

    private Token getPrevious() {
        return tokens.get(pos - 1);
    }

    private Token transformShortOp(Token shortOp) {
        if (shortOp.getType() == SHORT_POWER)
            return new Token(shortOp.getMod(), POWER, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_DIVIDE)
            return new Token(shortOp.getMod(), DIVIDE, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_MINUS)
            return new Token(shortOp.getMod(), MINUS, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_INTDIV)
            return new Token(shortOp.getMod(), INTDIV, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_PLUS)
            return new Token(shortOp.getMod(), PLUS, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_MODULO)
            return new Token(shortOp.getMod(), MODULO, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        if (shortOp.getType() == SHORT_MULTIPLY)
            return new Token(shortOp.getMod(), MULTIPLY, shortOp.getLexeme(),
                    shortOp.getLine(), shortOp.getCharacter(), shortOp.getLength());
        return shortOp;
    }

    private Node parseStatement() {
        return new Node(Token.UNDEFINED);
    }

    private Node parseExpression(ParsingPolicy policy) throws ParserException {
        if (match(ASYNC) != null)
            return new AsyncNode(getPrevious(), parseStatement());

        return parseAssignment(policy);
    }

    private Node parseAssignment(ParsingPolicy policy) throws ParserException {
        Node left = parseOr(policy);
        if (match(ASSIGN) != null) {
            Token assignment = getPrevious();
            if (left instanceof IndexingNode)
                return new IndexSetNode(assignment, ((IndexingNode) left).collection,
                        ((IndexingNode) left).index, parseOr(policy));
            if (left instanceof FieldReferenceNode)
                return new FieldSetNode(assignment, ((FieldReferenceNode) left).parent,
                        ((FieldReferenceNode) left).value, parseOr(policy));
            return new AssignNode(getPrevious(), left, parseOr(policy));
        }
        if (matchMultiple(SHORT_DIVIDE, SHORT_MODULO, SHORT_INTDIV,
                SHORT_POWER, SHORT_MINUS, SHORT_MULTIPLY, SHORT_PLUS) != null) {
            Token fullOp = transformShortOp(getPrevious());
            Token assignToken = fullOp.derivativeFor(ASSIGN, "=");
            Node value = parseOr(policy);
            if (left instanceof IndexingNode)
                return new IndexSetNode(assignToken, ((IndexingNode) left).collection,
                        ((IndexingNode) left).index, new BinaryOperatorNode(fullOp, left, value));
            if (left instanceof FieldReferenceNode)
                return new FieldSetNode(assignToken, ((FieldReferenceNode) left).parent,
                        ((FieldReferenceNode) left).value, new BinaryOperatorNode(fullOp, left, value));
            return new AssignNode(assignToken, left, new BinaryOperatorNode(fullOp, left, value));
        }
        return left;
    }

    private Node parseOr(ParsingPolicy policy) throws ParserException {
        Node left = parseAnd(policy);
        while (match(OR) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parseAnd(policy));
        return left;
    }

    private Node parseAnd(ParsingPolicy policy) throws ParserException {
        Node left = parseEquality(policy);
        while (match(AND) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parseEquality(policy));
        return left;
    }

    private Node parseEquality(ParsingPolicy policy) throws ParserException {
        Node left = parseComparison(policy);
        while (matchMultiple(EQUALS, NOT_EQUALS, INSTANCEOF, IN) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parseComparison(policy));
        return left;
    }

    private Node parseComparison(ParsingPolicy policy) throws ParserException {
        Node left = parseRange(policy);
        while (matchMultiple(LESS, LESS_EQUAL, GREATER, GREATER_EQUAL) != null) {
            Token token = getPrevious();
            if (match(LESS) != null)
                left = new BinaryOperatorNode(token.derivativeFor(SHIFT_LEFT, "<<"),
                        left, parseRange(policy));
            else if (match(GREATER) != null)
                left = new BinaryOperatorNode(token.derivativeFor(SHIFT_RIGHT, ">>"),
                        left, parseRange(policy));
            else
                left = new BinaryOperatorNode(getPrevious(), left, parseRange(policy));
        }
        return left;
    }

    private Node parseRange(ParsingPolicy policy) throws ParserException {
        Node left = parseTerm(policy);
        if (matchMultiple(RANGE, RANGE_INCLUDE) != null) {
            Token rangeToken = getPrevious();
            Node rangeY = parseTerm(policy);
            if (forseePattern(RANGE, EOL))
                return new RangeNode(rangeToken, left, rangeY, null);
            if (match(RANGE) != null)
                return new RangeNode(rangeToken, left, rangeY, parseTerm(policy));
            else
                return new RangeNode(rangeToken, left, rangeY, null);
        }
        return left;
    }

    private Node parseTerm(ParsingPolicy policy) throws ParserException {
        Node left = parseFactor(policy);
        while (matchMultiple(PLUS, MINUS) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parseFactor(policy));
        return left;
    }

    private Node parseFactor(ParsingPolicy policy) throws ParserException {
        Node left = parsePower(policy);
        while (matchMultiple(MULTIPLY, DIVIDE, INTDIV, MODULO) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parsePower(policy));
        return left;
    }

    private Node parsePower(ParsingPolicy policy) throws ParserException {
        Node left = parseUnary(policy);
        while (match(POWER) != null)
            left = new BinaryOperatorNode(getPrevious(), left, parseUnary(policy));
        return left;
    }

    private Node parseUnary(ParsingPolicy policy) throws ParserException {
        if (matchMultiple(NOT, MINUS) != null)
            return new UnaryOperatorNode(getPrevious(), parseUnary(policy));
        return parseCall(policy);
    }

    private Node parseCall(ParsingPolicy policy) throws ParserException {
        Node left = parsePrimary(policy);
        while (true) {
            if (matchSameLine(TokenType.LPAR) != null) {
                left = finishCall(left);
            } else if (matchSameLine(DOT) != null) {
                Token dot = getPrevious();
                Token name = require(VAR, "Expected id");
                left = new FieldReferenceNode(dot, left, name.getLexeme());
            } else {
                break;
            }
        }

        while (matchSameLine(TokenType.LSPAR) != null) {
            Token leftBracket = getPrevious();
            Node start = null, end = null, step = null;
            boolean isSubscript = true;
            if (match(RANGE) != null) { // [:x] [:x:y] [::x]
                if (match(RANGE) != null) {
                    step = parseOr(ParsingPolicy.noRange()); // [::x]
                } else {
                    end = parseOr(ParsingPolicy.noRange()); // [:x]
                    if (match(RANGE) != null) {
                        step = parseOr(ParsingPolicy.noRange()); // [:x:y]
                    }
                }
            } else { // [x:y] [x::y] [x:y:z] [x:+y] [x:+y:z]
                start = parseOr(ParsingPolicy.noRange());
                if (matchMultiple(RANGE, RANGE_INCLUDE) != null) {
                    if (match(RANGE) != null) {
                        step = parseOr(ParsingPolicy.noRange()); // [x::y]
                    } else if (match(RSPAR) != null) {
                        pos--; // [x:]
                    } else {
                        end = parseOr(ParsingPolicy.noRange()); // [x:y] [x:+y]
                        if (matchMultiple(RANGE) != null) {
                            step = parseOr(ParsingPolicy.noRange()); // [x:y:z] [x:+y:z]
                        }
                    }
                } else isSubscript = false;
            }
            require(RSPAR, "Expected ] to close indexing");
            if (isSubscript)
                left = new SubscriptNode(leftBracket, left, start, end, step);
            else
                left = new IndexingNode(leftBracket, left, start);
        }
        return left;
    }

    private Node finishCall(Node callee) throws ParserException {
        Token leftBracket = getPrevious();
        List<Node> arguments = new ArrayList<>();
        HashMap<String, Node> keywordArguments = new HashMap<>();
        List<Node> args = parseArgs(null);
        for (Node argument : args)
            if (argument instanceof AssignNode &&
                    ((AssignNode) argument).variable instanceof VariableNode)
                keywordArguments.put(((AssignNode) argument).variable.getToken().getLexeme(),
                        ((AssignNode) argument).value);
            else
                arguments.add(argument);
        require(TokenType.RPAR, "Expected closing bracket");
        return new CallNode(leftBracket, callee, arguments, keywordArguments);
    }

    private List<Node> parseArgs(ParsingPolicy policy) throws ParserException {
        require(LPAR);
        List<Node> args = new ArrayList<>();
        do {
            args.add(parseExpression(null));
        } while (match(COMMA) != null);
        require(RPAR);
        return args;
    }

    private Node parsePrimary(ParsingPolicy policy) throws ParserException {
        if (match(LPAR) != null) {
            Node expr = parseExpression(null);
            if (current() != null && current().getType() == COMMA) {
                List<Node> args = new ArrayList<>();
                args.add(expr);
                while (match(COMMA) != null) args.add(parseExpression(null));
                require(RPAR);
                Token arrow = require(LAMBDA_ARROW);
                Node statement = parseStatement();
                return new LiteralLambda(arrow, args, statement);
            }
            require(RPAR);
            return expr;
        }
        if (match(LITERAL_NUM) != null) return new LiteralNum(getPrevious());
        if (match(LITERAL_STR) != null) return new LiteralStr(getPrevious());
        if (match(LITERAL_FALSE) != null) return new LiteralBool(getPrevious(), false);
        if (match(LITERAL_TRUE) != null) return new LiteralBool(getPrevious(), true);
        if (match(LITERAL_NULL) != null) return new LiteralNull(getPrevious());
        if (match(LSPAR) != null) {
            Token left_bracket = getPrevious();
            if (match(RSPAR) != null) return new LiteralList(left_bracket, new ArrayList<>());
            List<Node> values;
            Node expr = parseExpression(null);
            if (matchMultiple(CONTROL_FOR, CONTROL_EVERY) != null) {
                List<VariableNode> iterators = new ArrayList<>();
                do {
                    iterators.add(new VariableNode(require(VAR)));
                } while (match(COMMA) != null);
                require(IN);
                Node iterable = parseExpression(null);
                Node condition = null;
                if (match(CONTROL_IF) != null) condition = parseExpression(null);
                require(RSPAR);
                return new ListForGeneratorNode(
                        left_bracket,
                        expr,
                        iterators,
                        iterable,
                        condition
                );
            } else if (match(CONTROL_THROUGH) != null) {
                Node range = parseRange(null);
                if (!(range instanceof RangeNode))
                    error("Expected range");
                require(AS);
                Token variable = require(VAR);
                Node condition = null;
                if (match(CONTROL_IF) != null) condition = parseExpression(null);
                require(RSPAR);
                return new ListThroughGeneratorNode(
                        left_bracket,
                        expr,
                        new VariableNode(variable),
                        (RangeNode) range,
                        condition
                );
            } else values = new ArrayList<>();
            values.add(expr);
            while (match(COMMA) != null) values.add(parseExpression(null));
            require(RSPAR);
            return new LiteralList(left_bracket, values);
        }
        if (match(LCPAR) != null) {
            Token left_brace = getPrevious();
            if (match(RCPAR) != null) return new LiteralDict(
                    left_brace,
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            List<Node> keys, values;
            Node key = parseOr(null);
            require(ASSIGN);
            Node value = parseOr(null);
            if (matchMultiple(CONTROL_FOR, CONTROL_EVERY) != null) {
                List<VariableNode> iterators = new ArrayList<>();
                do {
                    iterators.add(new VariableNode(require(VAR)));
                } while (match(COMMA) != null);
                require(IN);
                Node iterable = parseExpression(null);
                Node condition = null;
                if (match(CONTROL_IF) != null) condition = parseExpression(null);
                require(RCPAR);
                return new DictForGeneratorNode(
                        left_brace,
                        key,
                        value,
                        iterators,
                        iterable,
                        condition
                );
            } else if (match(CONTROL_THROUGH) != null) {
                Node range = parseRange(null);
                if (!(range instanceof RangeNode))
                    error("Expected range");
                require(AS);
                Token variable = require(VAR);
                Node condition = null;
                if (match(CONTROL_IF) != null) condition = parseExpression(null);
                require(RCPAR);
                return new DictThroughGeneratorNode(
                        left_brace,
                        key,
                        value,
                        new VariableNode(variable),
                        (RangeNode) range,
                        condition
                );
            } else {
                keys = new ArrayList<>();
                values = new ArrayList<>();
            }
            keys.add(key);
            values.add(value);
            while (match(COMMA) != null) {
                keys.add(parseOr(null));
                require(ASSIGN);
                values.add(parseOr(null));
            }
            require(RCPAR);
            return new LiteralDict(left_brace, keys, values);
        }
        if (match(TYPE_FUNCTION) != null && match(LPAR) != null) {
            pos--;
            Token func = getPrevious();
            List<Node> args = parseArgs(null);
            Node statement = parseStatement();
            return new LiteralLambda(func, args, statement);
        }
        Node modifiersResult = parseModifiers(policy);
        if (modifiersResult instanceof TypecastNode ||
            modifiersResult instanceof LiteralFunction)
            return modifiersResult;
        if (modifiersResult != null) {
            IncompleteModifierNode modifier = (IncompleteModifierNode) modifiersResult;
            Token variable = require(VAR);
            return new VariableNode(variable, modifier.modifiers);
        }
        if (match(VAR) != null) return new VariableNode(getPrevious());
        error("Unparseable expression or statement");
        return null;
    }

    private Node parseModifiers(ParsingPolicy policy) throws ParserException {
        List<Integer> modifiers = null;
        Integer currentModifier = ModifierConstants.UNCLARIFIED;
        do {
            if (modifiers == null) modifiers = new ArrayList<>();
            else modifiers.add(currentModifier);
            currentModifier = parseModifier();
            if (currentModifier == null) return null;
            if (match(LPAR) != null) {
                Token parent = getPrevious();
                Node expr = parseExpression(null);
                require(RPAR);
                return new TypecastNode(parent, currentModifier, expr);
            }
            if (forseePattern(VAR, LPAR)) {
                Token functionName = require(VAR);
                List<Node> args = parseArgs(null);
                Node statement = parseStatement();
                return new LiteralFunction(functionName, functionName.getLexeme(), args, statement);
            }
            if (forseeToken(VAR)) {
                int[] modifiersArray = new int[modifiers.size()];
                for (int i = 0; i < modifiersArray.length; i++)
                    if (modifiers.get(i) == null)
                        error("(INTERNAL) Unexpected null modifiers.get(i) at " +
                                "parseModifiers->do-while->if forseeToken(VAR)");
                    else
                        modifiersArray[i] = modifiers.get(i);
                return new IncompleteModifierNode(modifiersArray);
            }
        } while (match(PILLAR) != null);
        return null;
    }

    private Integer parseModifier() throws ParserException {
        int currentModifier = ModifierConstants.UNCLARIFIED;
        if (match(TYPE_NUM) != null) currentModifier |= ModifierConstants.NUM;
        else if (match(TYPE_STRING) != null) currentModifier |= ModifierConstants.STR;
        else if (match(TYPE_BOOL) != null) currentModifier |= ModifierConstants.BOOL;
        else if (match(TYPE_DICT) != null) currentModifier |= ModifierConstants.DICT;
        else if (match(TYPE_VOID) != null) currentModifier |= ModifierConstants.VOID;
        else if (match(MOD_STATIC) != null) currentModifier |= ModifierConstants.STATIC;
        else if (match(MOD_FINAL) != null) currentModifier |= ModifierConstants.FINAL;
        else if (match(MOD_REQUIRED) != null) currentModifier |= ModifierConstants.NOTNULL;
        else if (match(MOD_LOCAL) != null) currentModifier |= ModifierConstants.LOCAL;
        else if (matchMultiple(TYPE_LIST, TYPE_OBJECT, TYPE_FUNC) != null) {
            if (match(LESS) != null) {
                Integer mod = parseModifier();
                Node klass = null;
                if (mod == null) klass = parseExpression(ParsingPolicy.noComparison());
                if (klass == null) error("Expected modifier or an expression but found none");
                require(GREATER);
            }
        } else return null;
        return currentModifier;
    }

}
