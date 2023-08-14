package me.tapeline.quailj.parsing;

/**
 * Determines which parsing methods should be skipped
 * @author Tapeline
 */
public class ParsingPolicy {

    public boolean excludeComparison = false;
    public boolean excludeRange = false;

    public static ParsingPolicy noComparison() {
        ParsingPolicy policy = new ParsingPolicy();
        policy.excludeComparison = true;
        return policy;
    }

    public static ParsingPolicy noRange() {
        ParsingPolicy policy = new ParsingPolicy();
        policy.excludeRange = true;
        return policy;
    }

}
