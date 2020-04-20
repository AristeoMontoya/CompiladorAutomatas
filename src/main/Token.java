package main;

public class Token {
	private String simbolo;
	private String valor;
	private String tipoDato;
	private Gramatica tipoToken;
	private String alcance;
	private String operacion;
	private int linea;

	public Token(String simbolo, Gramatica tipoToken, int linea) {
		this.simbolo = simbolo;
		this.tipoToken = tipoToken;
		this.linea = linea;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simobolo) {
		this.simbolo = simobolo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public Gramatica getTipoToken() {
		return tipoToken;
	}

	public void setTipoToken(Gramatica tipoToken) {
		this.tipoToken = tipoToken;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
}
