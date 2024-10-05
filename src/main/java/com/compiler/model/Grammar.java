package com.compiler.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Grammar {
    CLASS_DECLARATION("(class)"),
    SPECIAL_SYMBOL("(\\(|\\)|\\{|\\}|\\[|\\]|;)"),
    EVALUATION_SYMBOL("(<=|>=|<|>|==|!=)"),
    ASSIGNATION("(=)"),
    WHILE_STATEMENT("(while)"),
    IF_STATEMENT("(if)"),
    SPECIFIER("(boolean|int)"),
    LITERAL_BOOLEAN("(true|false)"),
    ACCESS_MODIFIER("(public|private)"),
    ARITHMETICAL_OPERATOR("(\\+|-|/|\\*)"),
    LOGIC_OPERATOR("(&&|\\|\\|)"),
    IDENTIFIER("[a-z]+[1-9]*"),
    LITERAL_INTEGER("[1-9]?[0-9]");

    private final Pattern pattern;

    Grammar(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    /*
        TODO: This could throw an exception, if we find a symbol that is not found
            it means the code is wrong.
     */
    public int findComponent(String input) {
        Matcher match = pattern.matcher(input);

        if (match.find()) {
            return match.end();
        }
        return -1;
    }
}
