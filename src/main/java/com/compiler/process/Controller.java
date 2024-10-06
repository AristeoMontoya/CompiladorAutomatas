package com.compiler.process;

import com.compiler.model.IntermediateCode;
import com.compiler.model.Quadruplet;
import com.compiler.model.Token;

import com.compiler.process.analizers.LexicalAnalyzer;
import com.compiler.process.analizers.SyntacticAnalyzer;
import com.compiler.process.analizers.SemanticAnalyzer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Controller {
    private String inputCode;
    private String errors;
    private ArrayList<String[]> table;
    private ArrayList<Token> tokens;
    private String quadruplets;

    public Controller(String inputCode) {
        this.inputCode = inputCode;
        errors = "";
        table = new ArrayList<>();
    }

    public boolean startProcess() {
        runLexicalAnalyzer();
        runSyntacticAnalyzer();        //TODO: Rehacer esto. Pero después
        runSemanticAnalyzer();         //TODO: Hacer esto correctamente. Después también
        generateIntermediateCode();
        return errors.isEmpty();
    }

    private void runLexicalAnalyzer() {
        LexicalAnalyzer lexer = new LexicalAnalyzer(inputCode);
        if (!lexer.analyze()) {
            errors += lexer.getErrorMessage() + "\n";
        } else {
            tokens = lexer.getTokens();
        }
    }

    private void runSyntacticAnalyzer() {
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(tokens);
        if (!syntacticAnalyzer.analyze()) {
            errors += syntacticAnalyzer.getOutput() + "\n";
        } else {
            tokens = syntacticAnalyzer.getTokens();
        }
    }

    private void runSemanticAnalyzer() {
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(tokens);
        if (!semanticAnalyzer.analyze()) {
            errors += semanticAnalyzer.getErrors();
        }
        generateTable(semanticAnalyzer.getTable());
    }

    private void generateIntermediateCode() {
        IntermediateCode intermediateCode = new IntermediateCode(tokens);
        formatQuadrupletsAsString(intermediateCode.getQuadruplets());
    }

    private void optimizeIntermediateCode() {
        throw new UnsupportedOperationException("Operation not yet implemented");
    }

    private void buildObjectCode() {
        throw new UnsupportedOperationException("Operation not yet implemented");
    }

    private void generateTable(HashMap<String, Token> tempTable) {
        Collection<Token> rows = tempTable.values();
        for (Token t : rows) {
            String[] registry = new String[5];
            registry[0] = t.getSymbol();
            registry[1] = t.getValue();
            registry[2] = t.getDataType();
            registry[3] = "" + t.getLine();
            registry[4] = t.getScope();
            table.add(registry);
        }
    }

    public ArrayList<String[]> getTable() {
        return table;
    }

    private void formatQuadrupletsAsString(ArrayList<Quadruplet> quadruplets) {
        String identifier = null;
        if (quadruplets.get(0).getIdentifier() != null)
            identifier = quadruplets.get(0).getIdentifier();
        int count = 1;
        if (quadruplets.size() > 1)
            this.quadruplets = "Cuadruplo 1";
        for (int i = 0; i < quadruplets.size(); i++) {
            Quadruplet currentQuadruplet = quadruplets.get(i);
            if (identifier.equals(currentQuadruplet.getIdentifier())) {
                this.quadruplets += "\n" + currentQuadruplet.getFormat();
            } else {
                count++;
                this.quadruplets += "Resultado de " + quadruplets.get(i - 1).getIdentifier() + " = " + quadruplets.get(i - 1).getResult() + "\nCuadruplo " + count;
                this.quadruplets += "\n" + currentQuadruplet.getFormat();
            }
        }
        this.quadruplets += "\nResultado de " + quadruplets.get(quadruplets.size() - 1).getIdentifier() + " = " + quadruplets.get(quadruplets.size() - 1).getResult();
    }

    public String getQuadruplets() {
        return quadruplets;
    }

    public String getErrors() {
        return errors;
    }
}
