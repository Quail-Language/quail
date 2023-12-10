package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class TryNode extends Node {

    public final Node code;
    public final List<CatchClause> catchClauses;

    public TryNode(Token token, Node code, List<CatchClause> catchClauses) {
        super(token);
        this.code = code;
        this.catchClauses = catchClauses;
    }

    @Override
    public String stringRepr() {
        return "try{" + code.stringRepr() + " " +
                TextUtils.catchClauseListToStringRepr(catchClauses) + "}";
    }

}
