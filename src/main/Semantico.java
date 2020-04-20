package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Semantico {
	HashMap<String, Token> tabla = new HashMap<>();
	private ArrayList<Token> listaTokens;
	private String errores;
	private int indice = 0;

	/*	TODO:
	 *	-Crear tabla de símbolos
	 *		Mostrar la tabla de símbolos con las variables declaradas dentro de una clase/método/función
	 *		Mostrar por lo menos los siguientes atributos:
	 *		-Tipo de dato
	 *		-Posición
	 *		-Valor
	 *		-Alcance global/local
	 *
	 *	-Validar la asignación de una variable *
	 *		Validar que es correcto el tipo de dato asignado a una variable
	 *		Al final del proceso de compilación, en caso de haber errores
	 *		mostrar un mensaje que indique la variable, el tipo de dato de la
	 *		variable, el tipo de dato que se le asignó y la posición del error
	 *
	 *	-Validar las variables usadas y no definidas *
	 *		Validar que las variables esten declaradas antes de ser utilizadas en el código
	 *		Paara conocer los valores.
	 *		Al finalizar la compilación, en caso de haber erroes mostrar un mensaje
	 *		con la variable y la posición donde se trató de utilizar
	 *
	 *	-Validar las variables ya declaradas *
	 *		Validar que una variable no se declare más de una vez
	 *		En caso de existir la declaración de una variable más de una vez, mostrar
	 *		un mensaje con la variable y la posición donde se realiza la segunda declaración.
	 *		Además agregar la posición donde se realizó la primera declaración
	 *
	 *	-Validar operando de tipos compatibles
	 *		Si se encuentra una expresión con operadores no aptos al contexto
	 *		de tipos de datos usados, mostrar un mensaje con la posición de la
	 *		expresión
	 */
	public Semantico(ArrayList<Token> listaTokens) {
		this.listaTokens = listaTokens;
		errores = "";
	}

	public void comenzarAnalisis() {
		for (Token t : listaTokens) {
			if (t.getOperacion() == null && t.getTipoToken() == Gramatica.Identificador) {
				if (!tabla.containsKey(t.getSimbolo())) {
					errores += "La variable " + t.getSimbolo() + " se utilizó sin ser declarada en la línea " + t.getLinea() + " \n";
				}
			} else if (t.getTipoToken() == Gramatica.Identificador && !t.getOperacion().equals("clase")) {
				System.out.println("Evaluando " + t.getSimbolo());
				if (t.getOperacion().equals("declaracion")) {
					insertarDeclaracion(t);
				} else if (t.getOperacion().equals("asignacion")) {
					validarOperacion(t);
				}
				validarTipodeDatos(t);
			}
			indice++;
		}
		System.out.println(errores);
	}

	private void validarOperacion(Token t) {
		Token aux = tabla.get(t.getSimbolo());
		Gramatica operador = listaTokens.get(indice + 1).getTipoToken();
		System.out.println("Entró " + aux.getSimbolo() + " y el operador " + listaTokens.get(indice +1).getSimbolo());
		if (aux.getTipoDato().equals("Entero") && (operador != Gramatica.Operadores_aritmeticos && operador != Gramatica.Asignacion)) {
			errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: " + t.getLinea() + "\n";
		} else if (aux.getTipoDato().equals("Booleano") && operador != Gramatica.Operadores_logicos && operador != Gramatica.Asignacion) {
			errores += "Se usaron operadores incorrectos para la variable " + t.getSimbolo() + " en la línea: " + t.getLinea() + "\n";
		}
	}

	private void validarTipodeDatos(Token t) {
		Token aux = tabla.get(t.getSimbolo());
		Gramatica tipo = getTipoDato(t.getValor());
		if (aux.getTipoDato().equals("Entero") && tipo != Gramatica.Entero_literal) {
			errores += "La variable " + t.getSimbolo() + " es de tipo entero y recibe el valor " + t.getValor() + "\n";
		} else if (aux.getTipoDato().equals("Booleano") && tipo != Gramatica.Booleano_literal) {
			errores += "La variable " + t.getSimbolo() + " es de tipo booleano y recibe el valor " + t.getValor() + "\n";
		}
	}

	private void insertarDeclaracion(Token t) {
		if (!tabla.containsKey(t.getSimbolo())) {
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
}
