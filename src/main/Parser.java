package main;

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

	public Parser(ArrayList<Token> listaTokens) {    //TODO: Agregar método para las asignaciones de datos a los token.
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
		indiceIdentificador = indice;
		identificador();
		acomodar(Gramatica.Asignacion, "=");
		if (token == Gramatica.Entero_literal) {
			actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), "asignacion");
			integerLiteral();
		} else if (token == Gramatica.Booleano_literal) {
			actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), "asignacion");
			booleanLiteral();
		} else if (token == Gramatica.Identificador) {
			actualizarToken(lexemaActual, listaTokens.get(indiceIdentificador).getTipoDato(), "asignacion");
			identificador();
		}

		while (token != Gramatica.Simbolos_especiales) {
			if (token == Gramatica.Operadores_aritmeticos) {
				avanza();
			} else if (token == Gramatica.Operadores_logicos) {
				avanza();
			} else {
				error("arit", lineaActual);
				break;
			}
			if (token == Gramatica.Entero_literal) {
				integerLiteral();
			} else if (token == Gramatica.Booleano_literal) {
				booleanLiteral();
			} else if (token == Gramatica.Identificador) {
				identificador();
			}
		}
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
		indiceIdentificador = indice;
		identificador();
		if (token == Gramatica.Asignacion) {
			avanza();
			variableDeclarator();
		}

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
		aux.setTipoDato(operacion.equals("declaracion") ? especificador : tipodeDato);
		aux.setValor(valor);
		aux.setOperacion(operacion);
		listaTokens.set(indiceIdentificador, aux);
	}

	public ArrayList<Token> getListaTokens() {
		return listaTokens;
	}
}