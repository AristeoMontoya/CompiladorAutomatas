package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Semantico {
	HashMap<String, Token> tabla = new HashMap<>();
	private ArrayList<Token> listaTokens;

	/*	TODO:
	 *	-Crear tabla de símbolos
	 *		Mostrar la tabla de símbolos con las variables declaradas dentro de una clase/método/función
	 *		Mostrar por lo menos los siguientes atributos:
	 *		-Tipo de dato
	 *		-Posición
	 *		-Valor
	 *		-Alcance global/local
	 *
	 *	-Validar la asignación de una variable
	 *		Validar que es correcto el tipo de dato asignado a una variable
	 *		Al final del proceso de compilación, en caso de haber errores
	 *		mostrar un mensaje que indique la variable, el tipo de dato de la
	 *		variable, el tipo de dato que se le asignó y la posición del error
	 *
	 *	-Validar las variables usadas y no definidas
	 *		Validar que las variables esten declaradas antes de ser utilizadas en el código
	 *		Paara conocer los valores.
	 *		Al finalizar la compilación, en caso de haber erroes mostrar un mensaje
	 *		con la variable y la posición donde se trató de utilizar
	 *
	 *	-Validar las variables ya declaradas
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
		this.listaTokens = extraerIdentificadores(listaTokens);
	}

	private ArrayList<Token> extraerIdentificadores(ArrayList<Token> listaTokens) {
		ArrayList<Token> aux = new ArrayList<>();
		for (Token t : listaTokens) {
			if (t.getTipoToken() == Gramatica.Identificador)
				aux.add(t);
		}
		return aux;
	}

	public void comenzarAnalisis() {
	}

	private void validarOperacion() {

	}

	private void validarOperadores() {

	}

	private void validarDatosConstantes() {

	}

	private void validarTipodeDatos() {

	}

	private void insertarDeclaracion() {

	}

	private void validarTipoAccion() {

	}

	private void encontrarSimbolo() {

	}

	public void obtenerDatosSimbolo() {

	}
}
