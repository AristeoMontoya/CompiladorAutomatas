package main;

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

	public Arbol() {
	}

	private boolean esOperador(char c) {
		return c == '+' || c == '-' || c == '/' || c == '*';
	}

	public void infija(Nodo t) {
		if (t != null) {
			infija(t.izquierda);
			System.out.println(t.valor + " ");
			infija(t.derecha);
		}
	}

	public double resolver(Nodo t) {
		if(t != null) {
			double valor1, valor2;
			valor1 = resolver(t.izquierda);
			valor2 = resolver(t.derecha);
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
}
