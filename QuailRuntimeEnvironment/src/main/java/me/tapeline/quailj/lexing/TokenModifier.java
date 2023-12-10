package me.tapeline.quailj.lexing;

/**
 * TokenModifier tells runtime how to handle binary and
 * unary operators. By default, any operator is applied
 * straightforwardly: [1, 2] + [3, 4] = [1, 2, 3, 4].
 * TokenModifier can change this behaviour: if operator
 * is wrapped in brackets, then runtime will apply this
 * operator for each pair: [1, 2] [+] [3, 4] = [1+3, 2+4] = [4, 6].
 * If operator is wrapped in curly braces, then it will be
 * applied for each pair of each sub-list pair of operands:
 * [[1, 2], [3, 4]] {+} [[3, 2], [1, 0]] = [[4, 4], [4, 4]]
 * Learn more at Quail Specification
 * @author Tapeline
 */
public enum TokenModifier {

    /**
     * Straightforward application
     */
    SINGULAR_MOD,

    /**
     * Array application: [operator]
     */
    ARRAY_MOD,

    /**
     * Matrix application: {operator}
     */
    MATRIX_MOD

}
