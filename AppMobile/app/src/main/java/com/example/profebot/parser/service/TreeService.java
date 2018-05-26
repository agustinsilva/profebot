package com.example.profebot.parser.service;

import com.example.profebot.parser.container.TokenData;
import com.example.profebot.parser.container.Tree;
import com.example.profebot.parser.container.TreeNode;
import com.example.profebot.parser.exception.InvalidExpressionException;
import com.example.profebot.parser.utils.Token;

import java.util.ArrayList;
import java.util.List;

public class TreeService {

    public Tree parseExpression(String expression) throws InvalidExpressionException {

        List<TokenData> Tokens = (new ScannerService()).getExpressionList(expression);
        Tree tree = new Tree();

        // Busco el signo =
        int index = 0;
        for (TokenData TokenData : Tokens) {
            if (Token.IGUAL.equals(TokenData.getToken())){break;}
            index++;
        }

        List<TokenData> leftTokens = chopped(Tokens, 0, index);
        List<TokenData> rightTokens = chopped(Tokens, index+1, Tokens.size());

        TreeNode rootNode = new TreeNode("=");
        tree.setRootNode(rootNode);
        rootNode.setLeftNode(getChildNodes(leftTokens));
        rootNode.setLeftNode(getChildNodes(rightTokens));
//		"x-3=3-x"
//		"x-3" "=" "3-x"

        return tree;
    }

    private TreeNode getChildNodes(List<TokenData> Tokens) {

        TreeNode node = new TreeNode(null);

        // si queda 1 solo termino, es el mismo nodo
        if (Tokens.size() == 1){
            node.setValue(Tokens.get(0).getValue());
        }
        // TODO cargar subnodos dadas las condiciones

        return node;
    }

    private List<TokenData> chopped(List<TokenData> list, int fromIndex, int toIndex) {
        return new ArrayList<TokenData>(list.subList(fromIndex, toIndex));
    }

}
