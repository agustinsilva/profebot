package ar.com.profebot.service;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.EnterEquationOptionsActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import io.github.kexanie.library.MathView;

public class EquationManager extends Manager{

    private static SolveEquationActivity context;
    private static TextView rootsSummary;
    private static AlertDialog dialog;

    public static void showPopUp(){
        dialog.show();
    }

    public static void setUpPopUp(Boolean isFirstStep){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.equation_results_pop_up, null);

        rootsSummary = (TextView) view.findViewById(R.id.roots_summary_id);

        view.setClipToOutline(true);
        builder.setView(view);
        dialog = builder.create();

        ((Button) view.findViewById(R.id.close_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstStep){
                    v.getContext().startActivity(new Intent(v.getContext(), EnterEquationOptionsActivity.class));
                    dialog.cancel();
                }
                dialog.hide();
            }
        });
    }

    public static void enableSummary(){
        EquationManager.setUpPopUp(false);
        context.enableSummary();
    }

    public static void setContext(SolveEquationActivity context) {
        EquationManager.context = context;
    }

    @Override
    public void setUpSolveButton(Button button, RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, List<MultipleChoiceStep> multipleChoiceSteps, List<MultipleChoiceStep> currentMultipleChoiceSteps) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.isSolved = true;
                holder.multipleChoiceResolutionStep.setVisibility(View.GONE);
                holder.multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
                holder.layoutToUse = holder.multipleChoiceSolvedResolutionStep;

                holder.correctOptionRadio.setText(holder.correctOptionJustification);
                if(holder.chosenOption.equals(holder.correctOption)){
                    holder.incorrectOptionRadio.setVisibility(View.GONE);
                    holder.expandCollapseIndicatorColor.setBackgroundResource(R.drawable.solved_right);
                }else{
                    holder.expandCollapseIndicatorColor.setBackgroundResource(R.drawable.solved_wrong);
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
                    holder.incorrectOptionRadio.setVisibility(View.VISIBLE);
                    holder.incorrectOptionRadio.setText(incorrectOptions.get(holder.chosenOption));

                    ExpressionsManager.requestNewExercises(getAsInfix(holder.equationBase.getText()), getAsInfix(holder.newEquationBase.getText()), holder.equationBase.getContext());
                }
                holder.expandCollapseIndicatorColor.setVisibility(View.VISIBLE);

                MultipleChoiceStep currentMultipleChoiceStep = holder.multipleChoiceSteps.get(currentMultipleChoiceSteps.size()-1);
                currentMultipleChoiceStep.setSolved(true);
                holder.summary.setText(currentMultipleChoiceStep.getSummary());
                if(currentMultipleChoiceSteps.size() < holder.multipleChoiceSteps.size()){
                    setUpNextStepButton(holder, multipleChoiceSteps, currentMultipleChoiceSteps);
                }else{
                    holder.nextStep.setVisibility(View.GONE);
                    enableSummary();
                }

                currentMultipleChoiceStep.setSolved(true);
                SolveEquationActivity.recyclerView.scrollToPosition(currentMultipleChoiceSteps.size() - 1);
            }
        });
    }

    @Override
    public RecyclerView getRecyclerView() {
        return SolveEquationActivity.recyclerView;
    }
}
