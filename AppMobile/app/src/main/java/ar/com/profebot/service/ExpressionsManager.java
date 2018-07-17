package ar.com.profebot.service;

import android.content.Context;
import android.widget.Toast;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class ExpressionsManager {

    private static String equationDrawn;
    private static String equationPhoto;
    private static Tree treeOfExpression;

    public static String getEquationDrawn() {
        return equationDrawn;
    }

    public static String getEquationPhoto() {
        return equationPhoto;
    }

    public static void setEquationPhoto(String equationPhoto, Context context) {
        ExpressionsManager.equationPhoto = null;
        ExpressionsManager.equationPhoto = mapPhotoToOurAlphabet(equationPhoto);
        try{
            setTreeOfExpression(new ParserService().parseExpression(getEquationPhoto()));
        }catch (InvalidExpressionException e){
            CharSequence text = "Se produjo un error en la expresion:" + e.getMessage();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            equationDrawn = null;
            treeOfExpression = null;
        }
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

    public static Tree getTreeOfExpression() {
        return treeOfExpression;
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
    }

    public static Boolean expressionDrawnIsValid(){
        try{
            setTreeOfExpression(new ParserService().parseExpression(equationDrawn));
            return true;
        }catch (Exception e){
            setEquationDrawn(null);
            setTreeOfExpression(null);
            return false;
        }
    }

    private static String mapPhotoToOurAlphabet(String equationPhoto) {
        String ecuacion;
        String subEcuacion;
        equationPhoto = equationPhoto.replaceAll("\\s+", "");
        if (equationPhoto.contains("rac{")) {
            String numerador = equationPhoto.substring(equationPhoto.indexOf("rac{") + 3, closingParen(equationPhoto,equationPhoto.indexOf("rac{")+3)+1);
            String denominador = equationPhoto.substring(equationPhoto.lastIndexOf(numerador) + numerador.length()) + "}";
            denominador = denominador.substring(denominador.indexOf("{"), denominador.lastIndexOf("}"));

            ecuacion = numerador + "/" + denominador;
            if (denominador.contains("rac{")) {
                while (denominador.contains("rac{")) {
                    String nuevoNumerador = denominador.substring(denominador.indexOf("rac{") + 3, denominador.indexOf("}")) + "}";
                    String nuevoDenominador = denominador.substring(denominador.lastIndexOf(nuevoNumerador) + nuevoNumerador.length());
                    subEcuacion = nuevoNumerador + "/" + nuevoDenominador;
                    denominador = nuevoDenominador;
                    ecuacion = numerador + "/{" + subEcuacion + "}";
                }
            } else {
                ecuacion = numerador + "/" + denominador;
            }
            equationPhoto = ecuacion;
        }
        String equationWellWritten  = equationPhoto
                .replaceAll("\\\\sqrt", "R")
                .replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\\[", "(")
                .replaceAll("\\]", ")")
                .replaceAll(":", "/")
                .replaceAll(",", ".")
                .replaceAll("\\^\\(\\*\\)", "*") // After replacing [] by (), we must search ^(*)
                .replaceAll("n", "X")
                .replaceAll("x", "X")
                .replaceAll("\\)X", ")*X")
                .replaceAll("\\(X\\(", "(X*(")
                .replaceAll("×", "*")
                .replaceAll("√", "R")
                .replaceAll("\\\\cdot", "*")
                .replaceAll("e", "2.718281828459045235360")
                .replaceAll("pi", "3.14159265358979323846");
        for(int i = 0 ; i <= 9 ; i++){
            equationWellWritten = equationWellWritten
                    .replaceAll("\\^[\\(\\[]" + i + "[\\)\\]]", "^" + i)
                    .replaceAll(i + "\\(", i + "*(")
                    .replaceAll(i + "\\[", i + "(")
                    .replaceAll(i + "]", i + ")")
                    .replaceAll(i + "x", i + "*x")
                    .replaceAll(i + "X", i + "*X");
        }
        return equationWellWritten;
    }
    public static int closingParen(String s, int n) {
        int counter = 0;
        char opening = '{';
        char closing = '}';
        int positionOfMatchingParen = -1;
        boolean found = false;

        while (n < s.length() && !found) {

            if (s.charAt(n) == (opening)) {
                counter++;
            } else if (s.charAt(n) == (closing)) {
                counter--;
                if (counter == 0) {
                    positionOfMatchingParen = n;
                    found = true;
                }
            }
            n++;
        }
        return positionOfMatchingParen;
    }

    public static java.util.ArrayList<String> result = new java.util.ArrayList<String>();

    public static int max=0;

     public static String removeInvalidParentheses(String s) {
        if(s==null)
            return java.util.Arrays.toString(result.toArray());

        dfs(s, "", 0, 0);
        if(result.size()==0){
            result.add("");
        }

        return result.get(0);
    }

    public static void dfs(String left, String right, int countLeft, int maxLeft){
        if(left.length()==0){
            if(countLeft==0 && right.length()!=0){
                if(maxLeft > max){
                    max = maxLeft;
                }

                if(maxLeft==max && !result.contains(right)){
                    result.add(right);
                }
            }

            return;
        }

        if(left.charAt(0)=='('){
            dfs(left.substring(1), right+"(", countLeft+1, maxLeft+1);//keep (
            dfs(left.substring(1), right, countLeft, maxLeft);//drop (
        }else if(left.charAt(0)==')'){
            if(countLeft>0){
                dfs(left.substring(1), right+")", countLeft-1, maxLeft);
            }

            dfs(left.substring(1), right, countLeft, maxLeft);

        }else{
            dfs(left.substring(1), right+String.valueOf(left.charAt(0)), countLeft, maxLeft);
        }
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
                    .replaceAll(i + "x", i + "*x")
                    .replaceAll(i + "X", i + "*X");
        }

        return equationDrawn;
    }
}
