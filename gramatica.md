# Gramática
```
class_declaration = < modifier > "class" identifier "{" < field_declaration > < statement > "}"

field_declaration  =  variable_declaration  ";"  

variable_declaration  =   < modifier > (type (variable_declarator/identifier)" 

variable_declarator  =   identifier    "=" integer_literal / boolean_literal]  

expression  =  testing_expression 

testing_expression  =   (integer_literal / identifier
 (  ">" 
 /  "<" 
 /  ">=" 
 /  "<=" 
 /  "==" 
 /  "!="  ) 
integer_literal / identifier)


statement  = variable_declaration / if_statement/ while_statement

 
while_statement =  "while"  "(" expression  ")" "{"  < statement >  "}"

if_statement =   "if"  "(" expression  ")" "{" aritmetica_expression < statement >  "}"
                 
type  =  type_specifier  

type_specifier =   "boolean"   /  "int"   

modifier =   "public"   /  "private"    

integer_literal =   (  (  "1..9"  <  "0..9"  >  ) 

boolean_literal= “true” /  ”false”

identifier  =   "a..z" "1..9"          //(a-z)+ (1-9)+


aritmetica_expression  = =   (identifier  "="  (integer_literal 
 (  "+" 
 /  "-" 
 /  ”/”
 /  ”*”) 
integer_literal “;”)
```
