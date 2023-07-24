package me.tapeline.quailj.lexing;

import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.HashMap;

/**
 * Definitions for keywords, symbols, etc.
 * @author Tapeline
 */
public enum TokenType {

    CONTROL_THROUGH,
    CONTROL_IF,
    CONTROL_ELSEIF,
    CONTROL_ELSE,
    CONTROL_TRY,
    CONTROL_CATCH,
    CONTROL_WHILE,
    CONTROL_LOOP,
    CONTROL_STOP_WHEN,
    CONTROL_EVERY,
    CONTROL_ON,
    CONTROL_WHEN,
    CONTROL_FOR,

    TYPE_FUNC,
    TYPE_STRING,
    TYPE_BOOL,
    TYPE_NUM,
    TYPE_DICT,
    TYPE_LIST,
    TYPE_OBJECT,
    TYPE_FUNCTION,
    TYPE_METHOD,
    TYPE_CLASS,
    TYPE_VOID,

    MOD_REQUIRED,
    MOD_ANYOF,
    MOD_LOCAL,
    MOD_FINAL,
    MOD_STATIC,

    EFFECT_ASSERT,
    EFFECT_USE,
    EFFECT_THROW,
    EFFECT_IMPORT,
    EFFECT_STRIKE,
    EFFECT_RETURN,

    INSTRUCTION_BREAK,
    INSTRUCTION_CONTINUE,

    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MODULO,
    INTDIV,
    POWER,
    ASSIGN,
    EQUALS,
    NOT_EQUALS,
    GREATER,
    LESS,
    GREATER_EQUAL,
    LESS_EQUAL,
    AND,
    OR,
    RANGE,
    RANGE_INCLUDE,
    NOT,

    SHIFT_LEFT,
    SHIFT_RIGHT,

    SHORT_PLUS,
    SHORT_MINUS,
    SHORT_MULTIPLY,
    SHORT_DIVIDE,
    SHORT_MODULO,
    SHORT_INTDIV,
    SHORT_POWER,

    LITERAL_NULL,
    LITERAL_STR,
    LITERAL_NUM,
    LITERAL_FALSE,
    LITERAL_TRUE,

    CONSTRUCTOR,
    OVERRIDE,
    SETS,
    GETS,
    AS,
    ASYNC,

    LPAR,
    RPAR,
    LSPAR,
    RSPAR,
    LCPAR,
    RCPAR,

    CONSUMER,
    KWARG_CONSUMER,
    LAMBDA_ARROW,
    PILLAR,
    COMMA,
    DOT,
    VAR,
    IN,
    INSTANCEOF,
    LIKE,
    DOCS,
    EOF,
    TAB,
    EOL;

    /**
     * Keywords and matching TokenTypes
     */
    public static final HashMap<String, TokenType> keywords = Dict.make(
            new Pair<>("while", CONTROL_WHILE),
            new Pair<>("catch", CONTROL_CATCH),
            new Pair<>("else", CONTROL_ELSE),
            new Pair<>("through", CONTROL_THROUGH),
            new Pair<>("elseif", CONTROL_ELSEIF),
            new Pair<>("stop when", CONTROL_STOP_WHEN),
            new Pair<>("every", CONTROL_EVERY),
            new Pair<>("if", CONTROL_IF),
            new Pair<>("loop", CONTROL_LOOP),
            new Pair<>("when", CONTROL_WHEN),
            new Pair<>("try", CONTROL_TRY),
            new Pair<>("on", CONTROL_ON),
            new Pair<>("for", CONTROL_FOR),

            new Pair<>("bool", TYPE_BOOL),
            new Pair<>("class", TYPE_CLASS),
            new Pair<>("dict", TYPE_DICT),
            new Pair<>("func", TYPE_FUNC),
            new Pair<>("list", TYPE_LIST),
            new Pair<>("function", TYPE_FUNCTION),
            new Pair<>("method", TYPE_METHOD),
            new Pair<>("num", TYPE_NUM),
            new Pair<>("object", TYPE_OBJECT),
            new Pair<>("string", TYPE_STRING),
            new Pair<>("void", TYPE_VOID),

            new Pair<>("false", LITERAL_FALSE),
            new Pair<>("true", LITERAL_TRUE),
            new Pair<>("null", LITERAL_NULL),

            new Pair<>("async", ASYNC),

            new Pair<>("as", AS),
            new Pair<>("in", IN),

            new Pair<>("override", OVERRIDE),
            new Pair<>("constructor", CONSTRUCTOR),
            new Pair<>("gets", GETS),
            new Pair<>("sets", SETS),

            new Pair<>("break", INSTRUCTION_BREAK),
            new Pair<>("continue", INSTRUCTION_CONTINUE),

            new Pair<>("assert", EFFECT_ASSERT),
            new Pair<>("import", EFFECT_IMPORT),
            new Pair<>("return", EFFECT_RETURN),
            new Pair<>("strike", EFFECT_STRIKE),
            new Pair<>("throw", EFFECT_THROW),
            new Pair<>("use", EFFECT_USE),

            new Pair<>("anyof", MOD_ANYOF),
            new Pair<>("local", MOD_LOCAL),
            new Pair<>("final", MOD_FINAL),
            new Pair<>("required", MOD_REQUIRED),
            new Pair<>("static", MOD_STATIC),

            new Pair<>("has", LCPAR),
            new Pair<>("does", LCPAR),
            new Pair<>("with", LCPAR),
            new Pair<>("do", LCPAR),
            new Pair<>("then", LCPAR),
            new Pair<>("end", RCPAR),

            new Pair<>("not", NOT),

            new Pair<>("instanceof", INSTANCEOF),

            new Pair<>("like", LIKE)

    );

