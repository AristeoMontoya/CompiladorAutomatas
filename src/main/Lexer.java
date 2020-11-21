package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Lexer {
	private StringBuilder entrada = new StringBuilder();
	private ArrayList<Token> tokens;
	private boolean concluido = false;
	private String mensaje_error = "";
	private Set<Character> caracteresVacios = new HashSet<>();
	private int lineaActual = 1;
	private String[] lineasCodigo;

	public Lexer(String codigo) {
		lineasCodigo = codigo.split("\r\n|\r|\n");
		tokens = new ArrayList<>();

		caracteresVacios.add('\r');
		caracteresVacios.add('\n');
		caracteresVacios.add((char) 8);
		caracteresVacios.add((char) 9);
		caracteresVacios.add((char) 11);
		caracteresVacios.add((char) 12);
		caracteresVacios.add((char) 32);
	}

	public boolean analizar() {
		for (String linea : lineasCodigo) {
			entrada.append(linea);
			while (!concluido)
				continuar();
			if (entrada.length() > 0)
				entrada.delete(0, entrada.length());
			lineaActual += 1;
			concluido = false;
		}
		return mensaje_error.isEmpty();
	}

	private void continuar() {
		if (concluido) {
			return;
		}

		if (entrada.length() == 0) {
			concluido = true;
			return;
		}

		ignorarEspacios();

		if (siguienteToken()) {
			return;
		}

		concluido = true;

		if (entrada.length() > 0) {
			mensaje_error += "Simbolo no esperado: '" + entrada.charAt(0) + "'" + " en la línea " + lineaActual + "\n";
		}
	}

	private void ignorarEspacios() {
		int caracteresABorrar = 0;

		while (entrada.length() > caracteresABorrar && caracteresVacios.contains(entrada.charAt(caracteresABorrar))) {
			caracteresABorrar++;
		}

		if (caracteresABorrar > 0) {
			entrada.delete(0, caracteresABorrar);
		}
	}

	private boolean siguienteToken() {
		for (Gramatica t : Gramatica.values()) {
			int fin = t.finalCoincidencias(entrada.toString());
			if (fin != -1) {
				String lexema = entrada.substring(0, fin);
				entrada.delete(0, fin);
				tokens.add(new Token(lexema, t, lineaActual));
				return true;
			}
		}
		return false;
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public String mensajeError() {
		return mensaje_error;
	}
}
