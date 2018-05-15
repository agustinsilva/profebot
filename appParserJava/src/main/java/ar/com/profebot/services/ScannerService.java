package ar.com.profebot.services;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.container.TokenData;
import ar.com.profebot.exceptions.InvalidExpressionException;

public class ScannerService {

	public static enum TOKEN {CONSTANTE, LETRA_X, PARENIZQUIERDO,
	    PARENDERECHO, SUMA, RESTA,
	    MULTIPLICACION, DIVISION, IGUAL, POTENCIA, RAIZ, FIN
	};
	
	private enum ACEPTORES
	{
	    INICIO_ST_ACEPTORES(50), // Mayor a esto son aceptores
	    ST_ACEPTA_NUMERO(50),
	    ST_ACEPTA_X_CON_COEF(51),
	    ST_ACEPTA_X_SIN_COEF(52),
	    ST_ACEPTA_IGUAL(53),
	    ST_ACEPTA_PARENTESIS_IZQ(54),
	    ST_ACEPTA_PARENTESIS_DER(55),
	    ST_ACEPTA_MAS(56),
	    ST_ACEPTA_MENOS(57),
	    ST_ACEPTA_NUMERO_NEGATIVO(58),
	    ST_ACEPTA_X_CON_COEF_NEGATIVO(59),
	    ST_ACEPTA_X_SIN_COEF_NEGATIVO(60),
	    ST_ACEPTA_PRODUCTO(61),
	    ST_ACEPTA_DIVISION(62),
	    ST_ACEPTA_X_CON_COEF_Y_EXP(63),
	    ST_ACEPTA_X_SIN_COEF_Y_EXP(64),
	    ST_ACEPTA_X_CON_COEF_NEGATIVO_Y_EXP(65),
	    ST_ACEPTA_X_SIN_COEF_NEGATIVO_Y_EXP(66),
	    ST_ACEPTA_POTENCIA(67),
	    ST_ACEPTA_RAIZ(68),
	    ST_ACEPTA_FIN(98),
		ST_EXPRESION_INVALIDA(99);

		int id;
		private ACEPTORES(int id){
			this.id = id;
		}
		public int getId() {
			return id;
		}
	};
	
	private int tablaTransicion[][] =
		{
		//    D  X  =   (   )   +   -   *   /   ^   R   FIN 
			{ 1, 3,	4,	5,	6,	7,	8,	12,	13, 22, 23,	98},   	/*  Estado 0- */
			{ 1, 2,	50,	99,	50,	50,	50,	50,	50,	50, 99, 50},   	/*  Estado 1 */
			{99,99,	51,	99,	51,	51,	51,	51,	51,	14, 99, 51},   	/*  Estado 2 */
			{99,99,	52,	99,	52,	52,	52,	52,	52,	16, 99, 52},   	/*  Estado 3 */
			{53,53,	99,	53,	99,	99,	53,	99,	99,	99, 53, 99},   	/*  Estado 4 */
			{54,54,	99,	54,	99,	99,	54,	99,	99,	99, 54, 99},   	/*  Estado 5 */
			{99,99,	55,	99,	55,	55,	55,	55,	55,	55, 99, 55},   	/*  Estado 6 */
			{56,56,	99,	56,	99,	99,	56,	99,	99,	99, 56, 99},   	/*  Estado 7 */
			{ 9,11,	99,	57,	99,	99,	99,	99,	99,	99, 57, 99},   	/*  Estado 8 */
			{ 9,10,	58,	99,	58,	58,	58,	58,	58,	58, 99, 58},   	/*  Estado 9 */
			{99,99,	59,	99,	59,	59,	59,	59,	59,	18, 99, 59},   	/*  Estado 10 */
			{99,99,	60,	99,	60,	60,	60,	60,	60,	30, 99, 60},   	/*  Estado 11 */
			{61,61,	99,	61,	99,	99,	61,	99,	99,	99, 61, 99},   	/*  Estado 12 */
			{62,62,	99,	62,	99,	99,	62,	99,	99,	99, 62, 99},   	/*  Estado 13 */
			{15,99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99},	/*  Estado 14 */
			{15,99, 63, 99, 63, 63, 63, 63, 63, 99, 99, 63}, 	/*  Estado 15 */
			{17,99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99}, 	/*  Estado 16 */
			{17,99, 64, 99, 64, 64, 64, 64, 64, 99, 99, 64}, 	/*  Estado 17 */
			{19,99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99}, 	/*  Estado 18 */
			{19,99, 65, 99, 65, 65, 65, 65, 65, 99, 99, 65}, 	/*  Estado 19 */
			{21,99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99}, 	/*  Estado 20 */
			{21,99, 66, 99, 66, 66, 66, 66, 66, 99, 99, 66}, 	/*  Estado 21 */
			{67,99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99}, 	/*  Estado 22 */
			{68,68, 99, 68, 99, 99, 68, 99, 99, 99, 99, 99}  	/*  Estado 23 */
		};
	
	
	// Caracteres reconocidos (columnas de la TT)
	private static final int DIGITO_CHAR=0;         // [0-9]
	private static final int LETRA_X=1;          	// [X]
	private static final int IGUAL_CHAR=2;          // =
	private static final int PARENTESIS_IZQ_CHAR=3; // (
	private static final int PARENTESIS_DER_CHAR=4; // )
	private static final int MAS_CHAR=5;            // +
	private static final int MENOS_CHAR=6;          // -
	private static final int ASTERISCO_CHAR=7;      // *
	private static final int BARRA_CHAR=8;          // /
	private static final int POTENCIA_CHAR=9;       // ^
	private static final int RAIZ_CHAR=10;          // R
	private static final int FIN_CHAR=11;           // �
	
