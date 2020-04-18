package main;

import java.util.ArrayList;
import java.util.Hashtable;

public class Controlador {
	private String Codigo;
	private String errores;
	private ArrayList<String> tokens;
	private boolean encontroErrores = false;

	public Controlador(String codigo) {
		Codigo = codigo;
		errores = "";
	}

	public void iniciar() {
		lexico();        //TODO: Implementar el llenado de la tabla de símbolos con los identificadores obtenidos
		sintactico();
		semantico();    //TODO: Implementar analizador semántico.
	}

	private void lexico() {
		Lexer analizador = new Lexer(Codigo);
		analizador.analizar();

		if (!analizador.analisis_exitoso()) {
			encontroErrores = true;
			errores += analizador.mensaje_error() + "\n";
		} else {
			tokens = analizador.getTokens();
		}
	}

	private void sintactico() {
		Parser sintactico = new Parser();
		sintactico.motorSintactico(tokens);
		System.out.println("\n" + sintactico.getSalida());
	}

	private void semantico() {
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