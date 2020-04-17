package main;

import java.util.Hashtable;

public class Controlador {
	private String Codigo;
	private String errores;
	private boolean encontroErrores = false;

	public Controlador(String codigo) {
		Codigo = codigo;
		errores = "";
	}

	public void iniciar() {
		lexico();
		sintactico();
	}

	private void lexico() {
		Lexer analizador = new Lexer(Codigo);
		analizador.analizar();

		if (!analizador.analisis_exitoso()) {
			encontroErrores = true;
			errores += analizador.mensaje_error() + "\n";
		}
	}

	private void sintactico() {
		encontroErrores = true;
		errores += "Por aquí pasó un caballo con sus patas al revés\n";
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