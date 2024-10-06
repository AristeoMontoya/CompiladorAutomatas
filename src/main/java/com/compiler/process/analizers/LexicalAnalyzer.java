package com.compiler.process.analizers;

import com.compiler.model.Grammar;
import com.compiler.model.dto.LexicalAnalyzerResults;
import com.compiler.model.structures.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LexicalAnalyzer implements AnalyzerPipe<String, LexicalAnalyzerResults> {
    private StringBuilder input = new StringBuilder();
    private ArrayList<Token> tokens;
    private boolean isAnalysisDone = false;
    private List<String> errors;
    private Set<Character> emptyChars = new HashSet<>();
    private int currentLine = 1;
    private String[] codeLines;

    /**
     * Prepares the input code by splitting it into lines and
     * removing whitespaces.
     * @param inputCode
     */
    private void initialize(String inputCode) {
        errors = new ArrayList<>();
        tokens = new ArrayList<>();
        codeLines = inputCode.split("\r\n|\r|\n");

        /* TODO: I don't really remember what the characters are.
         *  I need to dig into this.
         */
        emptyChars.addAll(List.of(
                '\r',
                '\n',
                (char) 8,
                (char) 9,
                (char) 11,
                (char) 12,
                (char) 32
        ));
    }

    public LexicalAnalyzerResults runAnalyzer(String inputCode) {
        initialize(inputCode);
        for (String line : codeLines) {
            input.append(line);
            while (!isAnalysisDone)
                analyzeCurrentLine();
            if (input.length() > 0)
                input.delete(0, input.length());
            currentLine += 1;
            isAnalysisDone = false;
        }
        return new LexicalAnalyzerResults(tokens);
    }

    private void analyzeCurrentLine() {
        if (isAnalysisDone) {
            return;
        }

        if (input.length() == 0) {
            isAnalysisDone = true;
            return;
        }

        ignoreWhiteSpaces();

        if (nextToken()) {
            return;
        }

        isAnalysisDone = true;

        if (input.length() > 0) {
            errors.add("Simbolo no esperado: '" + input.charAt(0) + "'" + " en la línea " + currentLine + "\n");
        }
    }

    private void ignoreWhiteSpaces() {
        int caracteresABorrar = 0;

        while (input.length() > caracteresABorrar && emptyChars.contains(input.charAt(caracteresABorrar))) {
            caracteresABorrar++;
        }

        if (caracteresABorrar > 0) {
            input.delete(0, caracteresABorrar);
        }
    }

    private boolean nextToken() {
        for (Grammar t : Grammar.values()) {
            int fin = t.findComponent(input.toString());
            if (fin != -1) {
                String lexema = input.substring(0, fin);
                input.delete(0, fin);
                tokens.add(new Token(lexema, t, currentLine));
                return true;
            }
        }
        return false;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public List<String> getErrors() {
        return errors;
    }
}