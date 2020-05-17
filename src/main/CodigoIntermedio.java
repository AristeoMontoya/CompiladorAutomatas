package main;

import java.util.ArrayList;
import java.util.Stack;
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
		StringBuilder expresion = new StringBuilder();
		Arbol abb = new Arbol();
		boolean bandera = false;
		ArrayList<Token> listaExpresiones = extraerExpresiones();
		for (Token t : listaExpresiones) {
			if (t.getExpresion().equals("E3") && bandera)
				expresion.append(t.getSimbolo());
			if (t.getExpresion().equals("E3") && t.getSimbolo().equals("=")) {
				bandera = true;
			}
		}
		String cosa = convertirNotacion("" + expresion + ")");
		System.out.println(cosa);
		Nodo t = abb.construirArbol(cosa.toCharArray());
		abb.infija(t);
	}

	private String convertirNotacion(String expresion) { //TODO: Ordenar esto.
		String resultado = "";
		Stack<Character> pila = new Stack<>();
		pila.push('(');
		for (int i = 0; i < expresion.length(); i++) {
			char actual = expresion.charAt(i);
			if (precedencia(actual) > 0) {
				while (!pila.isEmpty() && precedencia(pila.peek()) >= precedencia(actual)) {
					resultado += pila.pop();
				}
				pila.push(actual);
			} else if (actual == ')') {
				char auxiliar = pila.pop();
				while (auxiliar != '(') {
					resultado += auxiliar;
					auxiliar = pila.pop();
				}
			} else if (actual == '(') {
				pila.push(actual);
			} else {
				resultado += actual;
			}
		}
		if (!pila.isEmpty()) {
			for (int i = 0; i <= pila.size(); i++) {
				resultado += pila.pop();
			}
		}
		return resultado;
	}

	private ArrayList<Token> extraerExpresiones() {
		Predicate<Token> porExpresion = t -> t.getExpresion() != null;
		var lista = listaTokens.stream().filter(porExpresion).collect(Collectors.toList());
		return (ArrayList<Token>) lista;
	}

	private void generarCuadruplos() {

	}

	private int precedencia(char c) {
		switch (c) {
			case '*':
			case '/':
				return 2;
			case '+':
			case '-':
				return 1;
		}
		return -1;
	}
}
