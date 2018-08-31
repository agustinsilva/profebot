package ar.com.profebot.service;

import android.content.Context;
import android.widget.Toast;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolvePolynomialActivity;
import ar.com.profebot.intelligent.module.IAModuleClient;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class FactoringManager {

    // (exponente, coeficiente)
    public static Map<Integer, Double> polynomialTerms;
    // (raíz, multiplicidad)
    public static Map<Double, Integer> rootsMultiplicity;
    public static List<Double> roots;
    public static String rootsFactorized;
    public static String pendingPolynomial;
    public static Double currentRoot1;
    public static Double currentRoot2;
    public static String currentRootType;
    public static Boolean end;
    private static SolvePolynomialActivity context;

    public static void setContext(SolvePolynomialActivity context) {
        FactoringManager.context = context;
    }

    public static Map<Integer, Double> getPolynomialTerms() {
        return polynomialTerms;
    }

    public static void setPolynomialTerms(Map<Integer, Double> polynomialTerms) {
        FactoringManager.polynomialTerms = polynomialTerms;
        roots = new ArrayList<>();
        rootsMultiplicity = new HashMap<>();
        end = false;
    }

    public static MultipleChoiceStep nextStep(){
        Boolean factorComunIsPossible = true;
        Boolean cuadraticIsPossible = true;

        // Veo qué casos son posibles

        for(Integer exponent : polynomialTerms.keySet()){
            if(exponent <= 1){
                factorComunIsPossible = false;
                break;
            }
        }

        Boolean anyExponentIs2 = false;
        Boolean firstTime = true;
        for(Integer exponent : polynomialTerms.keySet()){
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

        return new MultipleChoiceStep(getEquation(), "", "", "", "",
                context.getString(R.string.FACTOR_COMUN), "",
                context.getString(R.string.CUADRATICA), "",
                context.getString(R.string.GAUSS), "",
                correctOption, regularOption1, regularOption2, "","",
                "" );
    }

    public static String getEquation(){
        String rootsFactorizedAux = "";
        if(!rootsFactorized.isEmpty()){
            rootsFactorizedAux = rootsFactorized;
            rootsFactorizedAux = rootsFactorizedAux.replace("x", "a_1");
            rootsFactorizedAux = FormulaParser.parseToLatex(rootsFactorizedAux).replace("{a}_{1}", "x");
            rootsFactorizedAux += !pendingPolynomial.isEmpty() ? "*" : "";
        }

        String pendingPolynomialAux = "";
        if(!pendingPolynomial.isEmpty()){
            pendingPolynomialAux = pendingPolynomial;
            pendingPolynomialAux = pendingPolynomialAux.replace("x", "a_1");
            pendingPolynomialAux = FormulaParser.parseToLatex(pendingPolynomialAux).replace("{a}_{1}", "x");
            pendingPolynomialAux = "\\mathbf{" + pendingPolynomialAux + "}";
        }

        return rootsFactorizedAux + pendingPolynomialAux;
    }

    public static String getEquationAfterFactorizing(){
        String rootsFactorizedAux = "";
        if(!rootsFactorized.isEmpty()){
            rootsFactorizedAux = rootsFactorized;
            rootsFactorizedAux = rootsFactorizedAux.replace("x", "a_1");
            rootsFactorizedAux = FormulaParser.parseToLatex(rootsFactorizedAux).replace("{a}_{1}", "x");
            rootsFactorizedAux = "\\mathbf{" + rootsFactorizedAux + "}" + (!pendingPolynomial.isEmpty() ? "*" : "");
        }

        String pendingPolynomialAux = "";
        if(!pendingPolynomial.isEmpty()){
            pendingPolynomialAux = pendingPolynomial;
            pendingPolynomialAux = pendingPolynomialAux.replace("x", "a_1");
            pendingPolynomialAux = FormulaParser.parseToLatex(pendingPolynomialAux).replace("{a}_{1}", "x");
            pendingPolynomialAux = pendingPolynomialAux;
        }

        return rootsFactorizedAux + pendingPolynomialAux;
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
            Double coefficient = polynomialTerms.get(exponent);
            String operator = "+";
            if(coefficient < 0 || firstTerm){
                operator = "";
            }
            stringBuilder.append(operator);
            stringBuilder.append(coefficient);
            if(exponent != 0){
                stringBuilder.append("*x");
                if(exponent > 1){
                    stringBuilder.append("^");
                    stringBuilder.append(exponent);
                }
            }
            firstTerm = false;
        }

        // Polinomio a factorizar
        String firstSign = "";
        String equation = stringBuilder.toString().trim();
        if(equation.isEmpty()){
            pendingPolynomial = "";
        }else{
            if (equation.substring(0,1).matches("-")){
                firstSign = "-";
                equation = equation.substring(1);
            }
            pendingPolynomial = firstSign + equation;
            if(!roots.isEmpty()){
                pendingPolynomial = "(" + pendingPolynomial + ")";
            }
        }

        // Raíces ya calculadas
        stringBuilder = new StringBuilder("");
        for(int i = 0 ; i < roots.size() ; i++){
            if(roots.get(i) == 0){
                stringBuilder.append("x");
            }else{
                stringBuilder.append("(x");
                Double root = roots.get(i);
                stringBuilder.append(root >= 0 ? "-" : "+");
                stringBuilder.append(Math.abs(root));
                stringBuilder.append(")");
            }

            if(rootsMultiplicity.get(roots.get(i)) > 1){
                stringBuilder.append("^");
                stringBuilder.append(rootsMultiplicity.get(roots.get(i)));
            }

            if(i + 1 < roots.size()){
                stringBuilder.append("*");
            }
        }
        rootsFactorized = stringBuilder.toString();
    }

    public static void factorizeBy(Integer option){
        initializeVariables();
        switch (option){
            case 1:
                applyCommonFactor();
                break;
            case 2:
                applyQuadratic();
                break;
            default:
                applyGauss();
        }

        if(polynomialTerms.isEmpty() || Collections.max(polynomialTerms.keySet()) <= 1 || cantFactorizeAnymore()){
            end = true;
        }
    }

    private static Boolean cantFactorizeAnymore(){
        if(getDegree() != 2){
            return false;
        }

        Double a = polynomialTerms.containsKey(2) ? polynomialTerms.get(2) : 0.0;
        Double b = polynomialTerms.containsKey(1) ? polynomialTerms.get(1) : 0.0;
        Double c = polynomialTerms.containsKey(0) ? polynomialTerms.get(0) : 0.0;

        Double discriminant = b * b - 4 * a * c;
        return discriminant < 0;
    }

    private static void initializeVariables(){
        currentRoot1 = null;
        currentRoot2 = null;
        currentRootType = "";
    }

    private static void applyCommonFactor(){
        Integer minExponent = Collections.min(polynomialTerms.keySet());

        List<Integer> exponents = new ArrayList<>(polynomialTerms.keySet());
        for(Integer exponent : exponents){
            polynomialTerms.put(exponent - minExponent, polynomialTerms.get(exponent));
            polynomialTerms.remove(exponent);
        }

        roots.add(0.0);
        rootsMultiplicity.put(0.0, minExponent);

        currentRoot1 = 0.0;
        currentRootType = getMultiplicityName(minExponent);
    }

    private static void applyQuadratic(){
        Double a = polynomialTerms.containsKey(2) ? polynomialTerms.get(2) : 0.0;
        Double b = polynomialTerms.containsKey(1) ? polynomialTerms.get(1) : 0.0;
        Double c = polynomialTerms.containsKey(0) ? polynomialTerms.get(0) : 0.0;

        Double discriminant = b * b - 4 * a * c;

        if(discriminant < 0){
            end = true;
        }else{
            Double root1 = (-1 * b + Math.sqrt(discriminant)) / (2 * a);
            Double root2 = (-1 * b - Math.sqrt(discriminant)) / (2 * a);

            if(root1.equals(root2)){
                currentRoot1 = root1;
                roots.add(root1);
                rootsMultiplicity.put(root1, 2);
            }else{
                currentRoot1 = root1;
                currentRoot2 = root2;
                roots.add(root1);
                rootsMultiplicity.put(root1, 1);
                roots.add(root2);
                rootsMultiplicity.put(root2, 1);
            }
        }

        polynomialTerms = new HashMap<>();
        if(a != 1.0){
            // Cuando se factoriza cuadrática, el resultado es: a*(x-r1)(x-r2)
            polynomialTerms.put(0, a);
        }
        end = true;
    }

    private static void applyGauss(){
        if(hasIndependentTerm()){
            Double independentTerm = polynomialTerms.get(0);
            Double principalCoefficient = polynomialTerms.get(getDegree());

            List<Integer> independentTermDivisors = divisorsOf(independentTerm);
            List<Integer> principalCoefficientDivisors = divisorsOf(principalCoefficient);

            List<Double> possibleRoots = new ArrayList<>();
            for(Integer independentTermDivisor : independentTermDivisors){
                for(Integer principalCoefficientDivisor : principalCoefficientDivisors){
                    possibleRoots.add((double) independentTermDivisor / principalCoefficientDivisor);
                }
            }

            for(Double possibleRoot : possibleRoots){
                if(isRoot(possibleRoot)){
                    try{
                        applyRuffini(possibleRoot);
                    }catch (Exception e){
                        System.out.println(e.getMessage() + " - Raiz: " + possibleRoot);
                    }
                }
            }
        }
    }

    private static void applyRuffini(Double possibleRoot) throws Exception{
        // Genero el listado ordenado y completo de coeficientes

        List<Double> coefficientsSortedAndCompleted = new ArrayList<>();
        for(int exponent = getDegree() ; exponent >= 0  ; exponent--){
            Double coefficientToAdd;
            if(polynomialTerms.containsKey(exponent)){
                coefficientToAdd = polynomialTerms.get(exponent);
            }else{
                coefficientToAdd = 0.0;
            }
            coefficientsSortedAndCompleted.add(coefficientToAdd);
        }

        // Hago la división

        List<Double> quotient = new ArrayList<>();
        quotient.add((double)coefficientsSortedAndCompleted.get(0));

        for(int i = 1 ; i < coefficientsSortedAndCompleted.size() ; i++){
            Double newCoefficient =  (quotient.get(i - 1) * possibleRoot) + coefficientsSortedAndCompleted.get(i);
            quotient.add(newCoefficient);
        }

        // Valido el resto

        if(quotient.get(quotient.size() - 1) != 0.0){
            throw new Exception("El resto de Ruffini no es 0 --> es " + quotient.get(quotient.size() - 1));
        }

        // Genero la nueva raíz

        roots.add(possibleRoot);
        rootsMultiplicity.put(possibleRoot, 1);

        currentRoot1 = possibleRoot;
        currentRootType = getMultiplicityName(1);

        // Genero el nuevo polinomio a factorizar

        Integer currentDegree = getDegree() - 1;
        polynomialTerms = new HashMap<>();
        for(int i = 0 ; i < quotient.size() - 1 ; i++){
            polynomialTerms.put(currentDegree--, quotient.get(i));
        }
    }

    private static Integer getDegree(){
        return Collections.max(polynomialTerms.keySet());
    }

    private static Boolean isRoot(Double possibleRoot){
        Double result = 0.0;
        for(Integer exponent : polynomialTerms.keySet()){
            result += polynomialTerms.get(exponent) * Math.pow(possibleRoot, exponent);
        }
        return result.equals(0.0);
    }

    private static List<Integer> divisorsOf(Double number){
        List<Integer> divisors = new ArrayList<>();
        divisors.add(1);
        divisors.add(-1);
        for(int i = 2 ; i <= number / 2 ; i++){
            if(number % i == 0){
                divisors.add(i);
                divisors.add(-1 * i);
            }
        }
        return divisors;
    }

    private static Boolean hasIndependentTerm(){
        return polynomialTerms.containsKey(0);
    }

    private static String getMultiplicityName(Integer multiplicity){
        switch (multiplicity){
            case 1:
                return "simple";
            case 2:
                return "doble";
            case 3:
                return "triple";
            case 4:
                return "cuádruple";
            default:
                return "múltiple";
        }
    }

    public static String getMessageOfRightOption(Integer option){
        String answer;
        switch (option){
            case 1:
                answer = "" + context.getText(R.string.FACTOR_COMUN_ES_EL_CORRECTO);
                return  answer
                        .replace("/raiz/", "" + currentRoot1)
                        .replace("/tipo/", currentRootType);
            case 2:
                if(currentRoot2 == null){
                    answer = "" + context.getText(R.string.CUADRATICA_RAIZ_DOBLE_ES_EL_CORRECTO);
                    return answer.replace("/raiz/", "" + currentRoot1);
                }
                answer = "" + context.getText(R.string.CUADRATICA_RAICES_SIMPLES_ES_EL_CORRECTO);
                return answer
                        .replace("/raiz1/", "" + currentRoot1)
                        .replace("/raiz2/", "" + currentRoot2);
            default:
                answer = "" + context.getText(R.string.GAUSS_ES_EL_CORRECTO);
                return answer.replace("/raiz/", "" + currentRoot1);
        }
    }

    public static String getMessageOfRegularOptions(Integer regularOption1, Integer regularOption2){
        String regularOption1Text = regularOption1 == null ? "" : getMessageOfRegularOptionNotChosen(regularOption1);
        String regularOption2Text = regularOption2 == null ? "" : getMessageOfRegularOptionNotChosen(regularOption2);

        String answer = "";

        if(regularOption1Text.isEmpty() && regularOption2Text.isEmpty()){
            return answer;
        }

        if(!regularOption1Text.isEmpty()){
            answer += regularOption1Text;
        }

        if(!regularOption2Text.isEmpty()){
            answer += ". " + regularOption1Text;
        }

        return answer;
    }

    public static String getMessageOfRegularOptionNotChosen(Integer regularOption){
        switch (regularOption){
            case 2:
                return "" + context.getText(R.string.CUADRATICA_ERA_POSIBLE);
            default:
                return "" + context.getText(R.string.GAUSS_ERA_POSIBLE);
        }
    }

    public static String getMessageOfRegularOptionChosen(Integer regularOption){
        switch (regularOption){
            case 2:
                return "" + context.getText(R.string.CUADRATICA_ES_POSIBLE_PERO_NO_LO_MEJOR);
            default:
                String answer = "" + context.getText(R.string.GAUSS_ES_POSIBLE_PERO_NO_LO_MEJOR);
                return answer.replace("/raiz/", "" + currentRoot1);
        }
    }

    public static String getMessageOfRightOptionNotChosen(Integer correctOption){
        switch (correctOption){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN_ERA_EL_CORRECTO);
            case 2:
                return "" + context.getText(R.string.CUADRATICA_ERA_EL_CORRECTO);
            default:
                return "" + context.getText(R.string.GAUSS_ERA_EL_CORRECTO);
        }
    }

    public static String getMessageOfWrongOptionChosen(Integer optionChosen){
        switch (optionChosen){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN_NO_ES_CORRECTO);
            default:
                return "" + context.getText(R.string.CUADRATICA_NO_ES_CORRECTO);
        }
    }

    public static String getCaseNameFrom(Integer option){
        switch (option){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN);
            case 2:
                return "" + context.getText(R.string.CUADRATICA);
            default:
                return "" + context.getText(R.string.GAUSS);
        }
    }
}
