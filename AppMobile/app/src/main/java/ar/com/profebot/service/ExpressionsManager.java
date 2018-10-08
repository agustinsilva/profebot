package ar.com.profebot.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static String mapToLatexAndReplaceComparator(String infixEquation){
        return replaceComparatorWithMathViewTag(getEquationAsLatex(infixEquation));
    }

    public static String getEquationAsLatex(String infixEquation) {
        String firstSign, secondSign;
        System.out.println("Ecuación infija: " + infixEquation);
        String infixEquationCleaned = mapToOurAlphabet(infixEquation).replace("X", "x");
        System.out.println("Ecuación en nuestro alfabeto: " + infixEquationCleaned);

        if(infixEquationCleaned == null || infixEquationCleaned.isEmpty()){
            return "";
        }

        if(infixEquation.contains("\\")){
            System.out.println("La ecuación ya está en latex: " + infixEquation);
            return infixEquation;
        }

        String comparatorOperator = getRootOfEquation(infixEquation);
        if(comparatorOperator.isEmpty()){
            firstSign = "";
            if (infixEquationCleaned.substring(0,1).contains("-")){
                firstSign = "-";
                infixEquationCleaned = infixEquationCleaned.substring(1);
            }

            System.out.println("Expresión: " + infixEquationCleaned);
            return firstSign + parseToLatex(infixEquationCleaned);
        }

        String[] expressions = infixEquationCleaned.split(comparatorOperator);

        firstSign = "";
        if (expressions[0].substring(0,1).contains("-")){
            firstSign = "-";
            expressions[0] = expressions[0].substring(1);
        }
        secondSign = "";
        if (expressions[1].substring(0,1).contains("-")){
            secondSign = "-";
            expressions[1] = expressions[1].substring(1);
        }

        System.out.println("Ecuación 1: " + expressions[0]);
        System.out.println("Ecuación 2: " + expressions[1]);
        return firstSign
                + parseToLatex(expressions[0])
                + comparatorOperator
                + secondSign
                + parseToLatex(expressions[1]);
    }

    private static String replaceComparatorWithMathViewTag(String infixEquation){
        String newComparator;
        String currentComparator = getRootOfEquation(infixEquation);
        switch (currentComparator){
            case "<=":
                newComparator = " \\leqslant ";
                break;
            case ">=":
                newComparator = " \\geqslant ";
                break;
            case "<":
                newComparator = " \\lt ";
                break;
            case ">":
                newComparator =" \\gt ";
                break;
            case "!=":
                newComparator = " \\neq ";
                break;
            case "=":
                newComparator = "=";
                break;
            default:
                newComparator = "";
                break;
        }

        return infixEquation.replace(currentComparator, newComparator);
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

    public static String getEquationAsString() {
        return treeOfExpression.toExpression();
    }

    public static Tree getTreeOfExpression() {
        return treeOfExpression;
    }

    public static void setTreeOfExpression(Tree treeOfExpression) {
        ExpressionsManager.treeOfExpression = treeOfExpression;
        comparatorOperator = getRootOfEquation(equationDrawn != null ? equationDrawn : (equationPhoto != null ? equationPhoto : ""));
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

    public static String mapPhotoToOurAlphabet(String equationPhoto) {
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

    private static String mapToOurAlphabet(String equation){
        if(equation == null){
            return "";
        }

        String equationWellWritten = fixEquationFormat(equation);
        
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

                .replaceAll("^\\+\\(", "(")

                .replaceAll("^\\+X", "X")

                .replaceAll("\\(\\+", "(")

                .replaceAll("=\\+\\(", "=(")

                .replaceAll("=\\+X", "=X")

                .replaceAll("≤", "<=")
                .replaceAll("≥", ">=")

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
                    .replaceAll("\\)" + i, ")*" + i)
                    .replaceAll(i + "x", i + "*x")
                    .replaceAll(i + "X", i + "*X");
        }

        if(!equationDrawn.isEmpty()){
            String comparator = getRootOfEquation(equationDrawn);
            String posibleParOrdenado = equationDrawn;
            if(!comparator.isEmpty()){
                String[] members = equationDrawn.split(comparator);
                if(members.length == 2){
                    posibleParOrdenado = equationDrawn.split(comparator)[1];
                }
            }

            if(posibleParOrdenado.charAt(0) == '['
                    && posibleParOrdenado.charAt(posibleParOrdenado.length() - 1) == ']'
                    && posibleParOrdenado.contains(",")){
                equationDrawn = equationDrawn.replace(",", ";");
            }
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
         }else if(infixEquation.contains("!=")){
             return "!=";
         }else if(infixEquation.contains("=")){
             return "=";
         }

         return "";
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
                .replaceAll("\\.0\\}", "}")
                .replaceAll("\\.0\\,", ",")
                .replaceAll("\\.0$", "");
    }

    private static String parseToLatex(String equation){
        // 1º: si el polinomio está entre paréntesis, removerlos
        Map<String, Integer> result  = removeGlobalBrackets(equation);
        String equationCleaned = new ArrayList<>(result.keySet()).get(0);
        Integer countOfGlobalBrackets = result.get(equationCleaned);

        // 2º: si el polinomio tiene variables X elevadas, reemplazar las X por (a_1)
        equationCleaned = equationCleaned
                .replace("x^", "(a_1)^")
                .replace("x", "a_1")
                .replace(";-", "+b_1-")
                .replace(";", "+b_1+")
        ;

        // 3º: si el polinomio empieza con signo (-), removerlo
        String firstSign = "";
        if(equationCleaned.substring(0, 1).contains("-")){
            firstSign = "-";
            equationCleaned = equationCleaned.substring(1);
        }

        //equationCleaned = equationCleaned.replace("(-(", "((-1)*(");
        equationCleaned = equationCleaned.replace("(-", "((-1)*");
        System.out.println("Expresión a parsear a latex: " + equationCleaned);

        String latex = FormulaParser.parseToLatex(equationCleaned);
        System.out.println("Expreisón parseada a latex: " + latex);

        latex = firstSign + latex
                .replace("{a}_{1}", "x")
                .replace("+{b}_{1}-", ";-")
                .replace("+{b}_{1}+", ";")
                .replace("\\left(x\\right)", "x")
                .replace("\\left(\\left(-1\\right)\\cdot ", "\\left(-")
                .replace("\\left(-1\\right)\\cdot ", "-");
        System.out.println("Latex con variable X: " + latex);

        for(int i = 0; i < countOfGlobalBrackets; i++){
            latex = "(" + latex + ")";
        }
        System.out.println("Latex a retornar: " + latex);
        return latex;
    }

    private static Map<String, Integer> removeGlobalBrackets(String equation){
        String equationCleaned = equation;
        Integer count = 0;
        // Si hay un ";" implica un par ordenado o un intervalo
        while(hasGlobalBrackets(equationCleaned) && (noInconsistentBracket(equationCleaned) || equation.contains(";"))){
            equationCleaned = equationCleaned.substring(1, equationCleaned.length() - 1);
            count++;
        }

        Map<String, Integer> result = new HashMap<>();
        result.put(equationCleaned, count);
        return result;
    }

    private static Boolean hasGlobalBrackets(String equation){
         return equation.length() > 2
                 && equation.substring(0, 1).contains("(")
                 && equation.substring(equation.length() -1, equation.length()).contains(")");
    }

    private static Boolean noInconsistentBracket(String equation){
         String equationWithTags = equation.substring(1, equation.length());

         equationWithTags = equationWithTags
                 .replace("(", "(A")
                 .replace(")", ")C");

         while(equationWithTags.contains("(A") && equationWithTags.contains(")C")){
             equationWithTags = equationWithTags
                     .replaceFirst("\\(A", "(")
                     .replaceFirst("\\)C", ")");
         }

         return !equationWithTags.contains("(A") && !equationWithTags.contains(")C");
    }

    public static Map<Integer, Double> parsePolinomialToHashMap(String equation) {
        Map<Integer, Double> polynomialMap = new HashMap<>();
        equation = equation.replaceAll("-", "+!").replaceAll("\\s+", "").replaceAll("X","x").replaceAll("\\*","");;
        String[] terms = equation.split("\\+");

        for (String term : terms) {
            if (!term.isEmpty()) {
                String potential, coefficient;
                if (term.contains("x^")) {
                    int position = term.indexOf("x^");
                    switch (position) {
                        case 1:
                            if (term.substring(0, position).contains("!")) {
                                coefficient = "-1";
                            } else {
                                coefficient = term.substring(0, position);
                            }
                            break;
                        case 0:
                            coefficient = "1";
                            break;
                        default:
                            coefficient = term.substring(0, position).replace("!", "-");
                            break;
                    }
                    potential = term.substring(position + 2, term.length());
                    coefficient = coefficient.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\)", "").replaceAll("\\(", "");
                    potential = potential.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\)", "").replaceAll("\\(", "");
                    polynomialMap = AddTerm(potential, coefficient, polynomialMap);
                } else if (term.contains("x")) {//es un coeficeinte lineal
                    int positionLineal = term.indexOf("x");
                    switch (positionLineal) {
                        case 1:
                            if (term.substring(0, positionLineal).contains("!")) {
                                coefficient = "-1";
                            } else {
                                coefficient = term.substring(0, positionLineal);
                            }
                            break;
                        case 0:
                            coefficient = "1";
                            break;
                        default:
                            coefficient = term.substring(0, positionLineal).replace("!", "-");
                            break;
                    }
                    polynomialMap = AddTerm("1", coefficient, polynomialMap);
                } else {//es un termino independiente
                    if (term.contains("!")) {
                        term = term.replaceAll("!","-");
                    }
                    polynomialMap = AddTerm("0", term, polynomialMap);
                }
            }
        }
        return polynomialMap;
    }
    private static Map<Integer, Double> AddTerm(String potential, String coefficient, Map<Integer, Double> polynomialMap) {
        Double newCoefficient;
        if(!polynomialMap.containsKey(Integer.parseInt(potential))){
            newCoefficient = fractionToDouble(coefficient);
        }else {
            newCoefficient = polynomialMap.get(Integer.parseInt(potential)) + fractionToDouble(coefficient);
            polynomialMap.remove(potential);
        }
        if(newCoefficient != 0){
            polynomialMap.put(Integer.parseInt(potential), newCoefficient);
        }
        return polynomialMap;
    }

    public static Double fractionToDouble(String fraction) {
        Double d = null;
        if (fraction != null) {
            if (fraction.contains("/")) {
                String[] numbers = fraction.split("/");
                if (numbers.length == 2) {
                    Double d1 = Double.valueOf(numbers[0]);
                    Double d2 = Double.valueOf(numbers[1]);
                    d = Double.parseDouble(String.format("%.2f", d1/d2).replace(",","."));
                }
            }
            else {
                d = Double.parseDouble(fraction);
            }
        }
        if (d == null) {
        }
        return d;
    }

}
