package ar.com.profebot.service;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.intelligent.module.IAModuleClient;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class FactoringManager {

    public static Map<Integer, Integer> polynomialTerms;
    public static List<Integer> roots;
    public static String rootsFactorized;
    public static String pendingPolynomial;

    public static Map<Integer, Integer> getPolynomialTerms() {
        return polynomialTerms;
    }

    public static void setPolynomialTerms(Map<Integer, Integer> polynomialTerms) {
        FactoringManager.polynomialTerms = polynomialTerms;
        FactoringManager.roots = new ArrayList<>();
    }

    public static MultipleChoiceStep nextStep(){
        Boolean factorComunIsPossible = true;
        Boolean cuadraticIsPossible = true;
        Boolean gaussIsPossible = true;

        // Veo qué casos son posibles

        for(Integer exponent : polynomialTerms.values()){
            if(exponent <= 1){
                factorComunIsPossible = false;
                break;
            }
        }

        Boolean anyExponentIs2 = false;
        Boolean firstTime = true;
        for(Integer exponent : polynomialTerms.values()){
            if(firstTime && exponent == 2){
                anyExponentIs2 = true;
                firstTime = false;
            }

            if(exponent > 2){
                cuadraticIsPossible = false;
                break;
            }
        }
        cuadraticIsPossible = cuadraticIsPossible && anyExponentIs2;

        // Fomulo opciones correctas, regulares e incorrectas

        Integer correctOption = null;
        Integer regularOption1 = null;
        Integer regularOption2 = null;

        /**
         * factor comun: 1
         * cuadrática: 2
         * gauss: 3
         */

        if(!factorComunIsPossible && !cuadraticIsPossible){
            correctOption = 3;
        }else if (factorComunIsPossible){
            correctOption = 1;
            if(cuadraticIsPossible){
                regularOption1 = 2;
                regularOption2 = 3;
            }else{
                regularOption1 = 3;
            }
        }else if(cuadraticIsPossible){
            correctOption = 2;
            regularOption1 = 3;
        }

        setFactors();

        String rootsFactorizedAux = "";
        if(!rootsFactorized.isEmpty()){
            rootsFactorizedAux = rootsFactorizedAux.replace("x", "a_1");
            rootsFactorizedAux = FormulaParser.parseToLatex(rootsFactorizedAux).replace("{a}_{1}", "x");
            rootsFactorizedAux += "*";
        }

        String pendingPolynomialAux = pendingPolynomial;
        pendingPolynomialAux = pendingPolynomialAux.replace("x", "a_1");
        pendingPolynomialAux = FormulaParser.parseToLatex(pendingPolynomialAux).replace("{a}_{1}", "x");

        String equation = rootsFactorizedAux + "\\mathbf{" + pendingPolynomialAux + "}";

        return new MultipleChoiceStep(equation, "", "", "", "",
                "Factor común", "", "Fórmula cuadrática", "", "Método de Gauss",
                "", 1,1,1, "","",
                "" );
    }

    public static void setFactors(){
        List<Integer> exponents = new ArrayList<>(polynomialTerms.keySet());
        Collections.sort(exponents, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 >= o1 ? 1 : -1;
            }
        });

        StringBuilder stringBuilder = new StringBuilder("");
        Boolean firstTerm = true;
        for(Integer exponent : exponents){
            Integer coefficient = polynomialTerms.get(exponent);
            String operator = "+";
            if(coefficient < 0 || firstTerm){
                operator = "";
            }
            stringBuilder.append(operator);
            stringBuilder.append(polynomialTerms.get(exponent));
            stringBuilder.append("*x^");
            stringBuilder.append(exponent);
            firstTerm = false;
        }

        String firstSign = "";
        String equation = stringBuilder.toString().replaceAll("x\\^0","").trim();
        if (equation.substring(0,1).matches("-")){
            firstSign = "-";
            equation = equation.substring(1);
        } else {
            firstSign = "";
        }

        pendingPolynomial = "(" + firstSign + equation + ")";
        stringBuilder = new StringBuilder("");
        for(int i = 0 ; i < roots.size() ; i++){
            stringBuilder.append("(x-");
            stringBuilder.append(roots.get(i));
            stringBuilder.append(")");
            if(i + 1 < roots.size()){
                stringBuilder.append("*");
            }
        }
        rootsFactorized = stringBuilder.toString();
    }
}