	private static final char END_CHAR='�';          
	
    private int pos = -1;
    private boolean readNextToken = true;
    private String parsedExpression = null;
    private TOKEN previousToken = null;
    private TOKEN lastToken = null;
    private String resultado = "";
    private int numParantesisAbiertos;
    private boolean signoIgualDetectado = false;
    
    public ScannerService() {
		super();
	}
    
    public ScannerService(String expression) {
    	super();
    	init(expression);
    }
	private void init(String expression){
    	pos = -1;
		parsedExpression = expression.toUpperCase().replace(" ", "") + END_CHAR;
		readNextToken = true;
		lastToken = null;
		previousToken = null;
		numParantesisAbiertos = 0;
		signoIgualDetectado = false;
    }
    
	public void ValidateExpression(String expression) throws InvalidExpressionException {
	
		init(expression);
		
		while(!TOKEN.FIN.equals(prox_token())){
	        match(prox_token()); // Fuerzo a seguir
	    }
	}
	
	public List<TokenData> getExpressionList(String expression) throws InvalidExpressionException {
		
		init(expression);
		
		List<TokenData> tokensData = new ArrayList<>();
		while(!TOKEN.FIN.equals(prox_token())){
			tokensData.add(new TokenData(prox_token(), resultado));
			readNextToken = true;
		}
		
		return tokensData;
	}
	
    private TOKEN scanner() throws InvalidExpressionException{

        int estado = 0; // Estado inicial
        int tipo;
        char c;
        TOKEN tokDetectado;
        resultado = "";
        
        do{
        	pos++;
            // Toma el char y verifica el tipo
            c = parsedExpression.charAt(pos);
            tipo = getType(c);
            if(tipo == -1){throw new InvalidExpressionException("Detectado caracter invalido: " + c);}
            
            // Nuevo estado
            estado = tablaTransicion[estado][tipo];
        	
            // Peque�o fix que determina si un "-" es parte de un coeficiente o es un separador de terminos
            // Revisando el token detectado anteriormente
            if (tipo == MENOS_CHAR && resultado.isEmpty()){
            	if (TOKEN.CONSTANTE.equals(previousToken) || 
	    			TOKEN.LETRA_X.equals(previousToken) || 
	    			TOKEN.PARENDERECHO.equals(previousToken) ){
            		
            		// Con esto evito que sea coeficiente, frena aca
            		estado = ACEPTORES.ST_ACEPTA_MENOS.getId();
            		pos++; // Resto mas adelante, simulo un centinela
                	resultado += c;
            	}
            }
            
            // Si llega a un =, valido parentesis
            if (estado == ACEPTORES.ST_ACEPTA_IGUAL.getId() ){
            	if (numParantesisAbiertos != 0){
            		throw new InvalidExpressionException("Llega a un = con " + numParantesisAbiertos + " parentesis abiertos!");
            	}
            	if (signoIgualDetectado){
            		throw new InvalidExpressionException("Se encuentra mas de 1 signo =!");
            	}
            	signoIgualDetectado = true;
            }else if (estado == ACEPTORES.ST_ACEPTA_FIN.getId()){
            	if (numParantesisAbiertos != 0){
            		throw new InvalidExpressionException("Termina de parsear la expres�n, luego del = tiene " + numParantesisAbiertos + " parentesis abiertos!");
            	}
            }else if (estado == ACEPTORES.ST_ACEPTA_PARENTESIS_IZQ.getId()){
            	numParantesisAbiertos++;
            }else if (estado == ACEPTORES.ST_ACEPTA_PARENTESIS_DER.getId()){
            	numParantesisAbiertos--;
            	if (numParantesisAbiertos < 0){
            		throw new InvalidExpressionException("Se cierra un parentesis que no fue abierto!");
            	}
            }
            
            
            // Si llego a una expresi�n inv�lida, no sigo parseando
            if (estado == ACEPTORES.ST_EXPRESION_INVALIDA.getId()){
            	throw new InvalidExpressionException("Detectada expresion invalida en pos " + pos);
            	
            // Si tiene centinela, no lo agrega al lexema y devuelve el char
            }else if (estadoAceptor(estado)){
            	pos--; // Devuelve centinela
            }else{
            	resultado += c;
            }
        }
        while (!estadoAceptor(estado)); // Termina cuando llega a un estado aceptor

        // Obtiene el token segun el estado aceptor
        tokDetectado = getToken(estado);
        
        //System.out.println("Token: " + tokDetectado + ", resultado: " + resultado);
        
        return tokDetectado;
    }

