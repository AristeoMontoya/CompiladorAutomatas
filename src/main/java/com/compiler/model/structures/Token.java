package com.compiler.model.structures;

import com.compiler.model.Grammar;

public class Token {
    private final String symbol;
    private String value;
    private String dataType; // TODO: Make this an enum
    private Grammar tokenType;
    private String scope;
    private String operation;
    private String expression;
    private int line;

    public Token(String symbol, Grammar tokenType, int line) {
        this.symbol = symbol;
        this.tokenType = tokenType;
        this.line = line;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Grammar getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
