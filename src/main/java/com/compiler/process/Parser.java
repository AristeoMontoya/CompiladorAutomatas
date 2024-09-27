package com.compiler.process;

import com.compiler.model.Gramatica;
import com.compiler.model.Token;

import java.util.ArrayList;

public class Parser {
    private ArrayList<Token> listaTokens;
    private Gramatica token;
    private String lexemaActual;
    private String salida;
    private String especificador;
    private int lineaActual;
    private int indiceIdentificador;
    private int indice;
    private int contadorExpresiones = 0;
    private boolean banderaExpersiones = false;

    public Parser(ArrayList<Token> listaTokens) {    //TODO: Rehacer todo esto. O mínimo ordenar el código.
        this.listaTokens = listaTokens;
    }

    public boolean motorSintactico() {
        salida = "";
        indice = 0;
        try {
            Token aux = listaTokens.get(indice);
            token = aux.getTipoToken();
            lexemaActual = aux.getSimbolo();
            lineaActual = 1;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            salida = "Código vacío.";
            return false;
        }
        declaracionClase();
        return salida.isEmpty();
    }

    private void acomodar(Gramatica token, String lexema) {
        if (this.token == token && lexema.equals(this.lexemaActual)) {
            avanza();
        } else {
            error(lexema, listaTokens.get(indice).getLinea());
        }
    }

    private void avanza() {
        if (indice < listaTokens.size() - 1) {
            indice++;
        }
        try {
            Token aux = listaTokens.get(indice);
            token = aux.getTipoToken();
            lexemaActual = aux.getSimbolo();
            lineaActual = aux.getLinea();
        } catch (IndexOutOfBoundsException e) {
            token = null;
        }
    }

