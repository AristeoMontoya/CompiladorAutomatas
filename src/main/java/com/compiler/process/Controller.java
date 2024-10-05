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
    private String Codigo;
    private String errores;
    private ArrayList<String[]> tabla;
    private ArrayList<Token> tokens;
    private String cuadruplos;

    public Controller(String codigo) {
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
        LexicalAnalyzer analizador = new LexicalAnalyzer(Codigo);
        if (!analizador.analyze()) {
            errores += analizador.getErrorMessage() + "\n";
        } else {
            tokens = analizador.getTokens();
        }
    }

    private void sintactico() {
        SyntacticAnalyzer sintactico = new SyntacticAnalyzer(tokens);
        if (!sintactico.analyze()) {
            errores += sintactico.getOutput() + "\n";
        } else {
            tokens = sintactico.getTokens();
        }
    }

    private void semantico() {
        SemanticAnalyzer analizador = new SemanticAnalyzer(tokens);
        if (!analizador.analyze()) {
            errores += analizador.getErrors();
        }
        generarTabla(analizador.getTable());
    }

    private void codigoIntermedio() {
        IntermediateCode intermedio = new IntermediateCode(tokens);
        formatoCuadruplos(intermedio.getQuadruplets());
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
            registro[0] = t.getSymbol();
            registro[1] = t.getValue();
            registro[2] = t.getDataType();
            registro[3] = "" + t.getLine();
            registro[4] = t.getScope();
            tabla.add(registro);
        }
    }

    public ArrayList<String[]> getTabla() {
        return tabla;
    }

    private void formatoCuadruplos(ArrayList<Quadruplet> listaCuadruplos) {
        String identificador = null;
        if (listaCuadruplos.get(0).getIdentifier() != null)
            identificador = listaCuadruplos.get(0).getIdentifier();
        int conteo = 1;
        if (listaCuadruplos.size() > 1)
            cuadruplos = "Cuadruplo 1";
        for (int i = 0; i < listaCuadruplos.size(); i++) {
            Quadruplet cuad = listaCuadruplos.get(i);
            if (identificador.equals(cuad.getIdentifier())) {
                cuadruplos += "\n" + cuad.getFormat();
            } else {
                conteo++;
                cuadruplos += "Resultado de " + listaCuadruplos.get(i - 1).getIdentifier() + " = " + listaCuadruplos.get(i - 1).getResult() + "\nCuadruplo " + conteo;
                cuadruplos += "\n" + cuad.getFormat();
            }
        }
        cuadruplos += "\nResultado de " + listaCuadruplos.get(listaCuadruplos.size() - 1).getIdentifier() + " = " + listaCuadruplos.get(listaCuadruplos.size() - 1).getResult();
    }

    public String getCuadruplos() {
        return cuadruplos;
    }

    public String getErrores() {
        return errores;
    }
}
