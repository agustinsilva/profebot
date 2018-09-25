package ar.com.profebot.service;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.profebot.activities.R;

import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;

public abstract class Manager {
    abstract RecyclerView getRecyclerView();
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

        // Oculto botón de resolver, y muestro el del próximo paso + el de ver la explicación
        holder.solveStep.setVisibility(View.GONE);
        holder.nextStep.setVisibility(View.VISIBLE);
        holder.explanationStepLayout.setVisibility(View.VISIBLE);

        // Si es el último paso, solo muestro el botón de explicación
        if(multipleChoiceSteps.size() == currentMultipleChoiceSteps.size()){
            holder.solveAndNextStepLayout.setVisibility(View.GONE);
        }else {
            holder.nextStep.setBackgroundResource(R.drawable.rounded_corners_multiple_choice_buttons);
            currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 1));
        }
        getRecyclerView().scrollToPosition(currentMultipleChoiceSteps.size() - 1);
    }

    protected String getAsInfix(String equation){
        return equation
                .replace("\\(", "")
                .replace("\\)", "");
    }

    protected void setUpNextStepButton(RVMultipleChoiceAdapter.MultipleChoiceViewHolder holder,
                                       List<MultipleChoiceStep> currentMultipleChoiceSteps,
                                       List<RVMultipleChoiceAdapter.MultipleChoiceViewHolder> multipleChoiceViewHolders){
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
                currentMultipleChoiceSteps.get(currentMultipleChoiceSteps.size() - 2).setNextStepButtonWasPressed(true);
                for(RVMultipleChoiceAdapter.MultipleChoiceViewHolder viewHolder : multipleChoiceViewHolders){
                    viewHolder.card.setVisibility(View.VISIBLE);
                }
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

    protected void setUpMultipleChoiceExplanationsPopUp(Button button, String correctJustification, String incorrectJustification){
        // TODO: setupear el pop up
    }
}