    private int getType(char c)
    {
        int typeC = -1; // Default

        if (Character.isDigit(c)) typeC = DIGITO_CHAR;
        else if (c == 'X') typeC = LETRA_X;
        else if(c == '=') typeC=IGUAL_CHAR;
        else if(c == '(') typeC=PARENTESIS_IZQ_CHAR;
        else if(c == ')') typeC=PARENTESIS_DER_CHAR;
        else if(c == '+') typeC=MAS_CHAR;
        else if(c == '-') typeC=MENOS_CHAR;
        else if(c == '*') typeC=ASTERISCO_CHAR;
        else if(c == '/') typeC=BARRA_CHAR;
        else if(c == '^') typeC=POTENCIA_CHAR;
        else if(c == 'R') typeC=RAIZ_CHAR;
        else if(c == END_CHAR) typeC=FIN_CHAR;

        return typeC;
    }

    private boolean estadoAceptor(int estado)
    {
        return estado >= ACEPTORES.INICIO_ST_ACEPTORES.getId();
    }

    private TOKEN getToken(int estado)
    {
    	
    	TOKEN t = null;
        if(estado == ACEPTORES.ST_ACEPTA_NUMERO.getId() || estado == ACEPTORES.ST_ACEPTA_NUMERO_NEGATIVO.getId()){
            t = TOKEN.CONSTANTE;
        }else if(estado == ACEPTORES.ST_ACEPTA_X_CON_COEF.getId() || estado == ACEPTORES.ST_ACEPTA_X_SIN_COEF.getId()
        		|| estado == ACEPTORES.ST_ACEPTA_X_CON_COEF_NEGATIVO.getId() || estado == ACEPTORES.ST_ACEPTA_X_SIN_COEF_NEGATIVO.getId()
        		|| estado == ACEPTORES.ST_ACEPTA_X_CON_COEF_Y_EXP.getId() || estado == ACEPTORES.ST_ACEPTA_X_SIN_COEF_Y_EXP.getId()
        		|| estado == ACEPTORES.ST_ACEPTA_X_CON_COEF_NEGATIVO_Y_EXP.getId() || estado == ACEPTORES.ST_ACEPTA_X_SIN_COEF_NEGATIVO_Y_EXP.getId()){
            t = TOKEN.LETRA_X;
        }else if(estado == ACEPTORES.ST_ACEPTA_PARENTESIS_IZQ.getId()){
        	t = TOKEN.PARENIZQUIERDO;
        }else if(estado == ACEPTORES.ST_ACEPTA_PARENTESIS_DER.getId()){
            t = TOKEN.PARENDERECHO;
        }else if(estado == ACEPTORES.ST_ACEPTA_MAS.getId()){
            t = TOKEN.SUMA;
        }else if(estado == ACEPTORES.ST_ACEPTA_MENOS.getId()){
            t = TOKEN.RESTA;
        }else if(estado == ACEPTORES.ST_ACEPTA_PRODUCTO.getId()){
            t = TOKEN.MULTIPLICACION;
        }else if(estado == ACEPTORES.ST_ACEPTA_DIVISION.getId()){
            t = TOKEN.DIVISION;
        }else if(estado == ACEPTORES.ST_ACEPTA_IGUAL.getId()){
        	t = TOKEN.IGUAL;
        }else if(estado == ACEPTORES.ST_ACEPTA_POTENCIA.getId()){
        	t = TOKEN.POTENCIA;
        }else if(estado == ACEPTORES.ST_ACEPTA_RAIZ.getId()){
        	t = TOKEN.RAIZ;
        }else if(estado == ACEPTORES.ST_ACEPTA_FIN.getId()){
            t = TOKEN.FIN;
        }
        
        return t;
    }

    public TOKEN prox_token() throws InvalidExpressionException
    {

        // Busca token?
        if (readNextToken)
        {
        	previousToken = lastToken;
            lastToken = scanner();
            readNextToken=false;
        }
        return lastToken;
    }

    public void match(TOKEN tok) throws InvalidExpressionException
    {

        // Pide al prox. por si tiene que volver a leer.
    	TOKEN currentToken = prox_token();

        // Fuerza a leer otro
        readNextToken=true;

        if (!tok.equals(currentToken))
        {
            throw new InvalidExpressionException("Se esperaba " + tok + ", llega: " + currentToken);
        }
    }
    
    public String getResultado(){
    	return resultado;
    }
}
