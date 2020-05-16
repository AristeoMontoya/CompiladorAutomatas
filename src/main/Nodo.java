package main;

public class Nodo<Token> {
	Token dato;
	Nodo<Token> padre, derecha, izquierda;

	public Nodo(Token valor) {
		dato = valor;
	}
}