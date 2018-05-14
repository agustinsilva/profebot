package ar.com.profebot.services;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.container.TokenData;
import ar.com.profebot.container.Tree;
import ar.com.profebot.container.TreeNode;
import ar.com.profebot.exceptions.InvalidExpressionException;
import ar.com.profebot.services.ScannerService.TOKEN;

public class TreeService {

	public Tree parseExpression(String expression) throws InvalidExpressionException {
		
		List<TokenData> tokens = (new ScannerService()).getExpressionList(expression);
		Tree tree = new Tree();
		
		// Busco el signo =
		int index = 0;
		for (TokenData tokenData : tokens) {
			if (TOKEN.IGUAL.equals(tokenData.getToken())){break;}
			index++;
		}
		
		List<TokenData> leftTokens = chopped(tokens, 0, index);
		List<TokenData> rightTokens = chopped(tokens, index+1, tokens.size());
		
		TreeNode rootNode = new TreeNode("=");
		tree.setRootNode(rootNode);
		rootNode.setLeftNode(getChildNodes(leftTokens));
		rootNode.setLeftNode(getChildNodes(rightTokens));
//		"x-3=3-x"
//		"x-3" "=" "3-x"
		
		return tree;
	}
	
	private TreeNode getChildNodes(List<TokenData> tokens) {
		
		TreeNode node = new TreeNode(null);

		// si queda 1 solo termino, es el mismo nodo
		if (tokens.size() == 1){
			node.setValue(tokens.get(0).getValue());
		}
		// TODO cargar subnodos dadas las condiciones
		
		return node;
	}

	private List<TokenData> chopped(List<TokenData> list, int fromIndex, int toIndex) {
	    return new ArrayList<TokenData>(list.subList(fromIndex, toIndex));
	}

}
