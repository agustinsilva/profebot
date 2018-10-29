package ar.com.profebot.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.EnterEquationOptionsActivity;
import ar.com.profebot.activities.EnterPolinomialEquationOptionsActivity;
import ar.com.profebot.activities.PendingExercisesActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import io.github.kexanie.library.MathView;

public abstract class Manager {
    abstract RecyclerView getRecyclerView();
    abstract Context getContext();
    abstract LayoutInflater getLayoutInflater();
    abstract void setUpSolveButton(Button button, RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                   List<MultipleChoiceStep> multipleChoiceSteps,
                                   List<MultipleChoiceStep> currentMultipleChoiceSteps,
                                   List<RVMultipleChoiceAdapter.MultipleChoiceViewHolder> multipleChoiceViewHolders);

    public void setUpSolveButtonGlobal(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                       MultipleChoiceStep currentMultipleChoiceStep, List<MultipleChoiceStep> multipleChoiceSteps,
                                       List<MultipleChoiceStep> currentMultipleChoiceSteps){
        holder.explanationStep.setVisibility(View.VISIBLE);

        // Marco como resuelto el paso
        holder.isSolved = true;
        currentMultipleChoiceStep.setSolved(true);
        currentMultipleChoiceStep.setChosenOption(holder.chosenOption);

        // Oculto botón de resolver, y muestro el del próximo paso + el de ver la explicación
        holder.solveStep.setVisibility(View.GONE);
        holder.nextStep.setVisibility(View.VISIBLE);
        holder.explanationStepLayout.setVisibility(View.VISIBLE);

        // Si es el último paso, solo muestro el botón de explicación
        if(multipleChoiceSteps.size() == currentMultipleChoiceSteps.size()){
            holder.solveAndNextStepLayout.setVisibility(View.GONE);
            currentMultipleChoiceStep.setNextStepButtonWasPressed(true);
        }else {
            holder.nextStep.setBackgroundResource(R.drawable.rounded_corners_multiple_choice_buttons);
            currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size()));
        }
        getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size() - 1);
        disableMultipleChoiceOptions(holder);
    }

    private void disableMultipleChoiceOptions(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder){
        holder.optionA.setEnabled(false);
        holder.optionB.setEnabled(false);
        holder.optionC.setEnabled(false);
    }

    protected String getAsInfix(String equation){
        return equation
                .replace("\\(", "")
                .replace("\\)", "");
    }

    protected void setUpNextStepButton(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                       List<MultipleChoiceStep> currentMultipleChoiceSteps){
        holder.nextStep.setEnabled(true);
        holder.nextStep.setBackgroundResource(R.color.colorGreen);
        holder.nextStep.setTextColor(Color.WHITE);
        holder.nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.solveAndNextStepLayout.setVisibility(View.GONE);
                holder.multipleChoiceResolutionStep.setVisibility(View.GONE);
                holder.expandCollapseIndicator.setScaleY(1f);

                // Cuando se clickeó en "solve step", ya se agregó el próximo paso a la lista, por eso, cuando clickeo el next step, no lo hice sobre el último paso de la lista, sino el ante último
                currentMultipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 2).setExpanded(false);
                currentMultipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 2).setNextStepButtonWasPressed(true);

                currentMultipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 1).setExpanded(true);
                RVMultipleChoiceAdapter.MultipleChoiceViewHolder newHolder = currentMultipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 1).getMultipleChoiceViewHolder();

                newHolder.equationBaseAsLatex.config("MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
                newHolder.equationOptionA.setText("\\(" + newHolder.multipleChoiceStep.getEquationBase() + "\\)");
                expandCard(newHolder);
                newHolder.card.setVisibility(View.VISIBLE);

                getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size());
            }
        });
    }

    protected void showIconForOption(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, Integer option, Integer iconId){
        switch (option){
            case 1:
                holder.expandCollapseIndicatorAColor.setBackgroundResource(iconId);
                holder.expandCollapseIndicatorAColor.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.expandCollapseIndicatorBColor.setBackgroundResource(iconId);
                holder.expandCollapseIndicatorBColor.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.expandCollapseIndicatorCColor.setBackgroundResource(iconId);
                holder.expandCollapseIndicatorCColor.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void clearResolutionIcons(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder){
        holder.expandCollapseIndicatorAColor.setVisibility(View.GONE);
        holder.expandCollapseIndicatorBColor.setVisibility(View.GONE);
        holder.expandCollapseIndicatorCColor.setVisibility(View.GONE);
    }

    protected void setUpMultipleChoiceExplanationsPopUp(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, String correctJustification, String incorrectJustification){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.equation_step_summary, null);

        TextView rightExplanation = (TextView)view.findViewById(R.id.right_explanation_id);
        rightExplanation.setText(ExpressionsManager.removeDecimals(correctJustification));
        rightExplanation.setMovementMethod(ScrollingMovementMethod.getInstance());

        if(incorrectJustification == null){
            ((LinearLayout)view.findViewById(R.id.wrong_explanation_section_id)).setVisibility(View.GONE);
        }else{
            ((LinearLayout)view.findViewById(R.id.wrong_explanation_section_id)).setVisibility(View.VISIBLE);
            TextView wrongExplanation = (TextView)view.findViewById(R.id.wrong_explanation_id);
            wrongExplanation.setText(ExpressionsManager.removeDecimals(incorrectJustification));
            wrongExplanation.setMovementMethod(ScrollingMovementMethod.getInstance());
        }

        view.setClipToOutline(true);
        builder.setView(view);

        Button closeDialog = (Button) view.findViewById(R.id.back_to_multiplechoice_id);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.multipleChoiceStep.getDialog().hide();
            }
        });

        holder.multipleChoiceStep.setDialog(builder.create());
        holder.multipleChoiceStep.setCloseDialog(closeDialog);
    }

    public void markOptionChosen(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder, Integer chosenOption){
        switch (chosenOption){
            case 1:
                multipleChoiceViewHolder.optionA.setChecked(true);
                multipleChoiceViewHolder.optionB.setChecked(false);
                multipleChoiceViewHolder.optionC.setChecked(false);
                break;
            case 2:
                multipleChoiceViewHolder.optionA.setChecked(false);
                multipleChoiceViewHolder.optionB.setChecked(true);
                multipleChoiceViewHolder.optionC.setChecked(false);
                break;
            case 3:
                multipleChoiceViewHolder.optionA.setChecked(false);
                multipleChoiceViewHolder.optionB.setChecked(false);
                multipleChoiceViewHolder.optionC.setChecked(true);
                break;
        }
    }

    public void expandCard(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder){
        modifyCardStatus(multipleChoiceViewHolder, -1f, View.VISIBLE, true);
        showEquationOptions(multipleChoiceViewHolder);
    }

    public void collapseCard(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder){
        modifyCardStatus(multipleChoiceViewHolder, 1f, View.GONE, false);
        hideEquationOptions(multipleChoiceViewHolder);
    }

    private void showEquationOptions(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder){
        String holderEquation = multipleChoiceViewHolder.equationOptionA.getText();
        String stepEquation = "$$" + ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceViewHolder.multipleChoiceStep.getEquationOptionA()) + "$$";

        if(!stepEquation.equals(holderEquation)){
            String equationAsLatexOption;
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.optionA.setText(multipleChoiceViewHolder.multipleChoiceStep.getOptionA());
            equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceViewHolder.multipleChoiceStep.getEquationOptionA());
            if(!equationAsLatexOption.isEmpty()){
                multipleChoiceViewHolder.equationOptionA.config("MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
                multipleChoiceViewHolder.equationOptionA.setText("$$" + equationAsLatexOption + "$$");
            }else{
                multipleChoiceViewHolder.equationOptionA.setVisibility(View.GONE);
            }

            multipleChoiceViewHolder.optionB.setText(multipleChoiceViewHolder.multipleChoiceStep.getOptionB());
            equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceViewHolder.multipleChoiceStep.getEquationOptionB());
            if(!equationAsLatexOption.isEmpty()){
                multipleChoiceViewHolder.equationOptionB.config("MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
                multipleChoiceViewHolder.equationOptionB.setText("$$" + equationAsLatexOption + "$$");
            }else{
                multipleChoiceViewHolder.equationOptionB.setVisibility(View.GONE);
            }

            multipleChoiceViewHolder.optionC.setText(multipleChoiceViewHolder.multipleChoiceStep.getOptionC());
            equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceViewHolder.multipleChoiceStep.getEquationOptionC());
            if(!equationAsLatexOption.isEmpty()){
                multipleChoiceViewHolder.equationOptionC.config("MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
                multipleChoiceViewHolder.equationOptionC.setText("$$" + equationAsLatexOption + "$$");
            }else{
                multipleChoiceViewHolder.equationOptionC.setVisibility(View.GONE);
            }
        }
    }

    private void hideEquationOptions(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder){
        multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.GONE);
    }

    private void modifyCardStatus(RVMultipleChoiceAdapter.MultipleChoiceViewHolder multipleChoiceViewHolder,
                                  float scale, int visibility, boolean isExpanded){
        multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(scale);
        multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(visibility);
        multipleChoiceViewHolder.multipleChoiceStep.setExpanded(isExpanded);
    }

    public void showFinalSummary(LinearLayout recycleViewSection, LinearLayout finalSummarySection,
                                 String contextOfResolutionFirstText, String firstEquation,
                                 String contextOfResolutionSecondText, String lastEquation,
                                 String title, String solutionType){
        recycleViewSection.setVisibility(View.INVISIBLE);
        finalSummarySection.setVisibility(View.VISIBLE);

        ((TextView)finalSummarySection.findViewById(R.id.title_id)).setText(title);
        ((TextView)finalSummarySection.findViewById(R.id.first_text_id)).setText(contextOfResolutionFirstText);
        ((TextView)finalSummarySection.findViewById(R.id.second_text_id)).setText(contextOfResolutionSecondText);

        MathView originalEquation = (MathView)finalSummarySection.findViewById(R.id.first_equation_id);
        originalEquation.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        originalEquation.setText("$$" + ExpressionsManager.mapToLatexAndReplaceComparator(firstEquation) + "$$");

        MathView secondEquation = (MathView)finalSummarySection.findViewById(R.id.second_equation_id);
        if(SolvePolynomialActivity.CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED.equals(solutionType)){
            secondEquation.setVisibility(View.VISIBLE);
            secondEquation.config("MathJax.Hub.Config({\n"+
                    "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                    "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                    "         SVG: { linebreaks: { automatic: true } }\n"+
                    "});");
            secondEquation.setText("$$" + lastEquation + "$$");
        }else{
            secondEquation.setVisibility(View.GONE);
        }

        Button goToOptions = (Button)finalSummarySection.findViewById(R.id.return_to_options_id);
        if(SolvePolynomialActivity.CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED.equals(solutionType)){
            goToOptions.setText("Practicar otro polinomio");
        }else{
            goToOptions.setText("Practicar otra ecuación");
        }
        goToOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SolvePolynomialActivity.CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED.equals(solutionType)){
                    getContext().startActivity(new Intent(v.getContext(), EnterPolinomialEquationOptionsActivity.class));
                }else{
                    getContext().startActivity(new Intent(v.getContext(), EnterEquationOptionsActivity.class));
                }
            }
        });

        setUpGoToMultipleChoiceButton(recycleViewSection, finalSummarySection);
        showExtraButtonToGoToPendingExercisesIfCorresponds(finalSummarySection);
    }

    public void showExtraButtonToGoToPendingExercisesIfCorresponds(LinearLayout equationSummarySection){
        Button goToPendingExercises = (Button)equationSummarySection.findViewById(R.id.see_pending_exercises_id);
        if(!hasPendingExercises(getContext())){
            goToPendingExercises.setVisibility(View.GONE);
        }else{
            goToPendingExercises.setVisibility(View.VISIBLE);
            goToPendingExercises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(v.getContext(), PendingExercisesActivity.class));
                }
            });
        }
    }

    public static Boolean hasPendingExercises(Context context){
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(context).getString("pendingExercises","");
        return !"".equals(pendingExercisesJson) && !pendingExercisesJson.contains("[]");
    }

    public void setUpGoToMultipleChoiceButton(LinearLayout recycleViewSection, LinearLayout finalSummarySection){
        Button goToMultipleChoice = (Button)finalSummarySection.findViewById(R.id.return_to_resolution_id);
        goToMultipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewSection.setVisibility(View.VISIBLE);
                finalSummarySection.setVisibility(View.INVISIBLE);

            }
        });
    }

    public void scrollRecycleToTheBottom(){
        getRecyclerView().scrollBy(0, 1000000);
    }

    public void disableSummary(Button seeSummary){
        seeSummary.setBackgroundResource(R.drawable.rounded_corners_disable_button);
        seeSummary.setTextColor(Color.GRAY);
        seeSummary.setEnabled(false);
    }

    public void enableSummary(Button seeSummary){
        seeSummary.setBackgroundResource(R.drawable.rounded_corners_polynomial_summary);
        seeSummary.setTextColor(Color.WHITE);
        seeSummary.setEnabled(true);
        seeSummary.setVisibility(View.VISIBLE);
    }
}
