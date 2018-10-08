package ar.com.profebot.parser.service;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class FunctionParserService {

    public enum FunctionType{
        CONSTANT,
        LINEAR,
        HOMOGRAPHIC,
        QUADRATIC,
        INVALID
    }

    public static FunctionType getFunctionType(String function){

        Tree functionTree;
        FunctionType functionType;
        try {
            // F(X) = 0
            functionTree = (new ParserService()).parseExpression(function);
            // Left node is evaluated
            TreeNode functionNode = functionTree.getLeftNode();

            if (isConstant(functionNode)){
                functionType = FunctionType.CONSTANT;
            }else if (isLinear(functionNode)){
                functionType = FunctionType.LINEAR;
            }else if (isHomographic(functionNode)){
                functionType = FunctionType.HOMOGRAPHIC;
            }else if (isQuadratic(functionNode)){
                functionType = FunctionType.QUADRATIC;
            }else {
                functionType = FunctionType.INVALID;
            }
        } catch (Exception e) {
            functionType = FunctionType.INVALID;
        }
        return functionType;
    }

    public static Boolean isConstant(String function){
        try {
            Tree functionTree = (new ParserService()).parseExpression(function);
            // Left node is evaluated
            TreeNode functionNode = functionTree.getLeftNode();
            return isConstant(functionNode);
        } catch (InvalidExpressionException e) {
            return false;
        }
    }

    private static Boolean isConstant(TreeNode functionNode){
        // Non null node
        return functionNode != null &&
                !functionNode.toExpression().contains("X");
    }

    public static Boolean isLinear(String function){
        try {
            Tree functionTree = (new ParserService()).parseExpression(function);
            // Left node is evaluated
            TreeNode functionNode = functionTree.getLeftNode();
            return isLinear(functionNode);
        } catch (InvalidExpressionException e) {
            return false;
        }
    }

    private static Boolean isLinear(TreeNode functionNode){

        // Linear = ax + b

        // Non null node
        if (functionNode == null){return false;}

        // Remove parentesis
        if (functionNode.isParenthesis()){return isLinear(functionNode.getContent());}

        // Flatten
        TreeNode functionNodeFlattened = TreeUtils.flattenOperands(functionNode);

        // Root node must be adition
        if (!functionNodeFlattened.esSuma()){return false;}

        // All args must be symbols, constants, symboil fractiosn or constant fractions
        for(TreeNode node: functionNodeFlattened.getArgs()){

            // Constant fraction?
            if (TreeUtils.isConstantFraction(node)){continue;}

            // Symbol / Constant?
            if (TreeUtils.isSymbolFraction(node, false, 1)){continue;}

            // Constant?
            if (TreeUtils.isConstant(node)){continue;}

            // Symbol?
            if (TreeUtils.isSymbol(node, false, 1)){continue;}

            // No valid term found, not linear
            return false;
        }

        // is linear
        return true;
    }

    public static Boolean isHomographic(String function){
        try {
            Tree functionTree = (new ParserService()).parseExpression(function);
            // Left node is evaluated
            TreeNode functionNode = functionTree.getLeftNode();
            return isHomographic(functionNode);
        } catch (InvalidExpressionException e) {
            return false;
        }
    }

    private static Boolean isHomographic(TreeNode functionNode){

        // Homographic = Linear / Linear

        // Non null node
        if (functionNode == null){return false;}

        // Remove parentesis
        if (functionNode.isParenthesis()){return isHomographic(functionNode.getContent());}

        // Root node must be /
        if (!functionNode.esDivision()){return false;}

        // Left node and right node must be linear
        if (!isLinear(functionNode.getLeftNode())){return false;}
        return isLinear(functionNode.getRightNode());
    }

    public static Boolean isQuadratic(String function){
        try {
            Tree functionTree = (new ParserService()).parseExpression(function);
            // Left node is evaluated
            TreeNode functionNode = functionTree.getLeftNode();
            return isQuadratic(functionNode);
        } catch (InvalidExpressionException e) {
            return false;
        }
    }


    private static Boolean isQuadratic(TreeNode functionNode){

        // Linear = ax^2 + bx + c

        // Non null node
        if (functionNode == null){return false;}

        // Remove parentesis
        if (functionNode.isParenthesis()){return isQuadratic(functionNode.getContent());}

        // Flatten
        TreeNode functionNodeFlattened = TreeUtils.flattenOperands(functionNode);

        return TreeUtils.hasQuadraticNode(functionNodeFlattened);
    }

}
