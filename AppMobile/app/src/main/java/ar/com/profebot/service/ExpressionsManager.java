package ar.com.profebot.service;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;

public class ExpressionsManager {

    private static String expressionDrawn;
    private static Tree treeOfExpression;

    public static String getExpressionDrawn() {
        return expressionDrawn;
    }

    public static void setExpressionDrawn(String expressionDrawn) {
        ExpressionsManager.expressionDrawn = expressionDrawn;
    }

    public static Tree getTreeOfExpression() {
        return treeOfExpression;
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
    }

    public static Boolean expressionDrawnIsValid(){
        try{
            setTreeOfExpression(new ParserService().parseExpression("1+2=3"));
            return true;
        }catch (InvalidExpressionException e){
            return false;
        }
    }
}
