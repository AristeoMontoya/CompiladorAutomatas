package com.compiler.process;

import com.compiler.model.CodigoIntermedio;
import com.compiler.model.Cuadruplo;
import com.compiler.model.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Controlador {
    private String Codigo;
    private String errores;
    private ArrayList<String[]> tabla;
    private ArrayList<Token> tokens;
    private String cuadruplos;

    public Controlador(String codigo) {
        Codigo = codigo;
        errores = "";
        tabla = new ArrayList<>();
    }

    public boolean iniciar() {
        lexico();
        sintactico();        //TODO: Rehacer esto. Pero después
        semantico();         //TODO: Hacer esto correctamente. Después también
        codigoIntermedio();
        return errores.isEmpty();
    }

    private void lexico() {
        Lexer analizador = new Lexer(Codigo);
        if (!analizador.analizar()) {
            errores += analizador.mensajeError() + "\n";
        } else {
            tokens = analizador.getTokens();
        }
    }

    private void sintactico() {
        Parser sintactico = new Parser(tokens);
        if (!sintactico.motorSintactico()) {
            errores += sintactico.getSalida() + "\n";
        } else {
            tokens = sintactico.getListaTokens();
        }
    }

    private void semantico() {
        Semantico analizador = new Semantico(tokens);
        if (!analizador.comenzarAnalisis()) {
            errores += analizador.getErrores();
        }
        generarTabla(analizador.getTabla());
    }

    private void codigoIntermedio() {
        CodigoIntermedio intermedio = new CodigoIntermedio(tokens);
        formatoCuadruplos(intermedio.getListaCuadruplos());
    }

    private void optimizacion() {
        //TODO Por implementar
    }

    private void codigoObjeto() {
        //TODO Por implementar
    }

    private void generarTabla(HashMap<String, Token> tablaAux) {
        Collection<Token> registros = tablaAux.values();
        for (Token t : registros) {
            String[] registro = new String[5];
            registro[0] = t.getSimbolo();
            registro[1] = t.getValor();
            registro[2] = t.getTipoDato();
            registro[3] = "" + t.getLinea();
            registro[4] = t.getAlcance();
            tabla.add(registro);
        }
    }

    public ArrayList<String[]> getTabla() {
        return tabla;
    }

    private void formatoCuadruplos(ArrayList<Cuadruplo> listaCuadruplos) {
        String identificador = null;
        if (listaCuadruplos.get(0).getIdentificador() != null)
            identificador = listaCuadruplos.get(0).getIdentificador();
        int conteo = 1;
        if (listaCuadruplos.size() > 1)
            cuadruplos = "Cuadruplo 1";
        for (int i = 0; i < listaCuadruplos.size(); i++) {
            Cuadruplo cuad = listaCuadruplos.get(i);
            if (identificador.equals(cuad.getIdentificador())) {
                cuadruplos += "\n" + cuad.getFormato();
            } else {
                conteo++;
                cuadruplos += "Resultado de " + listaCuadruplos.get(i - 1).getIdentificador() + " = " + listaCuadruplos.get(i - 1).getResultado() + "\nCuadruplo " + conteo;
                cuadruplos += "\n" + cuad.getFormato();
            }
        }
        cuadruplos += "\nResultado de " + listaCuadruplos.get(listaCuadruplos.size() - 1).getIdentificador() + " = " + listaCuadruplos.get(listaCuadruplos.size() - 1).getResultado();
    }

    public String getCuadruplos() {
        return cuadruplos;
    }

    public String getErrores() {
        return errores;
    }
}
