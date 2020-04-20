# Compilador para la materia de Lenguajes y Autómatas

Un compilador es una pieza de software que traduce el código de un lenguaje de programación a lenguaje máquina. 
Los compiladores no son la única pieza de software capaz de hacer esto, existen también los intérpretes, y su principal diferencia es que estos realizan la <em>traducción</em> en tiempo de ejecución. El compilador traduce el código fuente dando como resultado el código objeto, que es el código que será ejecutado.
Algunos ejemplos de lenguajes de programación interpretados son Python, JavaScript y PHP.

Existen, además, transpiladores, que son piezas de software que traducen de un lenguaje de programación a otro. Un ejemplo de transpilador es Cython, que traduce código escrito en Python a código en C para posteriormente ser compilado con el compilador de C.

---
## Fases de un compilador
- [Analizador léxico](#lexico)
- [Analizador sintáctico](#sintactico)
- [Analizador semántico](#semantico)
- [Generación de código intermedio](#codigoIntermedio)
- [Optimización de código](#optimizacion)
- [Generación de código objeto.](#codigoObjeto)

### <a name = "lexico"></a>Análisis léxico.
La fase del análisis léxico agrupa las cadenas de caracteres escritas en el código y las separa en [lexemas](#lexema), y para cada lexema genera un [*token*](#token).

El análisis léxico agrega los token encontrados a la [tabla de símbolos](#tablaSimbolos), que serán necesarios para la siguiente fase del compilador.

### <a name = "sintactico"></a>Análisis sintáctico.
El analizador sintáctico (o parser) determina que la estructura gramatica del código sea válida.

El analizador toma como entrada un flujo de *Tokens* producida por analizador léxico, y crea una representación en forma de árbol.

El producto del análisis sintáctico es un [árbol de sintáxis](#arbolSintactico), que representa el orden en que se deben ejecutar las operaciones.

### <a name = "semantico"></a>Análisis semántico.
El analizador semántico trabaja con la tabla de símbolos y con el árbol sintáctico producido en el analizador sintáctico para validar la consistencia del código escrito.

Una de las responsabilidades del analizador semántico, es la validación de tipos, donde se verifica que el tipo de operador coincida con el tipo de operando.

### <a name = "códigoIntermedio"></a>Generación de código intermedio.
Aún no implementado.

### <a name = "optimizacion"></a>Optimización de código.
Aún no implementado.

### <a name = "codigoObjeto"></a>Generación de código objeto. 
Aún no implementado.

# Términos utilizados.

## <a name = "lexema"></a>Lexema.
Por definir.

### <a name = "token"></a>Token.
Por definir

## <a name = "tablaSimbolos"></a>Tabla de símbolos.
Por definir.

### <a name = "gramatica"></a>Gramatica libre de contexto.
Por definir.

### <a name = "arbolSintactico"></a>Árbol Sintáctico.
Por definir. Aún no implementado.