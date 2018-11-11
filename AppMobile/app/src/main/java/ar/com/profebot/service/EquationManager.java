package ar.com.profebot.service;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.EnterEquationOptionsActivity;
import ar.com.profebot.activities.PendingExercisesActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import io.github.kexanie.library.MathView;

public class EquationManager extends Manager{

    private static SolveEquationActivity context;

    protected Context getContext(){
        return context;
    }

    protected LayoutInflater getLayoutInflater(){
        return context.getLayoutInflater();
    }

    public static void enableSummary(){
        context.enableSummary();
    }

    public static void setContext(SolveEquationActivity context) {
        EquationManager.context = context;
    }

    public void setUpSolveButton(Button button, RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                 List<MultipleChoiceStep> multipleChoiceSteps,
                                 List<MultipleChoiceStep> currentMultipleChoiceSteps,
                                 List<RVMultipleChoiceAdapter.MultipleChoiceViewHolder> multipleChoiceViewHolders) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.chosenOption.equals(holder.correctOption)){
                    showIconForOption(holder, holder.chosenOption, R.drawable.solved_right);
                    setUpMultipleChoiceExplanationsPopUp(holder, holder.correctOptionJustification, null);
                }else{
                    showIconForOption(holder, holder.correctOption, R.drawable.solved_right);
                    showIconForOption(holder, holder.chosenOption, R.drawable.solved_wrong);

                    Map<Integer, String> incorrectOptions = new HashMap<>();
                    for(int i = 1 ; i <= 3 ; i++){
                        if(i != holder.correctOption){
                            if(incorrectOptions.isEmpty()){
                                incorrectOptions.put(i, holder.incorrectOptionJustification1);
                            }else{
                                incorrectOptions.put(i, holder.incorrectOptionJustification2);
                            }
                        }
                    }
                    setUpMultipleChoiceExplanationsPopUp(holder, holder.correctOptionJustification, incorrectOptions.get(holder.chosenOption));

                    ExpressionsManager.requestNewExercises(getAsInfix(holder.equationBase), getAsInfix(holder.newEquationBase), holder.equationBaseAsLatex.getContext());
                }

                MultipleChoiceStep currentMultipleChoiceStep = holder.multipleChoiceSteps.get(currentMultipleChoiceSteps.size()-1);
                currentMultipleChoiceStep.setSolved(true);
                holder.summary.setText(currentMultipleChoiceStep.getSummary());
                if(currentMultipleChoiceSteps.size() < holder.multipleChoiceSteps.size()){
                    setUpNextStepButton(holder, currentMultipleChoiceSteps);
                }else{
                    holder.nextStep.setVisibility(View.GONE);
                    enableSummary();
                }

                setUpSolveButtonGlobal(holder, currentMultipleChoiceStep, multipleChoiceSteps, currentMultipleChoiceSteps);
            }
        });
    }

    @Override
    public RecyclerView getRecyclerView() {
        return SolveEquationActivity.recyclerView;
    }

    public static Map<String, String> fixResolutionTextsForRoots(Map<String, String> contextOfResolutionTexts, String lastEquation){
        String[] members = lastEquation.split("=");
        try{
            Double solution = Double.parseDouble(members[1]);
            contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/raices/", solution + "");
        }catch (Exception e){
            String roots = members[1]
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", ", ");
            contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/raices/", roots);
        }
        return contextOfResolutionTexts;
    }

    public static Map<String, String> fixResolutionTextsForFunctionIntervals(Map<String, String> contextOfResolutionTexts, String lastEquation){
        if(lastEquation.contains("!=")){
            String[] members = lastEquation.split("!=");
            contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/intervalo/", "R - {" + members[1] + "}");
        }else if(lastEquation.contains(">=")){
            // TODO
        }else if(lastEquation.contains("<=")){
            // TODO
        }else if(lastEquation.contains(">")){
            // TODO
        }else if(lastEquation.contains("<")){
            // TODO
        }

        return contextOfResolutionTexts;
    }

    public static String getIntervalFrom(String equation){
        String comparatorOperator = ExpressionsManager.getRootOfEquation(equation);
        String[] members = equation.split(comparatorOperator);
        Double ordenadaAlOrigen = Double.parseDouble(members[1]) * -1;
        Boolean ordenadaAlOrigenEsPositiva = ordenadaAlOrigen > 0;
        // Si el miembro izquierdo contiene X (pero no es X) --> no se pudo resolver la cuadrática --> no hay raíces
        if(members[0].toUpperCase().contains("X") && !members[0].toUpperCase().equals("X")){
            if(ordenadaAlOrigenEsPositiva){
                if(">".equals(comparatorOperator) || ">=".equals(comparatorOperator)){
                    return "(-∞, ∞)";
                }else{
                    return "";
                }
            }

            // Ordenada al origen es negativa
            if(">".equals(comparatorOperator) || ">=".equals(comparatorOperator)){
                return "";
            }
            return "(-∞, ∞)";
        }

        String result = "";
        switch (comparatorOperator){
            case ">":
                result = "(AUX, +∞)";
                break;
            case ">=":
                result = "[AUX, +∞)";
                break;
            case "<":
                result = "(-∞, AUX)";
                break;
            case "<=":
                result = "(-∞, AUX]";
                break;
        }

        return ExpressionsManager.removeDecimals(result.replace("AUX", "" + (-1 * ordenadaAlOrigen)));
    }
}
