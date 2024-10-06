package com.compiler.model.dto;

import com.compiler.model.structures.Token;
import java.util.List;

/**
 * Holds the resulting {@link Token} list from lexical analysis
 */
public class LexicalAnalyzerResults {
    private List<Token> tokenList;

    public LexicalAnalyzerResults(List<Token> tokens) {
        this.tokenList = tokens;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }
}
