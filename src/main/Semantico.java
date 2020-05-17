package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Semantico {
	HashMap<String, Token> tabla = new HashMap<>();
	private ArrayList<Token> listaTokens;
	private String errores;
	private int indice = 0;
	private int bloque = 0;

	/*	TODO:
	 *		-Crear tabla de símbolos [*]
	 *			Mostrar la tabla de símbolos con las variables declaradas dentro de una clase/método/función
	 *			Mostrar por lo menos los siguientes atributos:
	 *			-Tipo de dato
	 *			-Posición
	 *			-Valor
	 *			-Alcance global/local
	 *		-Validar la asignación de una variable [*]
	 *			Validar que es correcto el tipo de dato asignado a una variable
	 *			Al final del proceso de compilación, en caso de haber errores
	 *			mostrar un mensaje que indique la variable, el tipo de dato de la
	 *			variable, el tipo de dato que se le asignó y la posición del error
	 *		-Validar las variables usadas y no definidas [*]
	 *			Validar que las variables esten declaradas antes de ser utilizadas en el código
	 *			Paara conocer los valores.
	 *			Al finalizar la compilación, en caso de haber erroes mostrar un mensaje
	 *			con la variable y la posición donde se trató de utilizar
	 *		-Validar las variables ya declaradas [*]
	 *			Validar que una variable no se declare más de una vez
	 *			En caso de existir la declaración de una variable más de una vez, mostrar
	 *			un mensaje con la variable y la posición donde se realiza la segunda declaración.
	 *			Además agregar la posición donde se realizó la primera declaración
	 *		-Validar operando de tipos compatibles [*]
	 *			Si se encuentra una expresión con operadores no aptos al contexto
	 *			de tipos de datos usados, mostrar un mensaje con la posición de la
	 *			expresión
	 */
	public Semantico(ArrayList<Token> listaTokens) {
		this.listaTokens = listaTokens;
		errores = "";
	}
	public boolean comenzarAnalisis() {
		for (Token t : listaTokens) {
			if (t.getSimbolo().equals("{"))
				bloque++;
			else if (t.getSimbolo().equals("}"))
				bloque--;
			if (t.getOperacion() != null) {
				if (t.getTipoToken() == Gramatica.Identificador && !t.getOperacion().equals("clase")) {
					if (t.getOperacion().equals("declaracion")) {
						insertarDeclaracion(t);
					} else if (t.getOperacion().equals("asignacion") || t.getOperacion().equals("expresion")) {
						validarOperacion(t);
					}
					validarTipodeDatos(t);
				}
			}
			indice++;
		}
		return errores.isEmpty();
	}

	private void validarOperacion(Token t) {
		if (!validarDeclaracion(t))
			return;
		Token aux = tabla.get(t.getSimbolo());
		Gramatica operador = listaTokens.get(indice + 1).getTipoToken();
		if (aux.getTipoDato().equals("Entero") && (operador != Gramatica.Simbolos_de_evaluacion && operador != Gramatica.Asignacion)) {
			errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: " + t.getLinea() + "\n";
		} else if (aux.getTipoDato().equals("Booleano") && operador != Gramatica.Operadores_logicos && operador != Gramatica.Asignacion) {
			errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: " + t.getLinea() + "\n";
		}
	}

	private void validarTipodeDatos(Token t) {
		if (t.getOperacion().equals("expresion"))
			return;
		if (!validarDeclaracion(t))
			return;
		Token aux = tabla.get(t.getSimbolo());
		Gramatica tipo = getTipoDato(t.getValor());
		if (aux.getTipoDato().equals("Entero") && tipo != Gramatica.Entero_literal) {
			errores += "La variable " + t.getSimbolo() + " es de tipo entero y recibe el valor \"" + t.getValor() + "\" en la línea: " + t.getLinea() + "\n";
		} else if (aux.getTipoDato().equals("Booleano") && tipo != Gramatica.Booleano_literal) {
			errores += "La variable " + t.getSimbolo() + " es de tipo booleano y recibe el valor \"" + t.getValor() + "\" en la línea: " + t.getLinea() + "\n";
		}
	}

	private boolean validarDeclaracion(Token t) {
		if (!tabla.containsKey(t.getSimbolo())) {
			errores += "La variable \"" + t.getSimbolo() + "\" no se encuentra declarada. Fue usada en la línea: " + t.getLinea() + "\n";
			return false;
		}
		return true;
	}

	private void insertarDeclaracion(Token t) {
		if (!tabla.containsKey(t.getSimbolo())) {
			t.setAlcance("B" + bloque);
			tabla.put(t.getSimbolo(), t);
		} else {
			errores += "La variable " + t.getSimbolo() + " ya se encuentra declarada en la línea " + tabla.get(t.getSimbolo()).getLinea() + " y se declaró de nuevo en la línea " + t.getLinea() + "\n";
		}
	}

	private Gramatica getTipoDato(String entrada) {
		for (Gramatica t : Gramatica.values()) {
			int fin = t.finalCoincidencias(entrada);
			if (fin != -1) {
				return t;
			}
		}
		return null;
	}

	public HashMap<String, Token> getTabla() {
		return tabla;
	}

	public String getErrores() {
		return errores;
	}
}
