package ar.com.profebot.service;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class ExpressionsManager {

    private static String equationDrawn;
    private static Tree treeOfExpression;

    public static String getEquationDrawn() {
        return equationDrawn;
    }

    public static void setEquationDrawn(String equationDrawn) {
        ExpressionsManager.equationDrawn = mapToOurAlphabet(equationDrawn);
    }

    public static String getEquationAsLatex() {
        String infixEquation = treeOfExpression.toExpression()
                .replaceAll("R", "sqrt")
                .replaceAll("X", "x");
        String[] expressions = infixEquation.split("=");
        return FormulaParser.parseToLatex(expressions[0]) + "=" + FormulaParser.parseToLatex(expressions[1]);
    }

    public static String getEquationAsString() {
        return treeOfExpression.toExpression();
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
    }

    public static Boolean expressionDrawnIsValid(){
        try{
            setTreeOfExpression(new ParserService().parseExpression(equationDrawn));
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

    private static String mapToOurAlphabet(String equationDrawn){
        if(equationDrawn == null){
            return "";
        }

        String equationWellWritten = fixEquationFormat(equationDrawn);
        
        return equationWellWritten
                .replaceAll("\\[", "(")
                .replaceAll("]", ")")
                .replaceAll(":", "/")
                .replaceAll(",", ".")
                .replaceAll("\\^\\(\\*\\)", "*") // After replacing [] by (), we must search ^(*)
                .replaceAll("x", "X")
                .replaceAll("\\)X", ")*X")
                .replaceAll("×", "*")
                .replaceAll("√", "R")
                .replaceAll("e", "2.718281828459045235360")
                .replaceAll("pi", "3.14159265358979323846");
    }

    public static String fixEquationFormat(String equationDrawn){
        for(int i = 0 ; i <= 9 ; i++){
            equationDrawn = equationDrawn
                    .replaceAll("\\^[\\(\\[]" + i + "[\\)\\]]", "^" + i)
                    .replaceAll(i + "\\(", i + "*(")
                    .replaceAll(i + "x", i + "*x");
        }

        return equationDrawn;
    }
}
