package ar.com.profebot.service;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

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
            // TODO: convertir la expressionDrawn al formato que espera el parser
            setTreeOfExpression(new ParserService().parseExpression("1+2")); // TODO: usar la expresión corregida
            return true;
        }catch (InvalidExpressionException e){
            // TODO: nullizar los atributos en caso de ser inválida la expresión ingresada
            return false;
        }
    }

    public static void showInvalidEquationMessage(Context context){
        Toast toast = Toast.makeText(context,"Fijate si la ecuación está bien escrita!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
