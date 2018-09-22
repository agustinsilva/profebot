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
    abstract void setUpSolveButton(Button button, RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, List<MultipleChoiceStep> multipleChoiceSteps, List<MultipleChoiceStep> currentMultipleChoiceSteps);
    abstract RecyclerView getRecyclerView();

    protected String getAsInfix(String equation){
        return equation
                .replace("\\(", "")
                .replace("\\)", "");
    }

    protected void setUpNextStepButton(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder, List<MultipleChoiceStep> multipleChoiceSteps, List<MultipleChoiceStep> currentMultipleChoiceSteps){
        holder.nextStep.setEnabled(true);
        holder.nextStep.setBackgroundResource(R.color.colorGreen);
        holder.nextStep.setTextColor(Color.WHITE);
        holder.nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nextStep.setVisibility(View.GONE);
                holder.layoutToUse.setVisibility(View.GONE);
                holder.expandCollapseIndicator.setScaleY(1f);
                currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 1));
                getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size() - 1);
            }
        });
    }
}
