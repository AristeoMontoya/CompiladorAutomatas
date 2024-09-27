package com.compiler.model;

public class Token {
    private String simbolo;
    private String valor;
    private String tipoDato;
    private Gramatica tipoToken;
    private String alcance;
    private String operacion;
    private String expresion;
    private int linea;

    public Token(String simbolo, Gramatica tipoToken, int linea) {
        this.simbolo = simbolo;
        this.tipoToken = tipoToken;
        this.linea = linea;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Gramatica getTipoToken() {
        return tipoToken;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}
