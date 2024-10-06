package com.compiler.process.analizers;

import com.compiler.model.Grammar;
import com.compiler.model.dto.LexicalAnalyzerResults;
import com.compiler.model.structures.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs syntactical analysis on the input token list.
 * Ideally this should return a syntax tree, I'll take care of that in the future.
 */
public class SyntacticAnalyzer implements AnalyzerPipe<LexicalAnalyzerResults, LexicalAnalyzerResults> {
    private ArrayList<Token> tokens;
    private Grammar token;
    private String currentLexeme;
    private String output;
    private String specifier;
    private int currentLine;
    private int identifierIndex;
    private int index;
    private int expressionCount = 0;
    // TODO: Figure out what this boolean does and set a more descriptive name
    private boolean expressionFlag = false;
    private List<String> errors;

    public LexicalAnalyzerResults runAnalyzer(LexicalAnalyzerResults lexerResults) {
        this.tokens = (ArrayList<Token>) lexerResults.getTokenList();
        this.errors = new ArrayList<>();
        output = "";
        index = 0;
        try {
            Token aux = tokens.get(index);
            token = aux.getTokenType();
            currentLexeme = aux.getSymbol();
            currentLine = 1;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new EmptyCodeException("Código vacío");
        }
        handleClassDeclaration();
        return new LexicalAnalyzerResults(tokens);
    }

    // TODO: Figure out what this method actually does
    private void arrange(Grammar token, String lexeme) {
        if (this.token == token && lexeme.equals(this.currentLexeme)) {
            continueProcess();
        } else {
            appendErrorMessage(lexeme, tokens.get(index).getLine());
        }
    }

    private void continueProcess() {
        if (index < tokens.size() - 1) {
            index++;
        }
        try {
            Token aux = tokens.get(index);
            token = aux.getTokenType();
            currentLexeme = aux.getSymbol();
            currentLine = aux.getLine();
        } catch (IndexOutOfBoundsException e) {
            token = null;
        }
    }

