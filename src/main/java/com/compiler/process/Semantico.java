package com.compiler.process;

import com.compiler.model.Gramatica;
import com.compiler.model.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Semantico {
    HashMap<String, Token> tabla = new HashMap<>();
    private ArrayList<Token> listaTokens;
    private String errores;
    private int indice = 0;
    private int bloque = 0;

    public Semantico(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
        errores = "";
    }

    public boolean comenzarAnalisis() {
        for (Token t : listaTokens) {
            if (t.getSimbolo().equals("{"))
                bloque++;
            else if (t.getSimbolo().equals("}"))
                bloque--;
            if (t.getOperacion() != null) {
                if (t.getTipoToken() == Gramatica.Identificador && !t.getOperacion().equals("clase")) {
                    if (t.getOperacion().equals("declaracion")) {
                        insertarDeclaracion(t);
                    } else if (t.getOperacion().equals("asignacion") || t.getOperacion().equals("expresion")) {
                        validarOperacion(t);
                    }
                    validarTipodeDatos(t);
                }
            }
            indice++;
        }
        return errores.isEmpty();
    }

    private void validarOperacion(Token t) {
        if (!validarDeclaracion(t))
            return;
        Token aux = tabla.get(t.getSimbolo());
        Gramatica operador = listaTokens.get(indice + 1).getTipoToken();
        if (aux.getTipoDato().equals("Entero")
                && (operador != Gramatica.Simbolos_de_evaluacion && operador != Gramatica.Asignacion)) {
            errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: "
                    + t.getLinea() + "\n";
        } else if (aux.getTipoDato().equals("Booleano") && operador != Gramatica.Operadores_logicos
                && operador != Gramatica.Asignacion) {
            errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: "
                    + t.getLinea() + "\n";
        }
    }

    private void validarTipodeDatos(Token t) {
        if (t.getOperacion().equals("expresion"))
            return;
        if (!validarDeclaracion(t))
            return;
        Token aux = tabla.get(t.getSimbolo());
        Gramatica tipo = getTipoDato(t.getValor());
        if (aux.getTipoDato().equals("Entero") && tipo != Gramatica.Entero_literal) {
            errores += "La variable " + t.getSimbolo() + " es de tipo entero y recibe el valor \"" + t.getValor()
                    + "\" en la línea: " + t.getLinea() + "\n";
        } else if (aux.getTipoDato().equals("Booleano") && tipo != Gramatica.Booleano_literal) {
            errores += "La variable " + t.getSimbolo() + " es de tipo booleano y recibe el valor \"" + t.getValor()
                    + "\" en la línea: " + t.getLinea() + "\n";
        }
    }

    private boolean validarDeclaracion(Token t) {
        if (!tabla.containsKey(t.getSimbolo())) {
            errores += "La variable \"" + t.getSimbolo() + "\" no se encuentra declarada. Fue usada en la línea: "
                    + t.getLinea() + "\n";
            return false;
        }
        return true;
    }

    private void insertarDeclaracion(Token t) {
        if (!tabla.containsKey(t.getSimbolo())) {
            t.setAlcance("B" + bloque);
            tabla.put(t.getSimbolo(), t);
        } else {
            errores += "La variable " + t.getSimbolo() + " ya se encuentra declarada en la línea "
                    + tabla.get(t.getSimbolo()).getLinea() + " y se declaró de nuevo en la línea " + t.getLinea()
                    + "\n";
        }
    }

    private Gramatica getTipoDato(String entrada) {
        for (Gramatica t : Gramatica.values()) {
            int fin = t.finalCoincidencias(entrada);
            if (fin != -1) {
                return t;
            }
        }
        return null;
    }

    public HashMap<String, Token> getTabla() {
        return tabla;
    }

    public String getErrores() {
        return errores;
    }
}
