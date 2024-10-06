package com.compiler.model.structures;

public class Quadruplet {
    String leftSideOperand;
    String rightSideOperand;
    String operator;
    String result;
    String tag;
    String identifier;

    public Quadruplet(String leftSideOperand, String rightSideOperand, String operator, String result) {
        this.leftSideOperand = leftSideOperand;
        this.rightSideOperand = rightSideOperand;
        this.operator = operator;
        this.result = result;
    }

    public String getFormat() {
        return String.format("| %-10s | %-10s | %-10s | %-10s |", operator, leftSideOperand, rightSideOperand, tag);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLeftSideOperand() {
        return leftSideOperand;
    }

    public void setLeftSideOperand(String leftSideOperand) {
        this.leftSideOperand = leftSideOperand;
    }

    public String getRightSideOperand() {
        return rightSideOperand;
    }

    public void setRightSideOperand(String rightSideOperand) {
        this.rightSideOperand = rightSideOperand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}