    private void handleArithmeticExpression() {
        expressionFlag = true;
        expressionCount++;
        identifierIndex = index;
        int indiceAux = index;
        updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
        handleIdentifier();
        identifierIndex++;
        updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
        arrange(Grammar.ASSIGNATION, "=");
        identifierIndex++;
        updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
        if (token == Grammar.LITERAL_INTEGER) {
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), "asignacion");
            handleIntegerLiteral();
        } else if (token == Grammar.LITERAL_BOOLEAN) {
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), "asignacion");
            handleLiteralBoolean();
        } else if (token == Grammar.IDENTIFIER) {
            updateToken(currentLexeme, tokens.get(indiceAux).getDataType(), "asignacion");
            handleIdentifier();
        }
        identifierIndex++;
        while (token != Grammar.SPECIAL_SYMBOL) {
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
            identifierIndex++;
            if (token == Grammar.ARITHMETICAL_OPERATOR) {
                continueProcess();
            } else if (token == Grammar.LOGIC_OPERATOR) {
                continueProcess();
            } else {
                appendErrorMessage("arit", currentLine);
                break;
            }
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
            identifierIndex++;
            if (token == Grammar.LITERAL_INTEGER) {
                handleIntegerLiteral();
            } else if (token == Grammar.LITERAL_BOOLEAN) {
                handleLiteralBoolean();
            } else if (token == Grammar.IDENTIFIER) {
                handleIdentifier();
            }
        }
        expressionFlag = false;
        identifierIndex = indiceAux;
        arrange(Grammar.SPECIAL_SYMBOL, ";");
    }

    private void handleLiteralBoolean() {
        continueProcess();
    }

    private void handleClassDeclaration() {
        if (token != Grammar.CLASS_DECLARATION) {
            handleModifier();
        }
        arrange(Grammar.CLASS_DECLARATION, "class");
        identifierIndex = index;
        updateToken(currentLexeme, "Identificador de clase", "clase");
        handleIdentifier();
        arrange(Grammar.SPECIAL_SYMBOL, "{");
        handleFieldDeclaration();
        handleStatement();
        arrange(Grammar.SPECIAL_SYMBOL, "}");
    }

    private void handleExpression() {
        testExpression();
    }

    private void appendErrorMessage(String expected, int line) {
        output += "Error sintáctico en la línea " + line + ": Se esperaba " + expected + ", se encontró \"" + currentLexeme + "\".\n";
    }

    private void handleFieldDeclaration() {
        if (token == Grammar.ACCESS_MODIFIER || token == Grammar.SPECIFIER) {
            handleVariableDeclaration();
            arrange(Grammar.SPECIAL_SYMBOL, ";");
            handleFieldDeclaration();
        }
    }

    public String getOutput() {
        return output;
    }

    private void handleIfStatement() {
        arrange(Grammar.SPECIAL_SYMBOL, "(");
        handleExpression();
        arrange(Grammar.SPECIAL_SYMBOL, ")");
        arrange(Grammar.SPECIAL_SYMBOL, "{");
        handleArithmeticExpression();
        handleStatement();
        arrange(Grammar.SPECIAL_SYMBOL, "}");
    }

    private void handleIntegerLiteral() {
        arrange(Grammar.LITERAL_INTEGER, currentLexeme);
    }

    private void handleIdentifier() {
        arrange(Grammar.IDENTIFIER, currentLexeme);
    }

    private void handleModifier() {
        if (token == Grammar.ACCESS_MODIFIER) {
            continueProcess();
        } else {
            appendErrorMessage("", tokens.get(index).getLine());
        }
    }

    private void handleStatement() {
        if (token == Grammar.IF_STATEMENT) {
            continueProcess();
            handleIfStatement();
        } else if (token == Grammar.WHILE_STATEMENT) {
            continueProcess();
            handleWhileStatement();
        } else if (token == Grammar.ACCESS_MODIFIER || token == Grammar.SPECIFIER) {
            handleVariableDeclaration();
            arrange(Grammar.SPECIAL_SYMBOL, ";");
        }
    }

    private void testExpression() {
        if (token == Grammar.IDENTIFIER) {
            identifierIndex = index;
            updateToken(currentLexeme, "", "expresion");
            handleIdentifier();
        } else if (token == Grammar.LITERAL_INTEGER) {
            handleIntegerLiteral();
        } else if (token == Grammar.LITERAL_BOOLEAN) {
            handleLiteralBoolean();
        } else
            appendErrorMessage("", currentLine);

        if (token == Grammar.EVALUATION_SYMBOL) {
            continueProcess();
        } else if (token == Grammar.LOGIC_OPERATOR) {
            continueProcess();
        } else
            appendErrorMessage("Testing expression", currentLine);

        if (token == Grammar.LITERAL_INTEGER) {
            handleIntegerLiteral();
        } else if (token == Grammar.LITERAL_BOOLEAN) {
            handleLiteralBoolean();
        } else if (token == Grammar.IDENTIFIER) {
            handleIdentifier();
        } else
            appendErrorMessage("componente", currentLine);
    }

    private void handleTypeSpecifier() {
        if (token == Grammar.SPECIFIER || token == Grammar.IDENTIFIER) {
            continueProcess();
        } else {
            appendErrorMessage(Grammar.SPECIFIER.toString(), currentLine);
        }
    }

    /* TODO: The next two methods have very similar names.
        Make it easier to tell them apart
     */

    private void handleVariableDeclaration() {
        if (token == Grammar.ACCESS_MODIFIER) {
            handleModifier();
        }
        specifier = currentLexeme.equals("int") ? "Entero" : "Booleano";
        handleTypeSpecifier();
        identifierIndex = index;    //FIXME: NO MOVER
        if (specifier.equals("Entero")) {
            expressionFlag = true;
        }
        handleIdentifier();
        int indiceAuxiliar = identifierIndex;
        if (expressionFlag)
            identifierIndex++;
        if (token == Grammar.ASSIGNATION) {
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
            continueProcess();
            identifierIndex++;
            updateToken(currentLexeme, tokens.get(identifierIndex).getDataType(), null);
            identifierIndex = indiceAuxiliar;
            handleVariableDeclarator();
        }
        if (expressionFlag)
            expressionCount++;
        expressionFlag = false;
    }

    private void handleVariableDeclarator() {
        if (token == Grammar.LITERAL_INTEGER) {
            updateToken(currentLexeme, "Entero", "declaracion");
            handleIntegerLiteral();
        } else if (token == Grammar.LITERAL_BOOLEAN) {
            updateToken(currentLexeme, "Booleano", "declaracion");
            handleLiteralBoolean();
        } else {
            appendErrorMessage("", currentLine);
        }
    }

    private void handleWhileStatement() {
        arrange(Grammar.SPECIAL_SYMBOL, "(");
        handleExpression();
        arrange(Grammar.SPECIAL_SYMBOL, ")");
        arrange(Grammar.SPECIAL_SYMBOL, "{");
        handleStatement();
        arrange(Grammar.SPECIAL_SYMBOL, "}");
    }

    public void updateToken(String value, String dataType, String operation) {
        Token resultingToken = tokens.get(identifierIndex);
        if (operation != null)
            resultingToken.setDataType(operation.equals("declaracion") ? specifier : dataType);
        resultingToken.setValue(value);
        resultingToken.setOperation(operation);
        if (expressionFlag) {
            resultingToken.setExpression("E" + expressionCount);
        }
        tokens.set(identifierIndex, resultingToken);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}