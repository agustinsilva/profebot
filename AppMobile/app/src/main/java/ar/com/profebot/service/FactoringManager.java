package ar.com.profebot.service;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.EnterPolinomialEquationOptionsActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import io.github.kexanie.library.MathView;

public class FactoringManager {

    public static final Integer FACTOR_COMUN = 1;
    public static final Integer CUADRATICA = 2;
    public static final Integer GAUSS = 3;

    public static final String CORRECT_OPTION = "correctOption";
    public static final String REGULAR_OPTION_1 = "regularOption1";
    public static final String REGULAR_OPTION_2 = "regularOption2";

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

    private static String originalPolynomial;

    private static AlertDialog dialog;

    public static void setContext(SolvePolynomialActivity context) {
        FactoringManager.context = context;
    }

    public static Map<Integer, Double> getPolynomialTerms() {
        return polynomialTerms;
    }

    public static void setPolynomialTerms(Map<Integer, Double> polynomialTerms) {
        FactoringManager.polynomialTerms = polynomialTerms;
        originalPolynomial = getOriginalPolynomial(polynomialTerms);
        roots = new ArrayList<>();
        rootsMultiplicity = new HashMap<>();
        end = false;
        multiplier = 1.0;

        // Checkear si el polinomio ingresado es factorizable
        tryToFinishingExercise();
    }

    private static String getOriginalPolynomial(Map<Integer, Double> polynomialTerms){
        return "\\mathbf{" + ExpressionsManager.mapToLatexAndReplaceComparator(getPolynomialGeneralForm(polynomialTerms)) + "}";
    }

    public static void showPopUp(){
        dialog.show();
    }

    public static void setUpPopUp(Boolean isFirstStep){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.polynomial_results_pop_up, null);

        MathView firstEquation = ((MathView) view.findViewById(R.id.first_polynomial_id));
        firstEquation.setEngine(MathView.Engine.MATHJAX);
        firstEquation.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        firstEquation.setText("$$" + originalPolynomial + "$$");

        MathView lastEquation = ((MathView) view.findViewById(R.id.last_polynomial_id));
        lastEquation.setEngine(MathView.Engine.MATHJAX);
        lastEquation.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        lastEquation.setText("$$" + getEquationAfterFactorizing() + "$$");

        String rootsCommaSeparated = ExpressionsManager.removeDecimals(android.text.TextUtils.join(" ; ", roots));
        TextView rootsLabel = (TextView) view.findViewById(R.id.roots_label_id);
        if(roots.size() > 1){
            String rootsAsText = "" + context.getText(R.string.RAICES_ENCONTRADAS);
            rootsLabel.setText(rootsAsText.replace("/raices/", rootsCommaSeparated));
        }else if(roots.size() == 1){
            String rootsAsText = "" + context.getText(R.string.RAIZ_ENCONTRADA);
            rootsLabel.setText(rootsAsText.replace("/raiz/", rootsCommaSeparated));
        }else{
            rootsLabel.setVisibility(View.INVISIBLE);
        }

        view.setClipToOutline(true);
        builder.setView(view);
        dialog = builder.create();

