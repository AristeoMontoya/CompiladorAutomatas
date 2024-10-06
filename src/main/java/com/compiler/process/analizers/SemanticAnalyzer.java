package com.compiler.process.analizers;

import com.compiler.model.Grammar;
import com.compiler.model.dto.LexicalAnalyzerResults;
import com.compiler.model.dto.SemanticAnalyzerResults;
import com.compiler.model.structures.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: This should get a syntax tree and return a syntax tree
 *  I'm first working ont getting it working as it used before, i'll take
 *  of that next.
 */
public class SemanticAnalyzer implements AnalyzerPipe<LexicalAnalyzerResults, SemanticAnalyzerResults> {
    HashMap<String, Token> table = new HashMap<>();
    private ArrayList<Token> tokens;
    private List<String> errors;
    private int index = 0;
    private int block = 0; // TODO: Likely to be scope

    private void initialize(List<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
        errors = new ArrayList<>();
    }

    public SemanticAnalyzerResults runAnalyzer(LexicalAnalyzerResults lexerResults) {
        initialize(lexerResults.getTokenList());
        for (Token t : tokens) {
            if (t.getSymbol().equals("{"))
                block++;
            else if (t.getSymbol().equals("}"))
                block--;
            if (t.getOperation() != null) {
                if (t.getTokenType() == Grammar.IDENTIFIER && !t.getOperation().equals("clase")) {
                    if (t.getOperation().equals("declaracion")) {
                        insertDeclaration(t);
                    } else if (t.getOperation().equals("asignacion") || t.getOperation().equals("expresion")) {
                        validateOperation(t);
                    }
                    validateDataType(t);
                }
            }
            index++;
        }

        return new SemanticAnalyzerResults(table, tokens);
    }

    private void validateOperation(Token token) {
        if (!validateStatement(token))
            return;
        Token aux = table.get(token.getSymbol());
        Grammar operador = tokens.get(index + 1).getTokenType();
        if (aux.getDataType().equals("Entero")
                && (operador != Grammar.EVALUATION_SYMBOL && operador != Grammar.ASSIGNATION)) {
            errors.add("Se usaron operadores incorrectos para la variable " + token.getSymbol() + " en la línea: "
                    + token.getLine() + "\n");
        } else if (aux.getDataType().equals("Booleano") && operador != Grammar.LOGIC_OPERATOR
                && operador != Grammar.ASSIGNATION) {
            errors.add("Se usaron operadores incorrectos para la variable " + token.getSymbol() + " en la línea: "
                    + token.getLine() + "\n");
        }
    }

    private void validateDataType(Token t) {
        if (t.getOperation().equals("expresion"))
            return;
        if (!validateStatement(t))
            return;
        Token aux = table.get(t.getSymbol());
        Grammar tipo = getDataType(t.getValue());
        if (aux.getDataType().equals("Entero") && tipo != Grammar.LITERAL_INTEGER) {
            errors.add("La variable " + t.getSymbol() + " es de tipo entero y recibe el valor \"" + t.getValue()
                    + "\" en la línea: " + t.getLine() + "\n");
        } else if (aux.getDataType().equals("Booleano") && tipo != Grammar.LITERAL_BOOLEAN) {
            errors.add("La variable " + t.getSymbol() + " es de tipo booleano y recibe el valor \"" + t.getValue()
                    + "\" en la línea: " + t.getLine() + "\n");
        }
    }

    // TODO: Invert this
    private boolean validateStatement(Token t) {
        if (!table.containsKey(t.getSymbol())) {
            errors.add("La variable \"" + t.getSymbol() + "\" no se encuentra declarada. Fue usada en la línea: "
                    + t.getLine() + "\n");
            return false;
        }
        return true;
    }

    private void insertDeclaration(Token t) {
        if (!table.containsKey(t.getSymbol())) {
            t.setScope("B" + block);
            table.put(t.getSymbol(), t);
        } else {
            errors.add("La variable " + t.getSymbol() + " ya se encuentra declarada en la línea "
                    + table.get(t.getSymbol()).getLine() + " y se declaró de nuevo en la línea " + t.getLine()
                    + "\n");
        }
    }

    private Grammar getDataType(String entrada) {
        for (Grammar t : Grammar.values()) {
            int fin = t.findComponent(entrada);
            if (fin != -1) {
                return t;
            }
        }
        return null;
    }

    public List<String> getErrors() {
        return errors;
    }
}
