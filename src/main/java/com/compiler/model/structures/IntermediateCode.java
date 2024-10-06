package com.compiler.model.structures;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IntermediateCode {
    /*TODO:
     *	-Entender que diablos debo hacer
     *		- Aparentemente necesito un árbol sintáctico.
     *		- Aparentemente partiendo del árbol sintáctico puedo hacer el código de tres direcciones.
     *	-Entender que diablos significa.
     *	-Entender como diablos debo hacerlo.
     *		- 1. Hacer el árbol.
     *		- 2. Hacer los cuadruplos.
     */

    private ArrayList<Token> tokens;
    private ArrayList<Quadruplet> quadruplets;
    private ArrayList<Tree> trees;

    public IntermediateCode(ArrayList<Token> tokens) {
        this.tokens = tokens;
        generateTrees();
    }

    private void generateTrees() {
        ArrayList<Token> expressionList = extractExpressions();
        String[] separatedExpressions = separateExpressions(expressionList);
        Tree[] expressionTrees = new Tree[separatedExpressions.length];
        Node node;
        for (int i = 0; i < separatedExpressions.length; i++) {
            System.out.println("Expresion: " + separatedExpressions[i]);
            separatedExpressions[i] = convertNotation(separatedExpressions[i]);
            expressionTrees[i] = new Tree();
            node = expressionTrees[i].buildTree(separatedExpressions[i].toCharArray());
            System.out.println(expressionTrees[i].traverse(node));
            expressionTrees[i].traverse(node);
            expressionTrees[i].setRootNode(node);
        }
        generateQuadruplets(expressionTrees, separatedExpressions);
    }

    private String convertNotation(String expresion) { //TODO: Ordenar esto.
        expresion += ")";
        String resultado = "";
        Stack<Character> pila = new Stack<>();
        pila.push('(');
        for (int i = 0; i < expresion.length(); i++) {
            char actual = expresion.charAt(i);
            if (operationPrecedence(actual) > 0) {
                while (!pila.isEmpty() && operationPrecedence(pila.peek()) >= operationPrecedence(actual)) {
                    resultado += pila.pop();
                }
                pila.push(actual);
            } else if (actual == ')') {
                char auxiliar = pila.pop();
                while (auxiliar != '(') {
                    resultado += auxiliar;
                    auxiliar = pila.pop();
                }
            } else if (actual == '(') {
                pila.push(actual);
            } else {
                resultado += actual;
            }
        }
        if (!pila.isEmpty()) {
            for (int i = 0; i <= pila.size(); i++) {
                resultado += pila.pop();
            }
        }
        return resultado;
    }

    private ArrayList<Token> extractExpressions() {
        Predicate<Token> porExpresion = t -> t.getExpression() != null;
        var lista = tokens.stream().filter(porExpresion).collect(Collectors.toList());
        return (ArrayList<Token>) lista;
    }

    private String[] separateExpressions(ArrayList<Token> lista) {
        ArrayList<String> expresiones = new ArrayList<>();
        String etiquetaActual = lista.get(0).getExpression();
        String expresion = "";
        int totalExpresiones = 0;
        for (Token t : lista) {
            if (etiquetaActual.equals(t.getExpression())) {
                expresion += t.getSymbol();
            } else {
                expresiones.add(expresion);
                etiquetaActual = t.getExpression();
                expresion = "" + t.getSymbol();
                totalExpresiones++;
            }
        }
        expresiones.add(expresion);
        return expresiones.toArray(new String[totalExpresiones]);
    }

    private void generateQuadruplets(Tree[] arboles, String[] expresiones) {
        quadruplets = new ArrayList<>();
        String identificador;
        int contador;
        System.out.println(arboles.length);
        for (int i = 0; i < arboles.length; i++) {
            Tree abb = arboles[i];
            contador = 1;
            identificador = expresiones[i].split("=")[0];
            for (Quadruplet cuad : abb.getQuadruplets()) {
                cuad.setTag("Temporal " + contador);
                if (contador > 1)
                    cuad.setRightSideOperand("Temporal " + (contador - 1));
                cuad.setIdentifier(identificador);
                quadruplets.add(cuad);
                contador++;
            }
        }
    }

    private int operationPrecedence(char c) {
        switch (c) {
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
        }
        return -1;
    }

    public ArrayList<Quadruplet> getQuadruplets() {
        return quadruplets;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }
}
