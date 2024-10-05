package com.compiler.model;

import java.util.ArrayList;
import java.util.Stack;

class Node {
    char value;
    Node left, right;

    Node(char value) {
        this.value = value;
        left = right = null;
    }
}

public class Tree {
    private ArrayList<Quadruplet> quadruplets;
    private Node rootNode;
    private String tag;

    public Tree() {
        quadruplets = new ArrayList<>();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*';
    }

    public void infixPrint(Node node) {
        if (node != null) {
            infixPrint(node.left);
            System.out.println(node.value);
            infixPrint(node.right);
        }
    }

    public int traverse(Node node) {
        if (node != null) {
            String data = "" + node.value;
            int leftValue, rightValue;
            leftValue = traverse(node.left);
            rightValue = traverse(node.right);
            if (data.matches("^(\\+|-|/|\\*)")) {
                int result = 0;
                switch (node.value) {
                    case '+':
                        result = leftValue + rightValue;
                        quadruplets.add(new Quadruplet("" + leftValue, "" + rightValue, "+", "" + result));
                        break;
                    case '-':
                        result = leftValue - rightValue;
                        quadruplets.add(new Quadruplet("" + leftValue, "" + rightValue, "-", "" + result));
                        break;
                    case '*':
                        result = leftValue * rightValue;
                        quadruplets.add(new Quadruplet("" + leftValue, "" + rightValue, "*", "" + result));
                        break;
                    case '/':
                        result = leftValue / rightValue;
                        quadruplets.add(new Quadruplet("" + leftValue, "" + rightValue, "/", "" + result));
                        break;
                }
                return result;
            } else {
                return Integer.parseInt("" + node.value);
            }
        }
        return 0;
    }

    public Node buildTree(char[] expression) {
        Stack<Node> stack = new Stack<>();
        Node t1, t2, t3;

        for (char actual : expression) {
            if (!isOperator(actual)) {
                t1 = new Node(actual);
                stack.push(t1);
            } else {
                t1 = new Node(actual);

                t2 = stack.pop();
                t3 = stack.pop();

                t1.right = t2;
                t1.left = t3;

                stack.push(t1);
            }
        }
        t1 = stack.peek();
        stack.pop();
        return t1;
    }

    public ArrayList<Quadruplet> getQuadruplets() {
        return quadruplets;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
