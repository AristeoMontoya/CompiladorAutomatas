package main;

import java.util.ArrayList;
import java.util.Hashtable;

public class Controlador {
	private String Codigo;
	private String errores;
	private ArrayList<Token> tokens;
	private boolean encontroErrores = false;

	public Controlador(String codigo) {
		Codigo = codigo;
		errores = "";
	}

	public void iniciar() {
		lexico();            //TODO: Implementar el llenado de la tabla de símbolos con los identificadores obtenidos
		sintactico();        //TODO: Rehacer esto. Pero después
		semantico();         //TODO: Implementar analizador semántico.
	}

	private void lexico() {
		Lexer analizador = new Lexer(Codigo);
		if (!analizador.analizar()) {
			encontroErrores = true;
			errores += analizador.mensajeError() + "\n";
		} else {
			tokens = analizador.getTokens();
		}
	}

	private void sintactico() {
		Parser sintactico = new Parser(tokens);
		if (!sintactico.motorSintactico()) {
			encontroErrores = true;
			errores += sintactico.getSalida() + "\n";
		} else {
			tokens = sintactico.getListaTokens();
		}
		for (Token aux : tokens) {
			System.out.println("Simbolo: " + aux.getSimbolo() + "\tValor: " + aux.getValor() + "\tTipo de dato: " + aux.getTipoDato() + "\tPosicion: " + aux.getLinea());
		}
	}

	private void semantico() {
		Semantico analizador = new Semantico(tokens);
		analizador.comenzarAnalisis();
	}

	private void codigoIntermedio() {

	}

	private void optimizacion() {

	}

	private void codigoObjeto() {

	}

	public Hashtable getTabla() {
		return null;
	}

	public String getErrores() {
		return errores;
	}

	public boolean encontroErrores() {
		return encontroErrores;
	}
}