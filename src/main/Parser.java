package main;

import java.util.ArrayList;

public class Parser {
	public static String salida2;
	private ArrayList<String> tokens;
	private String componente;
	private Gramatica tipo;
	private String salida;
	private int indice;

	public Parser(ArrayList<String> listaTokens) {
		tokens = listaTokens;
	}

	public boolean motorSintactico() {
		salida = "";
		indice = 0;
		try {
			componente = tokens.get(0);
			tipo = getTipo(componente);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			salida = "Código vacío.";
			return false;
		}
		declaracionClase();
		return salida.isEmpty();
	}

	private void acomodar(Gramatica token, String lexema) {
		if (getTipo(componente) == token && componente.equals(lexema)) {
			avanza();
		} else {
			error(getTipo(componente), lexema);
		}
	}

	private void avanza() {
		if (indice < tokens.size() - 1) {
			indice++;
		}
		try {
			componente = tokens.get(indice);
			tipo = getTipo(componente);
		} catch (IndexOutOfBoundsException e) {
			indice--;
		}
	}

	private void expresionAritmetica() {
		identificador();
		acomodar(Gramatica.Asignacion, "=");
		if (tipo == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (tipo == Gramatica.Identificador) {
			identificador();
		}

		while (!componente.equals(";")) {
			if (tipo == Gramatica.Operadores_aritmeticos) {
				avanza();
			} else {
				error(Gramatica.Operadores_aritmeticos, "arit");
				break;
			}

			if (tipo == Gramatica.Entero_literal) {
				integerLiteral();
			} else if (tipo == Gramatica.Identificador) {
				identificador();
			}
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
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaban operadores aritméticos\n";
				break;
			case Asignacion:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba \"=\"\n";
				break;
			case Modificador:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba modificador\n";
				break;
			case Entero_literal:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba número entero\n";
				break;
			case Booleano_literal:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba booleano\n";
				break;
			case Identificador:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba identificador\n";
				break;
			case Declaracion_clase:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba Class\n";
				break;
			case Especificador:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba Especificador\n";
				break;
			case If:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba IF\n";
				break;
			case Simbolos_de_evaluacion:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba algún símbolo de evaluación\n";
				break;
			case While:
				salida += "Error Sintactico, se encontró \"" + componente + "\", se esperaba while\n";
				break;
			default:
				break;
		}
		salida2 += "Token obtenido:" + componente + "\n" + "Token Esperado: " + to + "\n-------------------------------------------\n";

	}

	private void fieldDeclaration() {
		if (tipo == Gramatica.Modificador || tipo == Gramatica.Especificador) {
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
		acomodar(Gramatica.Entero_literal, componente);
	}

	private void identificador() {
		acomodar(Gramatica.Identificador, componente);

	}

	private void modificador() {
		if (tipo == Gramatica.Modificador) {
			avanza();
		} else {
			error(Gramatica.Identificador, "");
		}
	}

	private void statement() {
		if (tipo == Gramatica.If) {
			avanza();
			ifStatement();
		} else if (tipo == Gramatica.While) {
			avanza();
			whileStatement();
		} else if (tipo == Gramatica.Modificador || tipo == Gramatica.Especificador) {
			variableDeclaration();
			acomodar(Gramatica.Simbolos_especiales, ";");
		}
	}

	private void testingExpression() {
		if (tipo == Gramatica.Identificador) {
			identificador();
		} else if (tipo == Gramatica.Entero_literal) {
			integerLiteral();
		} else
			error(tipo, componente);

		if (tipo == Gramatica.Simbolos_de_evaluacion) {
			avanza();
		} else {
			error(tipo, componente);
		}

		if (tipo == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (tipo == Gramatica.Identificador) {
			identificador();
		} else
			error(tipo, componente);
	}

	private void epecificadorTipo() {
		if (tipo == Gramatica.Especificador || tipo == Gramatica.Identificador) {
			avanza();
		} else {
			error(tipo, Gramatica.Especificador.toString());
		}
	}

	private void variableDeclaration() {
		if (tipo == Gramatica.Modificador) {
			modificador();
		}
		epecificadorTipo();
		identificador();
		if (componente.equals("=")) {
			avanza();
			variableDeclarator();
		}

	}

	private void variableDeclarator() {
		if (tipo == Gramatica.Entero_literal) {
			integerLiteral();
		} else if (tipo == Gramatica.Booleano_literal) {
			booleanLiteral();
		} else {
			error(tipo, "");
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
			x = t.finalCoincidencias(s);
			if (x != -1) {
				return t;
			}
		}
		return null;
	}
}