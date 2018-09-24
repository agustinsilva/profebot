package ar.com.profebot.service;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.profebot.activities.R;

import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolveEquationActivity;

public abstract class Manager {
    abstract RecyclerView getRecyclerView();
    abstract void setUpSolveButton(Button button, RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, List<MultipleChoiceStep> multipleChoiceSteps, List<MultipleChoiceStep> currentMultipleChoiceSteps);

    public void setUpSolveButtonGlobal(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                       MultipleChoiceStep currentMultipleChoiceStep, List<MultipleChoiceStep> multipleChoiceSteps,
                                       List<MultipleChoiceStep> currentMultipleChoiceSteps){
        holder.explanationStep.setVisibility(View.VISIBLE);
        holder.isSolved = true;
        currentMultipleChoiceStep.setSolved(true);
        holder.solveStep.setVisibility(View.GONE);
        holder.nextStep.setVisibility(View.VISIBLE);
        holder.explanationStepLayout.setVisibility(View.VISIBLE);

        if(multipleChoiceSteps.size() == currentMultipleChoiceSteps.size()){
            holder.solveAndNextStepLayout.setVisibility(View.GONE);
        }else {
            holder.nextStep.setBackgroundResource(R.drawable.rounded_corners_multiple_choice_buttons);
        }
        getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size() - 1);
    };

    protected String getAsInfix(String equation){
        return equation
                .replace("\\(", "")
                .replace("\\)", "");
    }

    protected void setUpNextStepButton(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                       List<MultipleChoiceStep> multipleChoiceSteps,
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
                currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 1));
                getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size() - 1);
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

    protected void setUpMultipleChoiceExplanationsPopUp(Button button, String correctJustification, String incorrectJustification){
        // TODO: setupear el pop up
    }
}