    private void expresionAritmetica() {
        banderaExpersiones = true;
        contadorExpresiones++;
        indiceIdentificador = indice;
        int indiceAux = indice;
        actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
        identificador();
        indiceIdentificador++;
        actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
        acomodar(Gramatica.Asignacion, "=");
        indiceIdentificador++;
        actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
        if (token == Gramatica.Entero_literal) {
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), "asignacion");
            integerLiteral();
        } else if (token == Gramatica.Booleano_literal) {
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), "asignacion");
            booleanLiteral();
        } else if (token == Gramatica.Identificador) {
            actualizarToken(lexemaActual, listaTokens.get(indiceAux).getTipoDato(), "asignacion");
            identificador();
        }
        indiceIdentificador++;
        while (token != Gramatica.Simbolos_especiales) {
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
            indiceIdentificador++;
            if (token == Gramatica.Operadores_aritmeticos) {
                avanza();
            } else if (token == Gramatica.Operadores_logicos) {
                avanza();
            } else {
                error("arit", lineaActual);
                break;
            }
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
            indiceIdentificador++;
            if (token == Gramatica.Entero_literal) {
                integerLiteral();
            } else if (token == Gramatica.Booleano_literal) {
                booleanLiteral();
            } else if (token == Gramatica.Identificador) {
                identificador();
            }
        }
        banderaExpersiones = false;
        indiceIdentificador = indiceAux;
        acomodar(Gramatica.Simbolos_especiales, ";");
    }

    private void booleanLiteral() {
        avanza();
    }

    private void declaracionClase() {
        if (token != Gramatica.Declaracion_clase) {
            modificador();
        }
        acomodar(Gramatica.Declaracion_clase, "class");
        indiceIdentificador = indice;
        actualizarToken(lexemaActual, "Identificador de clase", "clase");
        identificador();
        acomodar(Gramatica.Simbolos_especiales, "{");
        fieldDeclaration();
        statement();
        acomodar(Gramatica.Simbolos_especiales, "}");
    }

    private void expression() {
        testingExpression();
    }

    private void error(String esperado, int linea) {
        salida += "Error sintáctico en la línea " + linea + ": Se esperaba " + esperado + ", se encontró \"" + lexemaActual + "\".\n";
    }

    private void fieldDeclaration() {
        if (token == Gramatica.Modificador || token == Gramatica.Especificador) {
            variableDeclaration();
            acomodar(Gramatica.Simbolos_especiales, ";");
            fieldDeclaration();
        }
    }

    public String getSalida() {
        return salida;
    }

    private void ifStatement() {
        acomodar(Gramatica.Simbolos_especiales, "(");
        expression();
        acomodar(Gramatica.Simbolos_especiales, ")");
        acomodar(Gramatica.Simbolos_especiales, "{");
        expresionAritmetica();
        statement();
        acomodar(Gramatica.Simbolos_especiales, "}");
    }

    private void integerLiteral() {
        acomodar(Gramatica.Entero_literal, lexemaActual);
    }

    private void identificador() {
        acomodar(Gramatica.Identificador, lexemaActual);
    }

    private void modificador() {
        if (token == Gramatica.Modificador) {
            avanza();
        } else {
            error("", listaTokens.get(indice).getLinea());
        }
    }

    private void statement() {
        if (token == Gramatica.If) {
            avanza();
            ifStatement();
        } else if (token == Gramatica.While) {
            avanza();
            whileStatement();
        } else if (token == Gramatica.Modificador || token == Gramatica.Especificador) {
            variableDeclaration();
            acomodar(Gramatica.Simbolos_especiales, ";");
        }
    }

    private void testingExpression() {
        if (token == Gramatica.Identificador) {
            indiceIdentificador = indice;
            actualizarToken(lexemaActual, "", "expresion");
            identificador();
        } else if (token == Gramatica.Entero_literal) {
            integerLiteral();
        } else if (token == Gramatica.Booleano_literal) {
            booleanLiteral();
        } else
            error("", lineaActual);

        if (token == Gramatica.Simbolos_de_evaluacion) {
            avanza();
        } else if (token == Gramatica.Operadores_logicos) {
            avanza();
        } else
            error("Testing expression", lineaActual);

        if (token == Gramatica.Entero_literal) {
            integerLiteral();
        } else if (token == Gramatica.Booleano_literal) {
            booleanLiteral();
        } else if (token == Gramatica.Identificador) {
            identificador();
        } else
            error("componente", lineaActual);
    }

    private void epecificadorTipo() {
        if (token == Gramatica.Especificador || token == Gramatica.Identificador) {
            avanza();
        } else {
            error(Gramatica.Especificador.toString(), lineaActual);
        }
    }

    private void variableDeclaration() {
        if (token == Gramatica.Modificador) {
            modificador();
        }
        especificador = lexemaActual.equals("int") ? "Entero" : "Booleano";
        epecificadorTipo();
        indiceIdentificador = indice;    //FIXME: NO MOVER
        if (especificador.equals("Entero")) {
            banderaExpersiones = true;
        }
        identificador();
        int indiceAuxiliar = indiceIdentificador;
        if (banderaExpersiones)
            indiceIdentificador++;
        if (token == Gramatica.Asignacion) {
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
            avanza();
            indiceIdentificador++;
            actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), null);
            indiceIdentificador = indiceAuxiliar;
            variableDeclarator();
        }
        if (banderaExpersiones)
            contadorExpresiones++;
        banderaExpersiones = false;
    }

    private void variableDeclarator() {
        if (token == Gramatica.Entero_literal) {
            actualizarToken(lexemaActual, "Entero", "declaracion");
            integerLiteral();
        } else if (token == Gramatica.Booleano_literal) {
            actualizarToken(lexemaActual, "Booleano", "declaracion");
            booleanLiteral();
        } else {
            error("", lineaActual);
        }
    }

    private void whileStatement() {
        acomodar(Gramatica.Simbolos_especiales, "(");
        expression();
        acomodar(Gramatica.Simbolos_especiales, ")");
        acomodar(Gramatica.Simbolos_especiales, "{");
        statement();
        acomodar(Gramatica.Simbolos_especiales, "}");
    }

    public void actualizarToken(String valor, String tipodeDato, String operacion) {
        Token aux = listaTokens.get(indiceIdentificador);
        if (operacion != null)
            aux.setTipoDato(operacion.equals("declaracion") ? especificador : tipodeDato);
        aux.setValor(valor);
        aux.setOperacion(operacion);
        if (banderaExpersiones) {
            aux.setExpresion("E" + contadorExpresiones);
        }
        listaTokens.set(indiceIdentificador, aux);
    }

    public ArrayList<Token> getListaTokens() {
        return listaTokens;
    }
}