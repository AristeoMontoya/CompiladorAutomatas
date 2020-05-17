package main;

public class Arbol {
	private Nodo raiz;

	public Arbol() {
//		raiz = new Nodo();
	}

	public boolean vacio() {
		return raiz == null;
	}

	public void insertar(Token t) {
		if (vacio()) {
			Nodo nuevo = new Nodo();
			nuevo.setDato(t);
			nuevo.setHojaDerecha(new Arbol());
			nuevo.setHojaIzquierda(new Arbol());
			raiz = nuevo;
		} else {
			if (precedencia(t.getSimbolo()) > precedencia(raiz.getDato().getSimbolo())) {
				raiz.getHojaDerecha().insertar(t);
			} else {
				raiz.getHojaIzquierda().insertar(t);
			}
		}
	}

	public void preOrden() {
		if (!vacio()) {
			System.out.println(raiz.getDato().getSimbolo() + " : " + raiz.getDato().getExpresion() + "\n");
			raiz.getHojaIzquierda().preOrden();
			raiz.getHojaDerecha().preOrden();
		}
	}

	private int precedencia(String lexema) {
		int precedencia = 0;
		switch (lexema) {
			case "=":
				precedencia = 5;
				break;
			case "*":
			case "/":
				precedencia = 2;
				break;
			case "+":
			case "-":
				precedencia = 1;
				break;
			default:
				if (lexema.matches("^[0-9]"))
					precedencia = 4;
				break;
		}
		return precedencia;
	}
}
