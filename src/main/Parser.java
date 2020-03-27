package main;

import java.util.ArrayList;
import java.util.Stack;

public class Parser
{
	ArrayList<String> componentes;

	String componente, auxDer, auxIzq;

	String salida = "";
	public static String salida2 = "";
	private int idx = 0;
	public Stack<Integer> pila;
	boolean error;

	public Parser()
	{
		componentes = new ArrayList<String>();

	}

	private void Acomodar(Gramatica token, String s)
	{

		if (getTipo(componente) == token && componente.equals(s))
		{
			Avanza();
		} else
		{
			error(getTipo(componente), s);
		}
	}

	private void Avanza()
	{
		if (idx < componentes.size() - 1)
		{
			idx++;
		}
		try
		{
			componente = componentes.get(idx);

		} catch (IndexOutOfBoundsException e)
		{
			idx--;
		}
	}

	private void aritmetic_expression()
	{

		String c;
		c = componente;

		identificador();

		Acomodar(Gramatica.Simbolos_especiales, "=");
		if (getTipo(componente) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else if (getTipo(componente) == Gramatica.Identificador)
		{
			identificador();
		}

		c = componente;
		while (!c.equals(";"))
		{
			if (getTipo(c) == Gramatica.Operadores_aritmeticos)
			{
				Avanza();
			} else
			{
				error(Gramatica.Operadores_aritmeticos, "arit");
				break;
			}

			if (getTipo(componente) == Gramatica.Entero_literal)
			{
				integer_literal();
			} else if (getTipo(componente) == Gramatica.Identificador)
			{
				identificador();
			}
			c = componente;
		}

		Acomodar(Gramatica.Simbolos_especiales, ";");
	}

	private void boolean_literal()
	{
		Avanza();
	}

	private void declaracion_Clase()
	{
		String c = componente;
		if (!c.equals("class"))
		{
			modificador();
		}
		c = componente;

		Acomodar(Gramatica.Declaracion_clase, "class");

		c = componente;

		identificador();
		c = componente;
		Acomodar(Gramatica.Simbolos_especiales, "{");

		c = componente;

		field_Declaration();
		statement();
		Acomodar(Gramatica.Simbolos_especiales, "}");
	}

	private void expression()
	{
		testing_expression();
	}

	private void error(Gramatica t, String to)
	{
		switch (t)
		{
			case Simbolos_especiales:
				salida += "Error Sintactico, se esperaba un \"" + to;
				break;
			case Operadores_aritmeticos:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaban operadores aritméticos";
				break;
			case Asignacion:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba \"=\"";
				break;
			case Modificador:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba modificador";
				break;
			case Entero_literal:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba número entero";
				break;
			case Booleano_literal:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba booleano";
				break;
			case Identificador:
				salida += "Error Sintactico, se encontró " + componente + ", se esperaba identificador";
				break;
			case Declaracion_clase:
				break;
			case Especificador:
				break;
			case If:
				break;
			case Simbolos_de_evaluacion:
				break;
			case While:
				break;
			default:
				break;
		}
		salida2 += "Token obtenido:" + componente + "\n" + "Token Esperado: " + to + "\n-------------------------------------------\n";

	}

	private void field_Declaration()
	{
		String c = componente;
		if (getTipo(c) == Gramatica.Modificador || getTipo(c) == Gramatica.Especificador)
		{
			variable_declaration();
			c = componente;
			Acomodar(Gramatica.Simbolos_especiales, ";");
		}
	}

	public String getSalida()
	{
		return salida;
	}

	private void if_Statement()
	{
		Acomodar(Gramatica.Simbolos_especiales, "(");
		expression();
		Acomodar(Gramatica.Simbolos_especiales, ")");
		Acomodar(Gramatica.Simbolos_especiales, "{");
		aritmetic_expression();

		Acomodar(Gramatica.Simbolos_especiales, "}");

		statement();
	}

	private void integer_literal()
	{
		Acomodar(Gramatica.Entero_literal, componente);
	}

	private void identificador()
	{
		Acomodar(Gramatica.Identificador, componente);

	}

	private void modificador()
	{
		if (getTipo(componente) == Gramatica.Modificador)
		{
			Avanza();
		} else
		{
			error(Gramatica.Identificador, "");
		}
	}

	public String motorSintactico(ArrayList<String> listaTokens)
	{

		salida = "";
		idx = 0;
		componentes = listaTokens;
		try
		{
			componente = componentes.get(0);
		} catch (IndexOutOfBoundsException e)
		{

		}
		declaracion_Clase();
		if (salida.equals(""))
		{
			salida = "No hay errores sintacticos";
			return salida;
		} else
		{
			return salida;
		}

	}

	private void statement()
	{
		String c = componente;
		if (getTipo(c) == Gramatica.If)
		{
			Avanza();
			if_Statement();
			
		} else if (getTipo(c) == Gramatica.While)
		{
			Avanza();
			while_Statement();
		} else if (getTipo(c) == Gramatica.Modificador || getTipo(c) == Gramatica.Especificador)
		{
			variable_declaration();
			Acomodar(Gramatica.Simbolos_especiales, ";");
		}
		else
			error(getTipo(c), "If while, o declaración de variable");
	}

	private void testing_expression()
	{
		Gramatica t = getTipo(componente);
		if (t == Gramatica.Identificador)
		{
			identificador();
		} else if (t == Gramatica.Entero_literal)
		{
			integer_literal();
		} else
			error(t, componente);

		t = getTipo(componente);

		if (t == Gramatica.Simbolos_de_evaluacion)
		{
			Avanza();
		} else
		{
			error(t, componente);
		}

		t = getTipo(componente);

		if (t == Gramatica.Entero_literal)
		{
			integer_literal();
		}
		else if(t == Gramatica.Identificador)
		{
			identificador();
		} 
		else
			error(t, componente);
	}

	private void type()
	{
		type_specifier();
	}

	private void type_specifier()
	{
		String c = null;
		c = componente;
		if (getTipo(c) == Gramatica.Especificador)
		{
			Avanza();
		} else
		{
			error(getTipo(c), Gramatica.Especificador.toString());
		}
	}

	private void variable_declaration()
	{

		String c = componente;

		if (getTipo(c) == Gramatica.Modificador)
		{
			modificador();
		}
		type();
		identificador();

		c = componente;
		if (c.equals("="))
		{
			Avanza();
			variable_declarator();
		}

	}

	private void variable_declarator()
	{
		String c;
		c = componente;

		if (getTipo(c) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else if (getTipo(c) == Gramatica.Booleano_literal)
		{
			boolean_literal();
		} else
		{
			error(getTipo(componente), "");
		}
	}

	private void while_Statement()
	{

		Acomodar(Gramatica.Simbolos_especiales, "(");
		expression();
		Acomodar(Gramatica.Simbolos_especiales, ")");
		Acomodar(Gramatica.Simbolos_especiales, "{");
		statement();
		Acomodar(Gramatica.Simbolos_especiales, "}");
	}

	private Gramatica getTipo(String s)
	{
		int x;
		for (Gramatica t : Gramatica.values())
		{
			x = t.final_coincidencias(s);
			if (x != -1)
			{
				return t;
			}
		}
		return null;
	}
}