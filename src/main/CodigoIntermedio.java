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
		ArrayList<Token> listaExpresiones = extraerExpresiones();
		String[] expresiones = separarExpresiones(listaExpresiones);
		Arbol[] arbolesExpresion = new Arbol[expresiones.length];
		Nodo t;
		for (int i = 0; i < expresiones.length; i++) {
			System.out.println("Expresion: " + expresiones[i]);
			expresiones[i] = convertirNotacion(expresiones[i]);
			arbolesExpresion[i] = new Arbol();
			t = arbolesExpresion[i].construirArbol(expresiones[i].toCharArray());
			System.out.println(arbolesExpresion[i].resolver(t));
			arbolesExpresion[i].resolver(t);
		}
	}

	private String convertirNotacion(String expresion) { //TODO: Ordenar esto.
		expresion += ")";
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

	private String[] separarExpresiones(ArrayList<Token> lista) {
		ArrayList<String> expresiones = new ArrayList<>();
		String etiquetaActual = lista.get(0).getExpresion();
		String expresion = "";
		int totalExpresiones = 0;
		for (Token t : lista) {
			if (etiquetaActual.equals(t.getExpresion())) {
				expresion += t.getSimbolo();
			} else {
				expresiones.add(expresion);
				etiquetaActual = t.getExpresion();
				expresion = "" + t.getSimbolo();
				totalExpresiones++;
			}
		}
		expresiones.add(expresion);
		return expresiones.toArray(new String[totalExpresiones]);
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
