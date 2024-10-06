package com.compiler.model.dto;

import com.compiler.model.structures.Token;
import java.util.HashMap;
import java.util.List;

public class SemanticAnalyzerResults {
    private HashMap<String, Token> tableData;
    private List<Token> tokenList;

    public SemanticAnalyzerResults(HashMap<String, Token> tableData, List<Token> tokenList) {
        this.tableData = tableData;
        this.tokenList = tokenList;
    }

    public HashMap<String, Token> getTableData() {
        return tableData;
    }

    public void setTableData(HashMap<String, Token> tableData) {
        this.tableData = tableData;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }
}
