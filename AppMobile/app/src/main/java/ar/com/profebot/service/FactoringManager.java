package ar.com.profebot.service;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolvePolynomialActivity;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class FactoringManager {

    public static final Integer FACTOR_COMUN = 1;
    public static final Integer CUADRATICA = 2;
    public static final Integer GAUSS = 3;

    // (exponente, coeficiente)
    public static Map<Integer, Double> polynomialTerms;
    // (raíz, multiplicidad)
    public static Map<Double, Integer> rootsMultiplicity;
    public static List<Double> roots;
    public static String rootsFactorized;
    public static String pendingPolynomial;
    public static Double currentRoot1;
    public static Double currentRoot2;
    public static Double multiplier;
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
        multiplier = 1.0;

        // Checkear si el polinomio ingresado es factorizable
        tryToFinishingExercise();
    }

    public static MultipleChoiceStep nextStep(){
        Boolean factorComunIsPossible = true;
        Boolean cuadraticIsPossible = true;

        // Veo qué casos son posibles

        factorComunIsPossible = commonFactorIsPossible();

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

        Integer correctOption;
        Integer regularOption1 = null;
        Integer regularOption2 = null;

        /**
         * factor comun: 1
         * cuadrática: 2
         * gauss: 3
         */

        if(!factorComunIsPossible && !cuadraticIsPossible){
            correctOption = GAUSS;
        }else if (factorComunIsPossible){
            correctOption = FACTOR_COMUN;
            if(cuadraticIsPossible){
                regularOption1 = CUADRATICA;
                regularOption2 = GAUSS;
            }else{
                regularOption1 = GAUSS;
            }
        }else{
            correctOption = CUADRATICA;
            regularOption1 = GAUSS;
        }

        setFactors();

        return new MultipleChoiceStep(getEquation(), "", "", "", "",
                context.getString(R.string.FACTOR_COMUN), "",
                context.getString(R.string.CUADRATICA), "",
                context.getString(R.string.GAUSS), "",
                correctOption, regularOption1, regularOption2, "","",
                "" );
    }

    private static Boolean commonFactorIsPossible(){
        Boolean factorComunIsPossible = true;
        if(!polynomialTerms.isEmpty() && polynomialTerms.get(getDegree()) == 1){ // Factor común numérico
            for(Integer exponent : polynomialTerms.keySet()){
                if(exponent <= 1){
                    factorComunIsPossible = false;
                    break;
                }
            }
        }
        return factorComunIsPossible;
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
            String firstSign = "";
            if(pendingPolynomialAux.substring(0, 1).contains("-")){
                firstSign = "-";
                pendingPolynomialAux = pendingPolynomialAux.substring(1);
            }
            pendingPolynomialAux = firstSign + FormulaParser.parseToLatex(pendingPolynomialAux).replace("{a}_{1}", "x");
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

            String firstSign = "";
            if(pendingPolynomialAux.substring(0, 1).contains("-")){
                firstSign = "-";
                pendingPolynomialAux = pendingPolynomialAux.substring(1);
            }
            pendingPolynomialAux = pendingPolynomialAux.replace("x", "a_1");
            pendingPolynomialAux = firstSign + FormulaParser.parseToLatex(pendingPolynomialAux).replace("{a}_{1}", "x");
        }

        return rootsFactorizedAux + pendingPolynomialAux;
    }

    public static void setFactors(){
        // Polinomio a factorizar
        pendingPolynomial = getPolynomialGeneralForm(polynomialTerms);
        if(!roots.isEmpty() && !pendingPolynomial.isEmpty()){
            pendingPolynomial = "(" + pendingPolynomial + ")";
            pendingPolynomial = (multiplier != 1 ? multiplier + "*" : "") + pendingPolynomial;
        }

        // Raíces ya calculadas
        StringBuilder stringBuilder = new StringBuilder("");
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

    public static String getPolynomialGeneralForm(Map<Integer, Double> terms){
        List<Integer> exponents = new ArrayList<>(terms.keySet());
        Collections.sort(exponents, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 >= o1 ? 1 : -1;
            }
        });

        StringBuilder stringBuilder = new StringBuilder("");
        Boolean firstTerm = true;
        for(Integer exponent : exponents){
            Double coefficient = terms.get(exponent);
            String operator = "+";
            if(firstTerm){
                if(coefficient > 0){
                    operator = "";
                }else{
                    operator = "-";
                }
            }else{
                if(coefficient < 0){
                    operator = "-";
                }
            }
            stringBuilder.append(operator);
            if(Math.abs(coefficient) == 1.0){
                if(exponent != 0){
                    stringBuilder.append("x");
                    if(exponent > 1){
                        stringBuilder.append("^");
                        stringBuilder.append(exponent);
                    }
                }else{
                    stringBuilder.append(Math.abs(coefficient));
                }
            }else{
                stringBuilder.append(Math.abs(coefficient));
                if(exponent != 0){
                    stringBuilder.append("*x");
                    if(exponent > 1){
                        stringBuilder.append("^");
                        stringBuilder.append(exponent);
                    }
                }
            }
            firstTerm = false;
        }
        return stringBuilder.toString().trim()
                .replaceAll("\\.0\\+", "+")
                .replaceAll("\\.0\\-", "-")
                .replaceAll("\\.0\\/", "/")
                .replaceAll("\\.0\\*", "*")
                .replaceAll("\\.0\\^", "^")
                .replaceAll("\\.0\\(", "(")
                .replaceAll("\\.0\\)", ")")
                .replaceAll("\\.0x", "x")
                .replaceAll("\\.0$", "");
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

        deleteNullTerms();

        tryToFinishingExercise();
    }

    private static void deleteNullTerms(){
        List<Integer> exponents = new ArrayList<>(polynomialTerms.keySet());
        for(Integer exponent : exponents){
            if(polynomialTerms.get(exponent) == 0.0){
                polynomialTerms.remove(exponent);
            }
        }
    }

    private static void tryToFinishingExercise(){
        Boolean existsTerms = !polynomialTerms.isEmpty();

        Boolean quadraticIsPossible = false;
        if(existsTerms && getDegree() == 2){
            Double a = polynomialTerms.containsKey(2) ? polynomialTerms.get(2) : 0.0;
            Double b = polynomialTerms.containsKey(1) ? polynomialTerms.get(1) : 0.0;
            Double c = polynomialTerms.containsKey(0) ? polynomialTerms.get(0) : 0.0;

            Double discriminant = b * b - 4 * a * c;
            quadraticIsPossible = discriminant >= 0;
        }

        Boolean gaussIsPossible = existsTerms && (getDegree() > 2 || (getDegree() == 2 && quadraticIsPossible));

        if(!commonFactorIsPossible() && !quadraticIsPossible && !gaussIsPossible){
            end = true;
            if(existsTerms && getDegree() <= 1){
                Double lastRoot = polynomialTerms.containsKey(0) ? -1 * polynomialTerms.get(0) : 0;
                addRoot(lastRoot);
            }
        }
    }

    private static void initializeVariables(){
        currentRoot1 = null;
        currentRoot2 = null;
        currentRootType = "";
    }

    private static void applyCommonFactor(){
        // Primero intento factor común numérico
        if(polynomialTerms.get(getDegree()) != 1){
            Double mainCoefficient = polynomialTerms.get(getDegree());
            for(Integer exponent : polynomialTerms.keySet()){
                polynomialTerms.put(exponent, polynomialTerms.get(exponent) / mainCoefficient);
            }
            multiplier = mainCoefficient;
        }else{
            Integer minExponent = Collections.min(polynomialTerms.keySet());

            List<Integer> exponents = new ArrayList<>(polynomialTerms.keySet());
            for(Integer exponent : exponents){
                polynomialTerms.put(exponent - minExponent, polynomialTerms.get(exponent));
                polynomialTerms.remove(exponent);
            }

            for(int i = 0 ; i < minExponent ; i++){
                addRoot(0.0);
            }

            currentRoot1 = 0.0;
            currentRootType = getMultiplicityName(minExponent);
        }

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
                // Agrego 2 veces la misma raíz, porque es doble
                addRoot(root1);
                addRoot(root1);
            }else{
                currentRoot1 = root1;
                currentRoot2 = root2;
                addRoot(root1);
                addRoot(root2);
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
                        break;
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

        addRoot(possibleRoot);

        currentRoot1 = possibleRoot;
        currentRootType = getMultiplicityName(1);

        // Genero el nuevo polinomio a factorizar

        Integer currentDegree = getDegree() - 1;
        polynomialTerms = new HashMap<>();
        for(int i = 0 ; i < quotient.size() - 1 ; i++){
            polynomialTerms.put(currentDegree--, quotient.get(i));
        }
    }

    private static void addRoot(Double root){
        if(roots.contains(root)){
            Integer multiplicity = rootsMultiplicity.get(root);
            rootsMultiplicity.remove(root);
            rootsMultiplicity.put(root, multiplicity + 1);
        }else{
            roots.add(root);
            rootsMultiplicity.put(root, 1);
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
