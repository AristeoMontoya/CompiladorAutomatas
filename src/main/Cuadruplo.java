package main;

public class Cuadruplo {
	String Operando1, Operando2, Operador, Resultado, Etiqueta, Identificador;

	public Cuadruplo(String operando1, String operando2, String operador, String resultado) {
		Operando1 = operando1;
		Operando2 = operando2;
		Operador = operador;
		Resultado = resultado;
	}

	public String getFormato() {
		return String.format("| %-10s | %-10s | %-10s | %-10s |", Operador, Operando1, Operando2, Etiqueta);
	}

	public String getIdentificador() {
		return Identificador;
	}

	public void setIdentificador(String identificador) {
		Identificador = identificador;
	}

	public String getEtiqueta() {
		return Etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		Etiqueta = etiqueta;
	}

	public String getOperando1() {
		return Operando1;
	}

	public void setOperando1(String operando1) {
		Operando1 = operando1;
	}

	public String getOperando2() {
		return Operando2;
	}

	public void setOperando2(String operando2) {
		Operando2 = operando2;
	}

	public String getOperador() {
		return Operador;
	}

	public void setOperador(String operador) {
		Operador = operador;
	}

	public String getResultado() {
		return Resultado;
	}

	public void setResultado(String resultado) {
		Resultado = resultado;
	}
}