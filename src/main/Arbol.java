package main;

import java.util.ArrayList;
import java.util.Stack;

class Nodo {
	char valor;
	Nodo izquierda, derecha;

	Nodo(char valor) {
		this.valor = valor;
		izquierda = derecha = null;
	}
}

public class Arbol {
	private ArrayList<Cuadruplo> listaCuadruplos;
	private Nodo raiz;
	private String etiqueta;

	public Arbol() {
		listaCuadruplos = new ArrayList<>();
	}

	private boolean esOperador(char c) {
		return c == '+' || c == '-' || c == '/' || c == '*';
	}

	public void infija(Nodo t) {
		if (t != null) {
			infija(t.izquierda);
			System.out.println(t.valor);
			infija(t.derecha);
		}
	}

	public int resolver(Nodo t) {
		if (t != null) {
			String dato = "" + t.valor;
			int valor1, valor2;
			valor1 = resolver(t.izquierda);
			valor2 = resolver(t.derecha);
			if (dato.matches("^(\\+|-|/|\\*)")) {
				int resultado = 0;
				switch (t.valor) {
					case '+':
						resultado = valor1 + valor2;
						listaCuadruplos.add(new Cuadruplo("" + valor1, "" + valor2, "+", "" + resultado));
						break;
					case '-':
						resultado = valor1 - valor2;
						listaCuadruplos.add(new Cuadruplo("" + valor1, "" + valor2, "-", "" + resultado));
						break;
					case '*':
						resultado = valor1 * valor2;
						listaCuadruplos.add(new Cuadruplo("" + valor1, "" + valor2, "*", "" + resultado));
						break;
					case '/':
						resultado = valor1 / valor2;
						listaCuadruplos.add(new Cuadruplo("" + valor1, "" + valor2, "/", "" + resultado));
						break;
				}
				return resultado;
			} else {
				return Integer.parseInt("" + t.valor);
			}
		}
		return 0;
	}

	public Nodo construirArbol(char[] expresion) {
		Stack<Nodo> pila = new Stack<>();
		Nodo t1, t2, t3;

		for (int i = 0; i < expresion.length; i++) {
			char actual = expresion[i];
			if (!esOperador(actual)) {
				t1 = new Nodo(actual);
				pila.push(t1);
			} else {
				t1 = new Nodo(actual);

				t2 = pila.pop();
				t3 = pila.pop();

				t1.derecha = t2;
				t1.izquierda = t3;

				pila.push(t1);
			}
		}
		t1 = pila.peek();
		pila.pop();
		return t1;
	}

	public ArrayList<Cuadruplo> getListaCuadruplos() {
		return listaCuadruplos;
	}

	public Nodo getRaiz() {
		return raiz;
	}

	public void setRaiz(Nodo raiz) {
		this.raiz = raiz;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
}
