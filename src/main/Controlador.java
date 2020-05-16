package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Controlador {
	private String Codigo;
	private String errores;
	private ArrayList<String[]> tabla;
	private ArrayList<Token> tokens;

	public Controlador(String codigo) {
		Codigo = codigo;
		errores = "";
		tabla = new ArrayList<>();
	}

	public boolean iniciar() {
		lexico();
		sintactico();        //TODO: Rehacer esto. Pero después
		semantico();         //TODO: Hacer esto correctamente. Después también
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
		for (Token t : tokens) {
			System.out.println(t.getSimbolo() + " : " + t.getExpresion());
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

	}

	private void optimizacion() {

	}

	private void codigoObjeto() {

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

	public String getErrores() {
		return errores;
	}
}