    /**
     * Shortened operators (e.g. +=) and matching full operators
     */
    public static final HashMap<TokenType, TokenType> shortToNormal = Dict.make(
            new Pair<>(SHORT_DIVIDE, DIVIDE),
            new Pair<>(SHORT_MODULO, MODULO),
            new Pair<>(SHORT_INTDIV, INTDIV),
            new Pair<>(SHORT_POWER, POWER),
            new Pair<>(SHORT_MINUS, MINUS),
            new Pair<>(SHORT_MULTIPLY, MULTIPLY),
            new Pair<>(SHORT_PLUS, PLUS)
    );

    /**
     * Operators and matching TokenTypes
     */
    public static final HashMap<String, TokenType> stringToOps = Dict.make(
            new Pair<>("+", PLUS),
            new Pair<>("-", MINUS),
            new Pair<>("/", DIVIDE),
            new Pair<>("//", INTDIV),
            new Pair<>("*", MULTIPLY),
            new Pair<>("^", POWER),
            new Pair<>("&&", AND),
            new Pair<>("and", AND),
            new Pair<>("||", OR),
            new Pair<>("or", OR),
            new Pair<>("!", NOT),
            new Pair<>("==", EQUALS),
            new Pair<>("!=", NOT_EQUALS),
            new Pair<>(">", GREATER),
            new Pair<>(">=", GREATER_EQUAL),
            new Pair<>("<", LESS),
            new Pair<>("<=", LESS_EQUAL)
    );

    /**
     * All operators that could be matrix and array
     * (e.g. a [+] b, c {>>} d)
     */
    public static final String[] ops = {
            "+",
            "-",
            "/",
            "//",
            "*",
            "^",
            "&&",
            "and",
            "||",
            "or",
            "!",
            "==",
            "!=",
            ">",
            ">=",
            "<",
            "<=",
            ">>",
            "<<"
    };

    /**
     * Overridable operators and their override names
     */
    public static final HashMap<TokenType, String> overrideOps = Dict.make(
            new Pair<>(PLUS, "_add"),
            new Pair<>(MINUS, "_sub"),
            new Pair<>(INTDIV, "_intdiv"),
            new Pair<>(DIVIDE, "_div"),
            new Pair<>(MULTIPLY, "_mul"),
            new Pair<>(POWER, "_pow"),
            new Pair<>(MODULO, "_mod"),
            new Pair<>(EQUALS, "_eq"),
            new Pair<>(NOT_EQUALS, "_neq"),
            new Pair<>(LESS_EQUAL, "_cmple"),
            new Pair<>(GREATER_EQUAL, "_cmpge"),
            new Pair<>(GREATER, "_cmpg"),
            new Pair<>(LESS, "_cmpl"),
            new Pair<>(TYPE_STRING, "_tostring"),
            new Pair<>(TYPE_NUM, "_tonumber"),
            new Pair<>(TYPE_BOOL, "_tobool"),
            new Pair<>(NOT, "_not"),
            new Pair<>(SHIFT_LEFT, "_shr"),
            new Pair<>(SHIFT_RIGHT, "_shl")
    );

}
