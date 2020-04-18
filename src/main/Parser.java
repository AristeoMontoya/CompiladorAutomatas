package main;

import java.util.ArrayList;

public class Parser {
	public static String salida2 = "";
	private ArrayList<String> tokens;
	private String componente;
	private String salida = "";
	private int indice = 0;

	public Parser() {	//TODO: Arreglar todo esto.
		tokens = new ArrayList<>();
	}

	private void acomodar(Gramatica token, String s) {
		if (getTipo(componente) == token && componente.equals(s)) {
			avanza();
		} else {
			error(getTipo(componente), s);
		}
	}

	private void avanza() {
		if (indice < tokens.size() - 1) {
			indice++;
		}
		try {
			componente = tokens.get(indice);

		} catch (IndexOutOfBoundsException e) {
			indice--;
		}
	}

	private void expresionAritmetica() {

		String c;

		identificador();

		acomodar(Gramatica.Simbolos_especiales, "=");
		if (getTipo(componente) == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (getTipo(componente) == Gramatica.Identificador) {
			identificador();
		}

		c = componente;
		while (!c.equals(";")) {
			if (getTipo(c) == Gramatica.Operadores_aritmeticos) {
				avanza();
			} else {
				error(Gramatica.Operadores_aritmeticos, "arit");
				break;
			}

			if (getTipo(componente) == Gramatica.Entero_literal) {
				integerLiteral();
			} else if (getTipo(componente) == Gramatica.Identificador) {
				identificador();
			}
			c = componente;
		}

		acomodar(Gramatica.Simbolos_especiales, ";");
	}

	private void booleanLiteral() {
		avanza();
	}

	private void declaracionClase() {
		if (!componente.equals("class")) {
			modificador();
		}

		acomodar(Gramatica.Declaracion_clase, "class");

		identificador();

		acomodar(Gramatica.Simbolos_especiales, "{");

		fieldDeclaration();
		statement();
		acomodar(Gramatica.Simbolos_especiales, "}");
	}

	private void expression() {
		testingExpression();
	}

	private void error(Gramatica t, String to) {
		switch (t) {
			case Simbolos_especiales:
				salida += "Error Sintactico, se esperaba un \"" + to;
				break;
			case Operadores_aritmeticos:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaban operadores aritméticos";
				break;
			case Asignacion:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba \"=\"";
				break;
			case Modificador:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba modificador";
				break;
			case Entero_literal:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba número entero";
				break;
			case Booleano_literal:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba booleano";
				break;
			case Identificador:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba identificador";
				break;
			case Declaracion_clase:
				break;
			case Especificador:
				break;
			case If:
				break;
			case Simbolos_de_evaluacion:
				break;
			case While:
				break;
			default:
				break;
		}
		salida2 += "Token obtenido:" + componente + "\n" + "Token Esperado: " + to + "\n-------------------------------------------\n";

	}

	private void fieldDeclaration() {
		if (getTipo(componente) == Gramatica.Modificador || getTipo(componente) == Gramatica.Especificador) {
			variableDeclaration();
			acomodar(Gramatica.Simbolos_especiales, ";");
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

		acomodar(Gramatica.Simbolos_especiales, "}");

		statement();
	}

	private void integerLiteral() {
		acomodar(Gramatica.Entero_literal, componente);
	}

	private void identificador() {
		acomodar(Gramatica.Identificador, componente);

	}

	private void modificador() {
		if (getTipo(componente) == Gramatica.Modificador) {
			avanza();
		} else {
			error(Gramatica.Identificador, "");
		}
	}

	public String motorSintactico(ArrayList<String> listaTokens) {

		salida = "";
		indice = 0;
		tokens = listaTokens;
		try {
			componente = tokens.get(0);
		} catch (IndexOutOfBoundsException e) {
			salida = "Código vacío.";
		}
		declaracionClase();
		if (salida.equals("")) {
			salida = "No hay errores sintacticos";
		}
		return salida;
	}

	private void statement() {
		String c = componente;
		if (getTipo(c) == Gramatica.If) {
			avanza();
			ifStatement();

		} else if (getTipo(c) == Gramatica.While) {
			avanza();
			whileStatement();
		} else if (getTipo(c) == Gramatica.Modificador || getTipo(c) == Gramatica.Especificador) {
			variableDeclaration();
			acomodar(Gramatica.Simbolos_especiales, ";");
		} else
			error(getTipo(c), "If while, o declaración de variable");
	}

	private void testingExpression() {
		Gramatica t = getTipo(componente);
		if (t == Gramatica.Identificador) {
			identificador();
		} else if (t == Gramatica.Entero_literal) {
			integerLiteral();
		} else
			error(t, componente);

		t = getTipo(componente);

		if (t == Gramatica.Simbolos_de_evaluacion) {
			avanza();
		} else {
			error(t, componente);
		}

		t = getTipo(componente);

		if (t == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (t == Gramatica.Identificador) {
			identificador();
		} else
			error(t, componente);
	}

	private void type() {
		epecificadorTipo();
	}

	private void epecificadorTipo() {
		if (getTipo(componente) == Gramatica.Especificador) {
			avanza();
		} else {
			error(getTipo(componente), Gramatica.Especificador.toString());
		}
	}

	private void variableDeclaration() {

		String c = componente;

		if (getTipo(c) == Gramatica.Modificador) {
			modificador();
		}
		type();
		identificador();

		c = componente;
		if (c.equals("=")) {
			avanza();
			variableDeclarator();
		}

	}

	private void variableDeclarator() {
		String c;
		c = componente;

		if (getTipo(c) == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (getTipo(c) == Gramatica.Booleano_literal) {
			booleanLiteral();
		} else {
			error(getTipo(componente), "");
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

	private Gramatica getTipo(String s) {
		int x;
		for (Gramatica t : Gramatica.values()) {
			x = t.final_coincidencias(s);
			if (x != -1) {
				return t;
			}
		}
		return null;
	}
}