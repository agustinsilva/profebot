package com.example.profebot.parser.service;

import com.example.profebot.parser.container.Tree;
import com.example.profebot.parser.container.TreeNode;
import com.example.profebot.parser.exception.InvalidExpressionException;
import com.example.profebot.parser.utils.Token;

public class ParserService {

    ScannerService scannerService = null;

    public Tree parseExpression(String expression) throws InvalidExpressionException{
        Tree tree = new Tree();
        scannerService = new ScannerService(expression);

        TreeNode rootNode = new TreeNode("=");
        tree.setRootNode(rootNode);

        // Expresion = Expresion
        rootNode.setLeftNode(getExpression());
        scannerService.match(Token.IGUAL);
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
        while(Token.SUMA.equals(scannerService.prox_Token()) || Token.RESTA.equals(scannerService.prox_Token())){

            TreeNode operacionAditiva = new TreeNode(scannerService.getResultado()); // El operador escaneado
            operacionAditiva.setLeftNode(expression); // el termino inicial queda a la izquierda
            expression = operacionAditiva; // El operador pasa a ser la raiz de la expresion

            scannerService.match(scannerService.prox_Token()); // Descarto el operador
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
        while(Token.MULTIPLICACION.equals(scannerService.prox_Token()) || Token.DIVISION.equals(scannerService.prox_Token())){
            TreeNode operacionMutiplicativa = new TreeNode(scannerService.getResultado()); // El operador escaneado
            operacionMutiplicativa.setLeftNode(termino); // la primaria inicial queda a la izquierda
            termino = operacionMutiplicativa; // El operador pasa a ser la raiz del termino

            scannerService.match(scannerService.prox_Token()); // Descarto el operador
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
        Token Token = scannerService.prox_Token();
        if (Token.CONSTANTE.equals(Token) || Token.LETRA_X.equals(Token)){
            primaria = new TreeNode(scannerService.getResultado());
            scannerService.match(Token); // Descarto la primaria

            // Si sigue una potencia, tiene q venir una constante
            if (Token.POTENCIA.equals(scannerService.prox_Token())){
                primaria = getPotencia(primaria);
            }
        }else if (Token.RAIZ.equals(Token)){
            scannerService.match(Token.RAIZ);
            primaria = new TreeNode(scannerService.getResultado()); // El operador escaneado
            primaria.setLeftNode(new TreeNode("2")); // TODO definir si no es cuadrada
            primaria.setRightNode(getPrimaria());
        }else{
            scannerService.match(Token.PARENIZQUIERDO);
            primaria = getExpression();
            scannerService.match(Token.PARENDERECHO);

            // Si sigue una potencia, encierra el parentesis, tiene q venir una constante
            if (Token.POTENCIA.equals(scannerService.prox_Token())){
                primaria = getPotencia(primaria);
            }
        }
        return primaria;
    }

    private TreeNode getPotencia(TreeNode primaria) throws InvalidExpressionException {

        scannerService.match(Token.POTENCIA);
        TreeNode operacionPotencia = new TreeNode(scannerService.getResultado()); // El operador escaneado

        scannerService.match(Token.CONSTANTE);
        operacionPotencia.setLeftNode(primaria); // la primaria inicial queda a la izquierda
        operacionPotencia.setRightNode(new TreeNode(scannerService.getResultado())); // la constante
        return operacionPotencia;
    }
}