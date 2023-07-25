package me.tapeline.quailj.docgen;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.comments.*;
import me.tapeline.quailj.parsing.nodes.expression.VarAssignNode;
import me.tapeline.quailj.parsing.nodes.literals.LiteralClass;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.parsing.nodes.literals.LiteralNull;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.utils.TextUtils;

import java.io.File;
import java.util.*;

public class DocumentationGenerator {

    private static HashMap<String, String> colorAssociations = Dict.make(
            new Pair<>("default", "primary"),
            new Pair<>("red", "danger"),
            new Pair<>("green", "success"),
            new Pair<>("yellow", "warning")
    );

    private String documentationHeader = "<!doctype html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\"\n" +
            "        content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
            "  <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "  <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\"\n" +
            "        rel=\"stylesheet\"\n" +
            "        integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\"\n" +
            "        crossorigin=\"anonymous\">\n" +
            "  <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js\"\n" +
            "          integrity=\"sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM\"\n" +
            "          crossorigin=\"anonymous\"></script>\n" +
            "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5" +
            "/font/bootstrap-icons.css\">\n" +
            "  <title>Docs</title>\n" +
            "</head>\n" +
            "<body class=\"row p-5\">";
    private String documentationFooter = "</body>\n" +
            "</html>";

    private List<String> tocEntries = new ArrayList<>();

    public String generateDocumentationForFile(String name, File path, Node contents) {
        List<Node> nodes = convertToList(contents);
        StringBuilder docs = new StringBuilder();

        docs.append(compileAllDocNodes(nodes));

        for (Node node : nodes) {
            docs.append(generateDocumentationForNode(node, null));
        }

        StringBuilder sb = new StringBuilder(documentationHeader);
        sb.append("<aside class=\"bd-aside sticky-xl-top text-muted " +
                "align-self-start mb-3 mb-xl-5 px-2 col-auto\">\n" +
                "    <h2 class=\"h6 pt-4 pb-3 mb-4 border-bottom\">")
                .append(name).append("</h2>\n")
                .append("    <nav class=\"small\" id=\"toc\">\n")
                .append("      <ul class=\"list-unstyled\">\n")
                .append("        <li class=\"my-2\">\n")
                .append("          <ul class=\"list-unstyled ps-3\">\n");
        for (String tocEntry : tocEntries) {
            sb.append("<li><a class=\"d-inline-flex align-items-center rounded\" href=\"#")
                    .append(tocEntry).append("\">\n").append("   ").append(tocEntry).append("\n")
                    .append("</a></li>\n");
        }
        sb.append(
                "          </ul>\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </nav>\n" +
                "</aside>");
        sb.append("<div class=\"col\">");
        sb.append(docs);
        sb.append("</div>");
        sb.append(documentationFooter);
        return sb.toString();
    }

    private List<Node> convertToList(Node node) {
        if (node instanceof BlockNode)
            return ((BlockNode) node).nodes;
        return Collections.singletonList(node);
    }

    public String compileAllDocNodes(List<Node> nodes) {
        List<String> docStrings = new ArrayList<>();
        List<DocBadgeNode> docBadges = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        String since = null;
        for (Node node : nodes) {
            if (node instanceof DocSinceNode)
                since = ((DocSinceNode) node).since;
            else if (node instanceof DocStringNode)
                docStrings.add(((DocStringNode) node).docString);
            else if (node instanceof DocAuthorNode)
                authors.add(((DocAuthorNode) node).author);
            else if (node instanceof DocBadgeNode)
                docBadges.add(((DocBadgeNode) node));
            else if (node instanceof DocTOCEntryNode)
                tocEntries.add(((DocTOCEntryNode) node).docString);
        }
        StringBuilder sb = new StringBuilder();
        if (docStrings.size() != 0) {
            sb.append("<p>");
            for (String docString : docStrings)
                sb.append(docString).append("<br>");
            sb.append("</p>");
        }
        if (docBadges.size() != 0) {
            sb.append("<p>Features:");
            for (DocBadgeNode badge : docBadges)
                sb.append("<span class=\"badge bg-").append(colorAssociations.get(badge.badgeColor))
                        .append("\">").append(badge.badgeString).append("</span>");
            sb.append("</p>");
        }
        if (authors.size() == 1) {
            sb.append("<p>Author: ").append(authors.get(0)).append("</p>");
        }
        if (authors.size() > 1) {
            sb.append("<p>Authors: <ul>");
            for (String author : authors)
                sb.append("<li>").append(author).append("</li>");
            sb.append("</ul></p>");
        }
        if (since != null) {
            sb.append("<span class=\"badge bg-secondary\">Since ").append(since).append("</span>");
        }
        return sb.toString();
    }


