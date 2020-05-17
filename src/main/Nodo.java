package main;

public class Nodo {
	private Arbol hojaDerecha;
	private Arbol hojaIzquierda;
	private Token dato;

	public Nodo() {
		hojaDerecha = null;
		hojaIzquierda = null;
		dato = new Token("0", null, 0);
	}

	public Arbol getHojaDerecha() {
		return hojaDerecha;
	}

	public void setHojaDerecha(Arbol hojaDerecha) {
		this.hojaDerecha = hojaDerecha;
	}

	public Arbol getHojaIzquierda() {
		return hojaIzquierda;
	}

	public void setHojaIzquierda(Arbol hojaIzquierda) {
		this.hojaIzquierda = hojaIzquierda;
	}

	public Token getDato() {
		return dato;
	}

	public void setDato(Token dato) {
		this.dato = dato;
	}
}