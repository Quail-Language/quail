package me.tapeline.quailj.parsing;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.effects.*;
import me.tapeline.quailj.parsing.nodes.expression.*;
import me.tapeline.quailj.parsing.nodes.generators.*;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.sections.*;
import me.tapeline.quailj.parsing.nodes.utils.IncompleteModifierNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.IntFlags;

import java.util.ArrayList;
import java.util.Collections;
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
        if (!reachedEnd())
            throw new ParserException(
                    message,
                    tokens.get(pos)
            );
        throw new ParserException(
                message,
                tokens.get(tokens.size() - 1)
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

    private int nextSignificantIndex() {
        if (reachedEnd()) return -1;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return -1;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return -1;
        return increment;
    }

    private boolean forseeToken(TokenType type) {
        int signIndex = nextSignificantIndex();
        return getNext(signIndex) != null && getNext(signIndex).getType() == type;
    }

    private boolean forseePattern(TokenType... pattern) {
        int signIndex = nextSignificantIndex();
        for (int i = 0; i < pattern.length; i++)
            if (getNext(signIndex + i) == null || getNext(signIndex + i).getType() != pattern[i])
                return false;
        return true;
    }

    private Token require(TokenType type) throws ParserException {
        return require(type, null);
    }

    private Token require(TokenType type, String message) throws ParserException {
        if (reachedEnd())
            error(message == null? "Expected " + type.toString() + " but file ended" : message);
        Token token = match(type);
        if (token == null && getNext() != null)
            error(message == null? "Expected " + type.toString() +
                    " but found " + getNext().getType() : message);
        else if (token == null)
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

    private BlockNode parseBlockUntil(Token beginning, TokenType... until) throws ParserException {
        BlockNode block = new BlockNode(beginning, new ArrayList<>());
        match(LCPAR);
        while (true) {
            for (TokenType end : until) if (match(end) != null) return block;
            block.nodes.add(parseStatement());
        }
    }

    private int consumeTabs() {
        int tabCount = 0;
        for (; matchExactly(TAB) != null; tabCount++);
        return tabCount;
    }

    private BlockNode parsePythonBlock() throws ParserException {
        Token blockToken = getPrevious();
        matchExactly(EOL);
        int tabCount = consumeTabs();
        pos -= tabCount;
        BlockNode block = new BlockNode(blockToken, new ArrayList<>());
        while (true) {
            int currentLineTabs = consumeTabs();
            if (currentLineTabs < tabCount) {
                pos -= currentLineTabs;
                return block;
            }
            block.nodes.add(parseStatement());
        }
    }

    public BlockNode parse() throws ParserException {
        BlockNode statements = new BlockNode(current(), new ArrayList<>());
        while (toNextSignificant() != null)
            statements.nodes.add(parseStatement());
        return statements;
    }

    private Node parseStatement() throws ParserException {
        if (forseePattern(RANGE, EOL)) {
            match(RANGE);
            match(EOL);
            return parsePythonBlock();
        }

        if (match(LCPAR) != null) {
            Token brace = getPrevious();
            List<Node> nodes = new ArrayList<>();
            while (match(RCPAR) == null)
                nodes.add(parseStatement());
            return new BlockNode(brace, nodes);
        }
        if (match(CONTROL_IF) != null) {
            Token ifToken = getPrevious();
            Node ifBranchCondition = parseExpression(null);
            BlockNode ifBranchCode;
            if (match(LCPAR) != null) {
                ifBranchCode = parseBlockUntil(getPrevious(), CONTROL_ELSEIF, CONTROL_ELSE, RCPAR);
                match(RCPAR);
            } else
                ifBranchCode = new BlockNode(current(), Collections.singletonList(parseStatement()));
            IfNode ifNode = new IfNode(ifToken, ifBranchCondition, ifBranchCode);
            while (match(CONTROL_ELSEIF) != null) {
                Node elseIfBranchCondition = parseExpression(null);
                if (elseIfBranchCondition == null) error("Null elseif condition");
                BlockNode elseIfCode;
                if (match(LCPAR) != null) {
                    elseIfCode = parseBlockUntil(getPrevious(), CONTROL_ELSEIF, CONTROL_ELSE, RCPAR);
                    match(RCPAR);
                } else
                    elseIfCode = new BlockNode(current(), Collections.singletonList(parseStatement()));
                ifNode.conditions.add(elseIfBranchCondition);
                ifNode.branches.add(elseIfCode);
                match(RCPAR);
            }
            if (match(CONTROL_ELSE) != null)
                if (match(LCPAR) != null) {
                    ifNode.elseBranch = parseBlockUntil(getPrevious(), RCPAR);
                    match(RCPAR);
                } else
                    ifNode.elseBranch = new BlockNode(current(), Collections.singletonList(parseStatement()));
            return ifNode;
        }
        if (match(CONTROL_WHILE) != null) {
            Token whileToken = getPrevious();
            return new WhileNode(whileToken, parseExpression(null), parseStatement());
        }
        if (match(CONTROL_LOOP) != null) {
            Token loopToken = getPrevious();
            matchSameLine(LCPAR);
            List<Node> nodes = new ArrayList<>();
            while (match(RCPAR) == null && !forseeToken(CONTROL_STOP_WHEN))
                nodes.add(parseStatement());
            require(CONTROL_STOP_WHEN);
            Node condition = parseExpression(null);
            return new LoopStopNode(loopToken, condition, new BlockNode(loopToken, nodes));
        }
        if (match(CONTROL_THROUGH) != null) {
            Token throughToken = getPrevious();
            Node range = parseRange(null);
            if (!(range instanceof RangeNode))
                error("Through range should be range");
            require(AS);
            Token iterator = require(VAR);
            Node code = parseStatement();
            return new ThroughNode(
                    throughToken,
                    ((RangeNode) range).rangeStart,
                    ((RangeNode) range).rangeEnd,
                    ((RangeNode) range).rangeStep,
                    iterator.getLexeme(),
                    code
            );
        }
        if (matchMultiple(CONTROL_FOR, CONTROL_EVERY) != null) {
            Token forToken = getPrevious();
            List<String> iterators = new ArrayList<>();
            do {
                Token iterator = require(VAR);
                iterators.add(iterator.getLexeme());
            } while (match(COMMA) != null);
            require(IN);
            Node iterable = parseExpression(null);
            Node code = parseStatement();
            return new ForNode(forToken, iterators, iterable, code);
        }
        if (match(CONTROL_TRY) != null) {
            Token tryToken = getPrevious();
            matchSameLine(LCPAR);
            List<Node> nodes = new ArrayList<>();
            while (match(RCPAR) == null && !forseeToken(CONTROL_CATCH))
                nodes.add(parseStatement());
            List<CatchClause> catchClauses = new ArrayList<>();
            while (match(CONTROL_CATCH) != null) {
                Token catchToken = getPrevious();
                Node instance = null;
                if (!forseeToken(AS))
                    instance = parseExpression(null);
                require(AS);
                String var = require(VAR).getLexeme();
                matchSameLine(LCPAR);
                List<Node> catchNodes = new ArrayList<>();
                while (match(RCPAR) == null && !forseeToken(CONTROL_CATCH))
                    catchNodes.add(parseStatement());
                pos--;
                match(RCPAR);
                catchClauses.add(new CatchClause(instance, var,
                        new BlockNode(catchToken, catchNodes)));
            }
            return new TryNode(tryToken, new BlockNode(tryToken, nodes), catchClauses);
        }
        if (matchMultiple(INSTRUCTION_BREAK, INSTRUCTION_CONTINUE) != null)
            return new InstructionNode(getPrevious());

        Node effect = parseEffect();
        if (effect != null) return effect;

        Node function = parseFunction();
        if (function != null) return function;

        Node klass = parseClass();
        if (klass != null) return klass;

        return parseExpression(null);
    }

    private Node parseEffect() throws ParserException {
        if (match(EFFECT_RETURN) != null) {
            Token effectToken = getPrevious();
            if (matchSameLine(EOL) != null)
                return new ReturnNode(effectToken);
            return new ReturnNode(effectToken, parseExpression(null));
        }
        if (matchMultiple(EFFECT_ASSERT, EFFECT_IMPORT, EFFECT_THROW, EFFECT_STRIKE) != null) {
            Token effectToken = getPrevious();
            return new EffectNode(effectToken, parseExpression(null));
        }
        if (match(EFFECT_USE) != null) {
            Token effectToken = getPrevious();
            String path = require(LITERAL_STR).getLexeme();
            require(ASSIGN);
            String var = require(VAR).getLexeme();
            return new UseNode(effectToken, path, var);
        }
        return null;
    }

    private Node parseFunction() throws ParserException {
        if (forseePattern(MOD_STATIC, TYPE_METHOD) ||
            forseePattern(MOD_STATIC, TYPE_FUNCTION)) {
            require(MOD_STATIC);
            Token funcToken = match(TYPE_METHOD);
            if (funcToken == null)
                funcToken = require(TYPE_FUNCTION);
            Token name = require(VAR);
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, name.getLexeme(), args, statement);
        }
        if (matchMultiple(TYPE_METHOD, TYPE_FUNCTION) != null) {
            Token funcToken = getPrevious();
            Token name = require(VAR);
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, name.getLexeme(), args, statement);
        }
        if (match(GETS) != null) {
            Token funcToken = getPrevious();
            Token name = require(VAR);
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, "_get_" + name.getLexeme(), args, statement);
        }
        if (match(SETS) != null) {
            Token funcToken = getPrevious();
            Token name = require(VAR);
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, "_set_" + name.getLexeme(), args, statement);
        }
        if (match(OVERRIDE) != null) {
            Token funcToken = getPrevious();
            Token name = consumeSignificant();
            if (name == null) error("Unexpected end");
            String functionName = overrideOps.get(name.getType());
            if (name.getLexeme().equals("index"))
                functionName = "_index";
            else if (name.getLexeme().equals("setIndex"))
                functionName = "_setIndex";
            else if (name.getLexeme().equals("call"))
                functionName = "_call";
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, functionName, args, statement);
        }
        if (match(CONSTRUCTOR) != null) {
            Token funcToken = getPrevious();
            List<LiteralFunction.Argument> args = convertDefinedArguments(parseArgs(null, true));
            Node statement = parseStatement();
            return new LiteralFunction(funcToken, "_constructor", args, statement);
        }
        return null;
    }

    private Node parseClass() throws ParserException {
        if (match(TYPE_CLASS) != null) {
            Token classToken = getPrevious();
            Token className = require(VAR);
            Node like = null;
            if (match(LIKE) != null)
                like = parseOr(null);
            match(LCPAR);
            HashMap<String, Node> contents = new HashMap<>();
            HashMap<String, LiteralFunction> methods = new HashMap<>();
            List<Node> initialize = new ArrayList<>();
            while (match(RCPAR) == null) {
                Node expr = parseStatement();
                if (expr instanceof AssignNode)
                    contents.put(((AssignNode) expr).variable.getToken().getLexeme(),
                            ((AssignNode) expr).value);
                else if (expr instanceof VariableNode)
                    contents.put(((VariableNode) expr).name,
                            getDefaultNodeFor(((VariableNode) expr).modifiers));
                else if (expr instanceof LiteralFunction)
                    methods.put(((LiteralFunction) expr).name, ((LiteralFunction) expr));
                else
                    initialize.add(expr);
            }
            return new LiteralClass(classToken, className.getLexeme(), like, contents, methods, initialize);
        }
        return null;
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
        if (policy != null && policy.excludeComparison) return left;
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
        if (policy != null && policy.excludeRange) return left;
        if (forseePattern(RANGE, EOL))
            return left;
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
            } else if (matchSameLine(TokenType.LSPAR) != null) {
                Token leftBracket = getPrevious();
                Node start = null, end = null, step = null;
                boolean isSubscript = true;
                if (match(RANGE) != null) { // [:x] [:x:y] [::x] [:] [::]
                    if (forseeToken(RSPAR)) {// [:]
                        match(RANGE);
                        end = null;
                    } else if (match(RANGE) != null) {
                        if (forseeToken(RSPAR)) { // [::]
                            match(RANGE);
                            end = null;
                        } else
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
            } else {
                break;
            }
        }

        return left;
    }

    private Node finishCall(Node callee) throws ParserException {
        Token leftBracket = getPrevious();
        List<Node> arguments = new ArrayList<>();
        HashMap<String, Node> keywordArguments = new HashMap<>();
        List<Node> args = parseArgs(null, false);
        for (Node argument : args)
            if (argument instanceof AssignNode &&
                    ((AssignNode) argument).variable instanceof VariableNode)
                keywordArguments.put(((AssignNode) argument).variable.getToken().getLexeme(),
                        ((AssignNode) argument).value);
            else
                arguments.add(argument);
        return new CallNode(leftBracket, callee, arguments, keywordArguments);
    }

    private List<Node> parseArgs(ParsingPolicy policy, boolean requireLeft) throws ParserException {
        if (requireLeft) require(LPAR);
        if (match(RPAR) != null)
            return new ArrayList<>();
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
            List<Node> args = parseArgs(null, true);
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
            boolean isArgConsumer = false, isKwargConsumer = false;
            if (match(CONSUMER) != null)
                isArgConsumer = true;
            else if (match(KWARG_CONSUMER) != null)
                isKwargConsumer = true;
            return new VariableNode(variable, modifier.modifiers, isArgConsumer, isKwargConsumer);
        }
        if (match(VAR) != null) {
            Token variable = getPrevious();
            boolean isArgConsumer = false, isKwargConsumer = false;
            if (match(CONSUMER) != null)
                isArgConsumer = true;
            else if (match(KWARG_CONSUMER) != null)
                isKwargConsumer = true;
            return new VariableNode(variable, new int[0], isArgConsumer, isKwargConsumer);
        }
        error("Unparseable expression or statement");
        return null;
    }

    private List<LiteralFunction.Argument> convertDefinedArguments(List<Node> argumentNodes)
            throws ParserException {
        List<LiteralFunction.Argument> arguments = new ArrayList<>();
        for (Node argumentNode : argumentNodes) {
            if (argumentNode instanceof VariableNode)
                arguments.add(LiteralFunction.Argument.fromVariable((VariableNode) argumentNode));
            else if (argumentNode instanceof AssignNode) {
                if (!(((AssignNode) argumentNode).variable instanceof VariableNode))
                    error("Invalid argument definition");
                arguments.add(LiteralFunction.Argument.fromAssignment((AssignNode) argumentNode));
            } else
                error("Invalid argument");
        }
        return arguments;
    }

    private Node parseModifiers(ParsingPolicy policy) throws ParserException {
        List<Integer> modifiers = null;
        Integer currentModifier = ModifierConstants.UNCLARIFIED;
        do {
            if (modifiers == null) modifiers = new ArrayList<>();
            currentModifier = parseModifier();
            if (currentModifier == null) return null;
            modifiers.add(currentModifier);
            if (match(LPAR) != null) {
                Token parent = getPrevious();
                Node expr = parseExpression(null);
                require(RPAR);
                return new TypecastNode(parent, currentModifier, expr);
            }
            if (forseePattern(VAR, LPAR)) {
                Token functionName = require(VAR);
                List<Node> args = parseArgs(null, true);
                Node statement = parseStatement();
                return new LiteralFunction(
                        functionName,
                        functionName.getLexeme(),
                        convertDefinedArguments(args),
                        statement
                );
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

    public static Node getDefaultNodeFor(int[] modifiers) {
        if (modifiers.length != 1) return new LiteralNull(Token.UNDEFINED);
        if (IntFlags.check(modifiers[0], ModifierConstants.BOOL))
            return new LiteralBool(Token.UNDEFINED, false);
        if (IntFlags.check(modifiers[0], ModifierConstants.DICT))
            return new LiteralDict(Token.UNDEFINED, new ArrayList<>(), new ArrayList<>());
        if (IntFlags.check(modifiers[0], ModifierConstants.LIST))
            return new LiteralList(Token.UNDEFINED, new ArrayList<>());
        if (IntFlags.check(modifiers[0], ModifierConstants.NUM))
            return new LiteralNum(Token.UNDEFINED, 0);
        if (IntFlags.check(modifiers[0], ModifierConstants.STR))
            return new LiteralStr(Token.UNDEFINED, "");
        return new LiteralNull(Token.UNDEFINED);
    }

}
