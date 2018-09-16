package ar.com.profebot.service;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.com.profebot.intelligent.module.IAModuleClient;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class ExpressionsManager {

    private static String equationDrawn;
    private static String equationPhoto;
    private static String equationPolinomial;
    private static Tree treeOfExpression;
    public static String comparatorOperator;

    public static String getEquationDrawn() {
        return equationDrawn;
    }

    public static String getEquationPhoto() {
        return equationPhoto;
    }
    public static String getPolinomialEquation() {
        return equationPolinomial;
    }

    public static void setEquationPhoto(String equationPhoto, Context context) {
        ExpressionsManager.equationPhoto = null;
        ExpressionsManager.equationPhoto = mapPhotoToOurAlphabet(equationPhoto);
        try{
            setTreeOfExpression(new ParserService().parseExpression(getEquationPhoto()));
        }catch (InvalidExpressionException e){
            equationDrawn = null;
            treeOfExpression = null;
        }
    }

    public static void setPolinomialEquation(String equationPhoto, Context context) {
        ExpressionsManager.equationPolinomial = null;
        ExpressionsManager.equationPolinomial = mapPhotoToOurAlphabet(equationPhoto);
        try{
            setTreeOfExpression(new ParserService().parseExpression(getPolinomialEquation()));
        }catch (InvalidExpressionException e){
            equationPolinomial = null;
            treeOfExpression = null;
        }
    }

    public static void setEquationDrawn(String equationDrawn) {
        ExpressionsManager.equationDrawn = mapToOurAlphabet(equationDrawn);
    }

    public static String getEquationAsInfix(){
        return treeOfExpression.toExpression()
                .replaceAll("R", "sqrt")
                .replaceAll("X", "x");
    }

    public static String getEquationAsInfix(String infixEquation){
        return infixEquation
                .replaceAll("R", "sqrt")
                .replaceAll("X", "x");
    }

    public static String getEquationAsLatex() {
        String firstSign, secondSign;
        String infixEquation = getEquationAsInfix();

        String[] expressions = infixEquation.split(comparatorOperator);

        if (expressions[0].substring(0,1).contains("-")){
            firstSign = "-";
            expressions[0] = expressions[0].substring(1);
        } else {
            firstSign = "";
        }
        if (expressions[1].substring(0,1).contains("-")){
            secondSign = "-";
            expressions[1] = expressions[1].substring(1);
        } else {
            secondSign = "";
        }

        return firstSign + FormulaParser.parseToLatex(expressions[0]) + comparatorOperator + secondSign + FormulaParser.parseToLatex(expressions[1]);
    }

    public static String getPolinomialEquationAsLatex() {
        String firstSign;
        String infixEquation = getEquationAsInfix();
        String[] expressions = infixEquation.split("=");
        if (expressions[0].substring(0,1).contains("-")){
            firstSign = "-";
            expressions[0] = expressions[0].substring(1);
        } else {
            firstSign = "";
        }
        return firstSign + FormulaParser.parseToLatex(expressions[0]);
    }

    public static String getEquationAsLatex(String infixEquation) {
        String[] expressions = getEquationAsInfix(infixEquation).split(comparatorOperator);
        return FormulaParser.parseToLatex(expressions[0]) + comparatorOperator + FormulaParser.parseToLatex(expressions[1]);
    }

    public static String getEquationAsString() {
        return treeOfExpression.toExpression();
    }

    public static Tree getTreeOfExpression() {
        return treeOfExpression;
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
        comparatorOperator = getRootOfEquation(equationDrawn != null ? equationDrawn : equationPhoto);
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
        equationPhoto = equationPhoto.replaceAll("\\s+", "");
        equationPhoto = containsFrac(equationPhoto);

        String equationWellWritten  = equationPhoto
                .replaceAll("\\\\leq", "<=")
                .replaceAll("\\\\geq", ">=")
                .replaceAll("\\\\cdot", "*")
                .replaceAll("\\\\sqrt", "R")
                .replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\\[", "(")
                .replaceAll("\\]", ")")
                .replaceAll("\\[", "(")
                .replaceAll("]", ")")
                .replaceAll("\\)\\(", ")*(")
                .replaceAll("\\.\\(", "*(")
                .replaceAll("\\)\\.", ")*")
                .replaceAll(":", "/")
                .replaceAll(",", ".")
                .replaceAll("\\^\\(\\*\\)", "*") // After replacing [] by (), we must search ^(*)
                .replaceAll("x", "X")
                .replaceAll("\\.X", "*X")
                .replaceAll("X\\.", "X*")
                .replaceAll("\\)X", ")*X")
                .replaceAll("^\\+\\(", "0+(")
                .replaceAll("^-\\(", "0-(")
                .replaceAll("^\\+X", "X")
                .replaceAll("^-X", "0-X")
                .replaceAll("\\(\\+", "(0+")
                .replaceAll("\\(-", "(0-")
                .replaceAll("=\\+\\(", "=(")
                .replaceAll("=-\\(", "=0-(")
                .replaceAll("=\\+X", "=X")
                .replaceAll("=-X", "=0-X")
                .replaceAll("×", "*")
                .replaceAll("√", "R")
                .replaceAll("sqrt", "R")
                .replaceAll("e", "2.718281828459045235360")
                .replaceAll("pi", "3.14159265358979323846")
                .replaceAll("\\.0", "");

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

    public static String containsFrac(String equationFrac) {
        if (equationFrac.contains("\\frac{")) {
            int inicioEquation=0;
            int finEquation=0;
            String fraccion="";
            while (equationFrac.contains("\\frac{")) {
                int cierreClosure = closingParen(equationFrac, equationFrac.indexOf("\\frac{") + 5);
                String numerador = equationFrac.substring(equationFrac.indexOf("\\frac{") + 6, cierreClosure);
                String denominador = equationFrac.substring(cierreClosure + 2, closingParen(equationFrac, cierreClosure + 1));
                inicioEquation = equationFrac.indexOf("\\frac{");
                finEquation = closingParen(equationFrac, cierreClosure + 1);
                //denominador = denominador.substring(denominador.indexOf("{"), denominador.lastIndexOf("}"));
                fraccion = "(" + numerador + ")/(" + denominador + ")";
                equationFrac = equationFrac.replace(equationFrac.substring(inicioEquation,finEquation+1),fraccion);
            }
            return equationFrac;
        }
        else{
            return equationFrac;
        }
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
                .replaceAll("\\)\\(", ")*(")
                .replaceAll("\\.\\(", "*(")
                .replaceAll("\\)\\.", ")*")
                .replaceAll(":", "/")
                .replaceAll(",", ".")
                .replaceAll("\\^\\(\\*\\)", "*") // After replacing [] by (), we must search ^(*)
                .replaceAll("x", "X")

                .replaceAll("\\.X", "*X")
                .replaceAll("X\\.", "X*")
                .replaceAll("\\)X", ")*X")

                .replaceAll("^\\+\\(", "0+(")
                .replaceAll("^-\\(", "0-(")

                .replaceAll("^\\+X", "X")
                .replaceAll("^-X", "0-X")

                .replaceAll("\\(\\+", "(0+")
                .replaceAll("\\(-", "(0-")

                .replaceAll("=\\+\\(", "=(")
                .replaceAll("=-\\(", "=0-(")

                .replaceAll("=\\+X", "=X")
                .replaceAll("=-X", "=0-X")

                .replaceAll("×", "*")
                .replaceAll("√", "R")
                .replaceAll("sqrt", "R")
                .replaceAll("e", "2.718281828459045235360")
                .replaceAll("pi", "3.14159265358979323846")

                .replaceAll("\\.0", "");
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

    public static void requestNewExercises(String equationBase, String newEquationBase, Context context){
        List<String> paramsToSend = getTermAndContextFromReduction(equationBase, newEquationBase);
        IAModuleClient client = new IAModuleClient(paramsToSend.get(0), paramsToSend.get(1), paramsToSend.get(2), context);
        client.execute();
    }

    public static String getRootOfEquation(String infixEquation){
         if(infixEquation.contains("<=")){
             return "<=";
         }else if(infixEquation.contains(">=")){
             return ">=";
         }else if(infixEquation.contains("<")){
             return "<";
         }else if(infixEquation.contains(">")){
             return ">";
         }
        return "=";
    }

    public static List<String> getTermAndContextFromReduction(String equationBase, String newEquationBase){
        List<String> equationBaseMembers = Arrays.asList(equationBase.split(comparatorOperator));
        List<String> newEquationBaseMembers = Arrays.asList(newEquationBase.split(comparatorOperator));
        List<String> result = new ArrayList<>();
        result.add(comparatorOperator);

        if(isTermPassage(equationBaseMembers, newEquationBaseMembers)){
            result.add(longestEquationMember(equationBaseMembers));
            result.add(longestEquationMember(newEquationBaseMembers));
            return result;
        }

        String originalBase;
        String newBase;
        if(!counterPartIsEquals(equationBaseMembers, newEquationBaseMembers, 0)){
            originalBase = equationBaseMembers.get(0);
            newBase = newEquationBaseMembers.get(0);
        }else{
            originalBase = equationBaseMembers.get(1);
            newBase = newEquationBaseMembers.get(1);
        }

        String baseFixed = originalBase.replace("-", "+!");
        String newBaseFixed = newBase.replace("-", "+!");

        List<String> baseFixedTokens = Arrays.asList(baseFixed.split("\\+"));
        List<String> newBaseFixedTokens = Arrays.asList(newBaseFixed.split("\\+"));
        for(int i = 0 ; i < baseFixedTokens.size() ; i++){
            if(newBaseFixedTokens.contains(baseFixedTokens.get(i))){
                for(int j = 0 ; j < newBaseFixedTokens.size() ; j++){
                    if(newBaseFixedTokens.get(j).equals(baseFixedTokens.get(i))){
                        newBaseFixedTokens.set(j, "");
                    }
                    break;
                }
                baseFixedTokens.set(i, "");
            }
        }

        StringBuilder builder = new StringBuilder("");
        for(int i = 0 ; i < baseFixedTokens.size() ; i++){
            if(!baseFixedTokens.get(i).equals("")){
                builder.append(baseFixedTokens.get(i));
                if(i + 1 < baseFixedTokens.size()){
                    builder.append("+");
                }
            }
        }
        String term = builder.toString().replace("+!", "-");
        if(term.substring(term.length() - 1).equals("+")){
            term = term.substring(0, term.length() - 1);
        }

        result.add(term);
        result.add(originalBase);
        return result;
    }

    private static Boolean isTermPassage(List<String> equationBaseMembers, List<String> newEquationBaseMembers){
         return !counterPartIsEquals(equationBaseMembers, newEquationBaseMembers, 0) &&
                 !counterPartIsEquals(equationBaseMembers, newEquationBaseMembers, 1);
    }

    private static Boolean counterPartIsEquals(List<String> equationBaseMembers, List<String> newEquationBaseMembers, Integer position){
         return equationBaseMembers.get(position).equals(newEquationBaseMembers.get(position));
    }

    private static String longestEquationMember(List<String> baseMembers){
         return baseMembers.get(0).length() >= baseMembers.get(1).length() ? baseMembers.get(0) : baseMembers.get(1);
    }

    public static Boolean isQuadraticExpression(String expression){
        return expression.replace("X", "x")
                .matches(".*(\\(.*x.*\\))\\*(\\(.*x.*\\)).*|.*x\\*(\\(.*x.*\\)).*|.*(\\(.*x.*\\))\\*x.*|.*x\\*x.*|.*x\\^2.*|.*(\\(.*x.*\\))\\^2.*");
    }

    public static String removeDecimals(String expression){
        return expression.trim()
                .replaceAll("\\.0\\+", "+")
                .replaceAll("\\.0\\-", "-")
                .replaceAll("\\.0\\/", "/")
                .replaceAll("\\.0\\*", "*")
                .replaceAll("\\.0\\^", "^")
                .replaceAll("\\.0\\(", "(")
                .replaceAll("\\.0\\)", ")")
                .replaceAll("\\.0x", "x")
                .replaceAll("\\.0 ", " ")
                .replaceAll("\\.0$", "");
    }
}