        ((Button) view.findViewById(R.id.close_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstStep){
                    v.getContext().startActivity(new Intent(v.getContext(), EnterPolinomialEquationOptionsActivity.class));
                    dialog.cancel();
                }
                dialog.hide();
            }
        });
    }

    public static MultipleChoiceStep nextStep(){
        Map<String, Integer> cases = getNextPossibleCases();
        setFactors();

        return new MultipleChoiceStep(getEquation(), "", "", "", "",
                context.getString(R.string.FACTOR_COMUN), "",
                context.getString(R.string.CUADRATICA), "",
                context.getString(R.string.GAUSS), "",
                cases.get(CORRECT_OPTION), cases.get(REGULAR_OPTION_1), cases.get(REGULAR_OPTION_2), "","",
                "" );
    }

    public static Map<String, Integer> getNextPossibleCases(){
        // Veo qué casos son posibles
        Map<String, Integer> result = new HashMap<>();
        Boolean factorComunIsPossible = commonFactorIsPossible();
        Boolean quadraticIsPossible= quadraticIsPossible();
        Boolean gaussIsPossible= gaussIsPossible();

        // Fomulo opciones correctas, regulares e incorrectas

        Integer correctOption = null;
        Integer regularOption1 = null;
        Integer regularOption2 = null;

        /**
         * factor comun: 1
         * cuadrática: 2
         * gauss: 3
         */

        if(!polynomialTerms.isEmpty()){
            Integer degree = getDegree();
            Boolean hasIndependentTerm = hasIndependentTerm();
            if(!factorComunIsPossible && !quadraticIsPossible){
                if(gaussIsPossible){
                    correctOption = GAUSS;
                }
            }else if (factorComunIsPossible){
                correctOption = FACTOR_COMUN;
                if(quadraticIsPossible){
                    regularOption1 = CUADRATICA;
                    if(hasIndependentTerm){
                        regularOption2 = GAUSS;
                    }
                }else if(gaussIsPossible){ // No se puede cuadrática porque el grado es != 2
                    regularOption1 = GAUSS;
                }
            }else{
                correctOption = CUADRATICA;
                if(gaussIsPossible){
                    regularOption1 = GAUSS;
                }
            }
        }

        result.put(CORRECT_OPTION, correctOption);
        result.put(REGULAR_OPTION_1, regularOption1);
        result.put(REGULAR_OPTION_2, regularOption2);
        return result;
    }

    private static Boolean commonFactorIsPossible(){
        if(polynomialTerms.size() == 1){
            return false;
        }

        Boolean numericCommonFactor = !polynomialTerms.isEmpty() && polynomialTerms.get(getDegree()) != 1;
        Boolean variableCommonFactor = !hasIndependentTerm();

        return numericCommonFactor || variableCommonFactor;
    }

    private static Boolean quadraticIsPossible(){
        Boolean quadraticIsPossible = false;
        Boolean existsTerms = !polynomialTerms.isEmpty();
        if(existsTerms && getDegree() == 2){
            Double a = polynomialTerms.containsKey(2) ? polynomialTerms.get(2) : 0.0;
            Double b = polynomialTerms.containsKey(1) ? polynomialTerms.get(1) : 0.0;
            Double c = polynomialTerms.containsKey(0) ? polynomialTerms.get(0) : 0.0;

            Double discriminant = b * b - 4 * a * c;
            quadraticIsPossible = discriminant >= 0;
        }
        return quadraticIsPossible;
    }

    private static Boolean gaussIsPossible(){
        Boolean existsTerms = !polynomialTerms.isEmpty();
        Boolean existsAtLeastOneRoot = existsTerms && hasIndependentTerm() && !rootsOfPolynomial().isEmpty();
        return existsAtLeastOneRoot && (getDegree() > 2 || (getDegree() == 2 && quadraticIsPossible()));
    }

    public static String getEquation(){
        String rootsFactorizedAux = "";
        if(!rootsFactorized.isEmpty()){
            rootsFactorizedAux = rootsFactorized;
            rootsFactorizedAux = ExpressionsManager.mapToLatexAndReplaceComparator(rootsFactorizedAux);
            rootsFactorizedAux += !pendingPolynomial.isEmpty() ? "*" : "";
        }

        String pendingPolynomialAux = "";
        if(!pendingPolynomial.isEmpty()){
            pendingPolynomialAux = ExpressionsManager.mapToLatexAndReplaceComparator(pendingPolynomial);
            pendingPolynomialAux = "\\mathbf{" + pendingPolynomialAux + "}";
        }

        return addMultiplier(rootsFactorizedAux, pendingPolynomialAux);
    }

    public static String getEquationAfterFactorizing(){
        String rootsFactorizedAux = "";
        if(!rootsFactorized.isEmpty()){
            rootsFactorizedAux = ExpressionsManager.removeDecimals(rootsFactorized);
            System.out.println("Parte factorizada: " + rootsFactorizedAux);
            rootsFactorizedAux = ExpressionsManager.mapToLatexAndReplaceComparator(rootsFactorizedAux);
            rootsFactorizedAux = "\\mathbf{" + rootsFactorizedAux + "}" + (!pendingPolynomial.isEmpty() ? " \\cdot " : "");
        }

        String pendingPolynomialAux = "";
        if(!pendingPolynomial.isEmpty()){
            pendingPolynomialAux = ExpressionsManager.removeDecimals(pendingPolynomial);
            System.out.println("Parte pendiente: " + pendingPolynomialAux);
            pendingPolynomialAux = ExpressionsManager.mapToLatexAndReplaceComparator(pendingPolynomialAux);
        }

        String finalPolynomial = addMultiplier(rootsFactorizedAux, pendingPolynomialAux); 
        System.out.println("Polinomio final: " + finalPolynomial);
        return finalPolynomial;
    }

    private static String addMultiplier(String roots, String pending){
        String equation;
        if(multiplier != 1){
            if(roots.isEmpty()){
                equation = multiplier + " \\cdot (" + pending + ")";
            }else{
                equation = multiplier + " \\cdot " + roots + pending;
            }
        }else{
            equation = roots + pending;
        }
        return ExpressionsManager.removeDecimals(equation);
    }

    public static void setFactors(){
        // Polinomio a factorizar
        pendingPolynomial = getPolynomialGeneralForm(polynomialTerms);
        if(!roots.isEmpty() && !pendingPolynomial.isEmpty()){
            pendingPolynomial = "(" + pendingPolynomial + ")";
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
        return ExpressionsManager.removeDecimals(stringBuilder.toString());
    }

    public static void factorizeBy(Integer option){
        if(!end){
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

        Boolean quadraticIsPossible = quadraticIsPossible();
        Boolean gaussIsPossible = gaussIsPossible();

        if(!commonFactorIsPossible() && !quadraticIsPossible && !gaussIsPossible){
            end = true;
            Integer degree = getDegree();
            if(existsTerms){
                if(hasIndependentTerm() && degree == 1){
                    Double lastRoot = -1 * polynomialTerms.get(0);
                    addRoot(lastRoot);
                    incrementMultiplier(polynomialTerms.get(1));
                    if(currentRoot1 == null){
                        currentRoot1 = lastRoot;
                        currentRootType = getMultiplicityName(1);
                    }else{
                        currentRoot2 = lastRoot;
                    }
                    polynomialTerms.clear();
                }else if(!hasIndependentTerm()){ // Ejemplo: x^3
                    for(int i = 0 ; i < degree ; i++){
                        addRoot(0.0);
                    }
                    incrementMultiplier(polynomialTerms.get(degree));
                    currentRoot1 = 0.0;
                    currentRootType = getMultiplicityName(degree);
                    polynomialTerms.clear();
                }
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
                polynomialTerms.put(exponent, (double) Math.round(((polynomialTerms.get(exponent) / mainCoefficient) * 100)) / 100);
            }
            incrementMultiplier(mainCoefficient);
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
            incrementMultiplier(a);
        }
        end = true;
    }

    private static void applyGauss(){
        if(hasIndependentTerm()){
            if(getDegree() == 2){
                incrementMultiplier(polynomialTerms.get(2));
            }

            List<Double> possibleRoots = rootsOfPolynomial();
            try{
                // Aplico gauss con la primer raíz que aparezca
                applyRuffini(possibleRoots.get(0));
            }catch (Exception e){
                System.out.println(e.getMessage() + " - Raíces: " + possibleRoots.toString());
            }
        }
    }

    private static List<Double> rootsOfPolynomial(){
        Double independentTerm = polynomialTerms.get(0);
        Double principalCoefficient = polynomialTerms.get(getDegree());

        List<Integer> independentTermDivisors = divisorsOf(independentTerm);
        List<Integer> principalCoefficientDivisors = divisorsOf(principalCoefficient);

        List<Double> totalRoots = new ArrayList<>();
        for(Integer independentTermDivisor : independentTermDivisors){
            for(Integer principalCoefficientDivisor : principalCoefficientDivisors){
                Double candidate = (double) independentTermDivisor / principalCoefficientDivisor;
                if(isRoot(candidate)){
                    totalRoots.add(candidate);
                }
            }
        }

        return totalRoots;
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
        polynomialTerms.clear();
        for(int i = 0 ; i < quotient.size() - 1 ; i++){
            polynomialTerms.put(currentDegree--, quotient.get(i));
        }
    }

    private static void incrementMultiplier(Double newMultiplier){
        multiplier *= newMultiplier;
    }

    private static void addRoot(Double root){
        Double roundedRoot = (double) Math.round(root * 100) / 100;
        if(roots.contains(roundedRoot)){
            Integer multiplicity = rootsMultiplicity.get(roundedRoot);
            rootsMultiplicity.remove(roundedRoot);
            rootsMultiplicity.put(roundedRoot, multiplicity + 1);
        }else{
            roots.add(roundedRoot);
            rootsMultiplicity.put(roundedRoot, 1);
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

    public static void enableSummary(Boolean isFirstStep){
        setUpPopUp(isFirstStep);
        context.enableSummary();
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
                if(currentRoot1 != null){
                    answer = "" + context.getText(R.string.FACTOR_COMUN_ES_EL_CORRECTO);
                    answer =  answer
                            .replace("/raiz/", "" + currentRoot1)
                            .replace("/tipo/", currentRootType);
                    if(currentRoot2 == null){
                        return answer;
                    }

                    answer += " " + context.getText(R.string.RAIZ_EXTRA);
                    return answer.replace("/raiz/", "" + currentRoot2);

                }

                return "" + context.getText(R.string.FACTOR_COMUN_NUMERICO_ES_EL_CORRECTO);

            case 2:
                if(currentRoot2 == null){
                    answer = "" + context.getText(R.string.CUADRATICA_RAIZ_DOBLE_ES_EL_CORRECTO);
                    return answer.replace("/raiz/", "" + currentRoot1);
                }
                answer = "" + context.getText(R.string.CUADRATICA_RAICES_SIMPLES_ES_EL_CORRECTO);
                return answer
                        .replace("/raiz1/", "" + currentRoot1)
                        .replace("/raiz2/", "" + currentRoot2);
            case 3:
                answer = "" + context.getText(R.string.GAUSS_ES_EL_CORRECTO);
                return answer.replace("/raiz/", "" + currentRoot1);
            default:
                return "";
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
            case 3:
                return "" + context.getText(R.string.GAUSS_ERA_POSIBLE);
            default:
                return "";
        }
    }

    public static String getMessageOfRegularOptionChosen(Integer regularOption){
        switch (regularOption){
            case 2:
                return "" + context.getText(R.string.CUADRATICA_ES_POSIBLE_PERO_NO_LO_MEJOR);
            case 3:
                String answer = "" + context.getText(R.string.GAUSS_ES_POSIBLE_PERO_NO_LO_MEJOR);
                return answer.replace("/raiz/", "" + currentRoot1);
            default:
                return "";
        }
    }

    public static String getMessageOfRightOptionNotChosen(Integer correctOption){
        switch (correctOption){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN_ERA_EL_CORRECTO);
            case 2:
                return "" + context.getText(R.string.CUADRATICA_ERA_EL_CORRECTO);
            case 3:
                return "" + context.getText(R.string.GAUSS_ERA_EL_CORRECTO);
            default:
                return "";
        }
    }

    public static String getMessageOfWrongOptionChosen(Integer optionChosen){
        switch (optionChosen){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN_NO_ES_CORRECTO);
            case 2:
                return "" + context.getText(R.string.CUADRATICA_NO_ES_CORRECTO);
            case 3:
                return "" + context.getText(R.string.GAUSS_NO_ES_CORRECTO);
            default:
                return "";
        }
    }

    public static String getCaseNameFrom(Integer option){
        switch (option){
            case 1:
                return "" + context.getText(R.string.FACTOR_COMUN);
            case 2:
                return "" + context.getText(R.string.CUADRATICA);
            case 3:
                return "" + context.getText(R.string.GAUSS);
            default:
                return "";
        }
    }
}
