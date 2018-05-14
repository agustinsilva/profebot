package ar.com.profebot.services;

import ar.com.profebot.container.Tree;
import ar.com.profebot.container.TreeNode;
import ar.com.profebot.exceptions.InvalidExpressionException;
import ar.com.profebot.services.ScannerService.TOKEN;

public class ParserService {
	
	ScannerService scannerService = null;
	
	public Tree parseExpression(String expression) throws InvalidExpressionException{
		Tree tree = new Tree();
		scannerService = new ScannerService(expression);
		
		TreeNode rootNode = new TreeNode("=");
		tree.setRootNode(rootNode);
		
		// Expresion = Expresion
		rootNode.setLeftNode(getExpression());
		scannerService.match(TOKEN.IGUAL);
		rootNode.setRightNode(getExpression());
		
		return tree;
	}

	/**
	 * Una expresion es una lista de terminos separados por + y -
	 * @return
	 * @throws InvalidExpressionException 
	 */
	private TreeNode getExpression() throws InvalidExpressionException {
		
		// En principio debe tener al menos 1 termino
		TreeNode expression = getTermino();
		
		// Si al procesar el termino sigue una suma o resta,
		// coloca el operador como raiz y el proximo termino a la derecha
		while(TOKEN.SUMA.equals(scannerService.prox_token()) || TOKEN.RESTA.equals(scannerService.prox_token())){
	        
			TreeNode operacionAditiva = new TreeNode(scannerService.getResultado()); // El operador escaneado
			operacionAditiva.setLeftNode(expression); // el termino inicial queda a la izquierda
			expression = operacionAditiva; // El operador pasa a ser la raiz de la expresion
			
			scannerService.match(scannerService.prox_token()); // Descarto el operador
			operacionAditiva.setRightNode(getTermino()); // El nuevo termino a la derecha
	    }
		return expression;
	}

	/**
	 * Una termino es una lista de primarias (X o Constante) separadas por * y /
	 * @return
	 * @throws InvalidExpressionException
	 */
	private TreeNode getTermino() throws InvalidExpressionException {

		// En principio debe tener al menos 1 primaria (CONSTANTE O X)
		TreeNode termino = getPrimaria();
		
		// Si al procesar la primaria sigue un producto o division,
		// coloca el operador como raiz y la proxima primaria a la derecha
		while(TOKEN.MULTIPLICACION.equals(scannerService.prox_token()) || TOKEN.DIVISION.equals(scannerService.prox_token())){
			TreeNode operacionMutiplicativa = new TreeNode(scannerService.getResultado()); // El operador escaneado
			operacionMutiplicativa.setLeftNode(termino); // la primaria inicial queda a la izquierda
			termino = operacionMutiplicativa; // El operador pasa a ser la raiz del termino
			
			scannerService.match(scannerService.prox_token()); // Descarto el operador
			operacionMutiplicativa.setRightNode(getPrimaria()); // La nueva primaria a la derecha
	    }
		return termino;
	}

	/**
	 * Una primaria es un termino basico, una X o CTE, o una expresion entera si esta ente parentesis
	 * @return
	 * @throws InvalidExpressionException
	 */
	private TreeNode getPrimaria() throws InvalidExpressionException {

		TreeNode primaria;
		TOKEN token = scannerService.prox_token();
		if (TOKEN.CONSTANTE.equals(token) || TOKEN.LETRA_X.equals(token)){
			primaria = new TreeNode(scannerService.getResultado());
			scannerService.match(token); // Descarto la primaria
			
			// Si sigue una potencia, tiene q venir una constante
	        if (TOKEN.POTENCIA.equals(scannerService.prox_token())){
	        	primaria = getPotencia(primaria);
	        }
	    }else if (TOKEN.RAIZ.equals(token)){
	    	scannerService.match(TOKEN.RAIZ);
	    	primaria = new TreeNode(scannerService.getResultado()); // El operador escaneado
	    	primaria.setLeftNode(new TreeNode("2")); // TODO definir si no es cuadrada
	    	primaria.setRightNode(getPrimaria());
	    }else{
	    	scannerService.match(TOKEN.PARENIZQUIERDO);
	    	primaria = getExpression();
	        scannerService.match(TOKEN.PARENDERECHO);
	        
	        // Si sigue una potencia, encierra el parentesis, tiene q venir una constante
	        if (TOKEN.POTENCIA.equals(scannerService.prox_token())){
				primaria = getPotencia(primaria);
	        }
	    }
		return primaria;
	}

	private TreeNode getPotencia(TreeNode primaria) throws InvalidExpressionException {
		
		scannerService.match(TOKEN.POTENCIA);
    	TreeNode operacionPotencia = new TreeNode(scannerService.getResultado()); // El operador escaneado
    	
    	scannerService.match(TOKEN.CONSTANTE);
    	operacionPotencia.setLeftNode(primaria); // la primaria inicial queda a la izquierda
    	operacionPotencia.setRightNode(new TreeNode(scannerService.getResultado())); // la constante
		return operacionPotencia;
	}
}
