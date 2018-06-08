package ar.com.profebot.service;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;

public class ExpressionsManager {

    private static String equationDrawn;
    private static Tree treeOfExpression;

    public static String getEquationDrawn() {
        return equationDrawn;
    }

    public static void setEquationDrawn(String equationDrawn) {
        ExpressionsManager.equationDrawn = equationDrawn;
    }

    public static Tree getTreeOfExpression() {
        return treeOfExpression;
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
    }

    public static Boolean expressionDrawnIsValid(){
        try{
            setTreeOfExpression(new ParserService().parseExpression(mapToOurAlphabet()));
            return true;
        }catch (InvalidExpressionException e){
            equationDrawn = null;
            treeOfExpression = null;
            return false;
        }
    }

    public static void showInvalidEquationMessage(Context context){
        Toast toast = Toast.makeText(context,"Fijate si la ecuación está bien escrita!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String mapToOurAlphabet(){
        if(equationDrawn == null){
            return "";
        }

        String equationWellWritten = fixEquationFormat();

        return equationWellWritten
                .replaceAll("\\[", "(")
                .replaceAll("]", ")")
                .replaceAll(":", "/")
                .replaceAll(",", ".")
                .replaceAll("\\^\\(\\*\\)", "*") // After replacing [] by (), we must search ^(*)
                .replaceAll("x", "X")
                .replaceAll("×", "*")
                .replaceAll("√", "R");
    }

    public static String fixEquationFormat(){
        String equation = equationDrawn;

        for(int i = 0 ; i <= 9 ; i++){
            equation = equation.replaceAll("\\^[\\(\\[]" + i + "[\\)\\]]", "^" + i);
        }

        return equation;
    }
}
