package main;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CodigoIntermedio {
	//TODO:
	//	-Entender que diablos debo hacer
	//		- Aparentemente necesito un árbol sintáctico.
	//		- Aparentemente partiendo del árbol sintáctico puedo hacer el código de tres direcciones.
	//	-Entender que diablos significa.
	//	-Entender como diablos debo hacerlo.
	//		- 1. Hacer el árbol.
	//		- 2. Hacer los cuadruplos.

	private ArrayList<Token> listaTokens;

	public CodigoIntermedio(ArrayList<Token> tokens) {
		listaTokens = tokens;
		generarArboles();
	}

	private void generarArboles() {
		Arbol abb = new Arbol();
		ArrayList<Token> listaExpresiones = extraerExpresiones();
		for (Token t : listaExpresiones) {
			if (t.getExpresion().equals("E3"))
				abb.insertar(t);
		}
		abb.preOrden();
	}

	private ArrayList<Token> extraerExpresiones() {
		Predicate<Token> porExpresion = t -> t.getExpresion() != null;
		var lista = listaTokens.stream().filter(porExpresion).collect(Collectors.toList());
		return (ArrayList<Token>) lista;
	}

	private void generarCuadruplos() {

	}
}
