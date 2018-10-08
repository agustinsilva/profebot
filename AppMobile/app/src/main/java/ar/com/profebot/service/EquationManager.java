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
}