    public String generateDocumentationForNode(Node node, DocumentationContext ctx) {
        if (node instanceof LiteralClass) {
            LiteralClass classNode = (LiteralClass) node;
            DocumentationContext scope = new DocumentationContext(ctx, classNode.name);
            StringBuilder sb = new StringBuilder();
            sb.append("<h3 id=\"").append(scope.qualifiedPath)
                    .append("\"><code>").append(scope.qualifiedPath)
                    .append("</code>");
            if (classNode.like != null)
                sb.append("<i class=\"bi-alarm\"></i> extends <code>")
                        .append(((VariableNode) classNode.like).name).append("</code>");
            sb.append("</h3>\n");
            sb.append("<div class=\"p-3\">");
            sb.append(compileAllDocNodes(classNode.initialize));
            sb.append("<div class=\"m-3\"></div>");
            sb.append("<h6>Fields</h6>");
            if (classNode.contents.size() == 0)
                sb.append("<div class=\"ps-3\"><p>None</p></div>");
            else {
                sb.append("<ul class=\"ps-3\">");
                for (VarAssignNode field : classNode.contents)
                    sb.append("<li><code>").append(variableToString(field.variable)).append("</code></li>");
                sb.append("</ul>");
            }

            sb.append("<h6>Methods</h6><div class=\"ps-3\">");
            if (classNode.methods.size() == 0)
                sb.append("<p>None</p></div>");
            else {
                for (LiteralFunction method : classNode.methods.values())
                    sb.append(generateDocumentationForNode(method, scope));
                sb.append("</div>");
            }
            sb.append("</div>");
            return sb.toString();
        } else if (node instanceof LiteralFunction) {
            LiteralFunction funcNode = (LiteralFunction) node;
            DocumentationContext scope = new DocumentationContext(ctx, funcNode.name);
            StringBuilder sb = new StringBuilder();
            sb.append("<h6 id=\"").append(scope.qualifiedPath)
                    .append("\"><code>");
            sb.append(TextUtils.modifierToStringRepr(funcNode.funcModifier)).append(" ")
                    .append(funcNode.name)
                    .append(argsToString(funcNode.args))
                    .append("</code></h6>");
            sb.append("<div class=\"ps-3\">");
            sb.append(compileAllDocNodes(convertToList(funcNode.code)));
            sb.append("</div>");
            return sb.toString();
        } else if (node instanceof DocHtmlNode)
            return ((DocHtmlNode) node).html + "\n";
        return "";
    }

    private String argsToString(List<LiteralFunction.Argument> arguments) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(TextUtils.modifiersToStringRepr(arguments.get(i).modifiers));
            if (arguments.get(i).defaultValue instanceof LiteralNull)
                sb.append(arguments.get(i).name);
            else
                sb.append(arguments.get(i).name).append("=")
                    .append(arguments.get(i).defaultValue.stringRepr());
            if (arguments.get(i).type == LiteralFunction.Argument.POSITIONAL_CONSUMER)
                sb.append("...");
            if (arguments.get(i).type == LiteralFunction.Argument.KEYWORD_CONSUMER)
                sb.append("{...}");
            if (i + 1 < arguments.size())
                sb.append(", ");
        }
        return sb.toString() + ")";
    }

    private String variableToString(VariableNode variableNode) {
        return TextUtils.modifiersToStringRepr(variableNode.modifiers) + " " + variableNode.name;
    }

}
