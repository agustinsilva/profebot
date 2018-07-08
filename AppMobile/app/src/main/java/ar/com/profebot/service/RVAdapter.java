package ar.com.profebot.service;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolveEquationActivity;
import io.github.kexanie.library.MathView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MultipleChoiceViewHolder> {

    private List<MultipleChoiceStep> multipleChoiceSteps;
    private List<MultipleChoiceStep> currentMultipleChoiceSteps;
    private static LinearLayout currentLayoutExpanded;
    private static ImageView currentExpandCollapseIndicator;
    private static List<MultipleChoiceViewHolder> multipleChoiceViewHolders = new ArrayList<>();

    public static class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        MathView equationBase;
        MathView newEquationBase;
        ImageView expandCollapseIndicator;
        TextView summary;
        LinearLayout multipleChoiceResolutionStep;
        LinearLayout multipleChoiceSolvedResolutionStep;
        Button solveStep;
        LinearLayout layoutToUse;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        RadioButton correctOptionRadio;
        RadioButton incorrectOptionRadio;
        Integer chosenOption;
        Integer correctOption;
        String correctOptionJustification;
        String incorrectOptionJustification1;
        String incorrectOptionJustification2;
        List<MultipleChoiceStep> multipleChoiceSteps;
        List<MultipleChoiceStep> currentMultipleChoiceSteps;
        Integer position;

        private void setUpSolveButton(){
            if(!solveStep.isEnabled()){
                solveStep.setEnabled(true);
                solveStep.setBackgroundResource(R.color.colorGreen);
                solveStep.setTextColor(Color.WHITE);
                solveStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SolveEquationActivity.refreshRV();
                        summary.setVisibility(View.VISIBLE);
                        multipleChoiceResolutionStep.setVisibility(View.GONE);
                        multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
                        layoutToUse = multipleChoiceSolvedResolutionStep;
                        currentLayoutExpanded = layoutToUse;
                        currentExpandCollapseIndicator = expandCollapseIndicator;

                        correctOptionRadio.setText(correctOptionJustification);
                        if(chosenOption.equals(correctOption)){
                            incorrectOptionRadio.setVisibility(View.GONE);
                        }else{
                            Map<Integer, String> incorrectOptions = new HashMap<>();
                            for(int i = 1 ; i <= 3 ; i++){
                                if(i != correctOption){
                                    if(incorrectOptions.isEmpty()){
                                        incorrectOptions.put(i, incorrectOptionJustification1);
                                    }else{
                                        incorrectOptions.put(i, incorrectOptionJustification2);
                                    }
                                }
                            }
                            incorrectOptionRadio.setVisibility(View.VISIBLE);
                            incorrectOptionRadio.setText(incorrectOptions.get(chosenOption));
                        }

                        if(currentMultipleChoiceSteps.size() < multipleChoiceSteps.size()){
                            currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size()));
                        }
                    }
                });
            }
        }

        MultipleChoiceViewHolder(View itemView, List<MultipleChoiceStep> currentMultipleChoiceSteps) {
            super(itemView);
            card = itemView.findViewById(R.id.step_id);
            equationBase = itemView.findViewById(R.id.equation_base_id);
            newEquationBase = itemView.findViewById(R.id.new_equation_base_id);
            expandCollapseIndicator = itemView.findViewById(R.id.expand_collapse_indicator_id);
            summary = itemView.findViewById(R.id.summary_id);

            multipleChoiceResolutionStep = itemView.findViewById(R.id.multiple_choice_section_id);
            multipleChoiceSolvedResolutionStep = itemView.findViewById(R.id.multiple_choice_solved_section_id);
            multipleChoiceResolutionStep.setVisibility(View.GONE);
            multipleChoiceSolvedResolutionStep.setVisibility(View.GONE);

            correctOptionRadio = itemView.findViewById(R.id.option_correct_id);
            incorrectOptionRadio = itemView.findViewById(R.id.option_incorrect_id);

            if(summary.getVisibility() == View.GONE){
                if(currentMultipleChoiceSteps.size() == 1){
                    expandCollapseIndicator.setScaleY(-1f);
                    multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
                }else{
                    expandCollapseIndicator.setScaleY(1f);
                    multipleChoiceResolutionStep.setVisibility(View.GONE);
                }
                layoutToUse = multipleChoiceResolutionStep;

                solveStep = itemView.findViewById(R.id.solve_step_id);
                solveStep.setEnabled(false);

                MultipleChoiceViewHolder viewHolder = this;

                optionA = itemView.findViewById(R.id.option_a_id);
                optionA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 1;
                    }
                });

                optionB = itemView.findViewById(R.id.option_b_id);
                optionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 2;
                    }
                });

                optionC = itemView.findViewById(R.id.option_c_id);
                optionC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 3;
                    }
                });
            }else{
                layoutToUse = multipleChoiceSolvedResolutionStep;
            }

            expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shouldExpand = layoutToUse.getVisibility() == View.GONE;
                    SolveEquationActivity.refreshRV();
                    if(shouldExpand){
                        if(currentLayoutExpanded != null){
                            currentLayoutExpanded.setVisibility(View.GONE);
                            currentExpandCollapseIndicator.setScaleY(1f);
                        }
                        expandCollapseIndicator.setScaleY(-1f);
                        layoutToUse.setVisibility(View.VISIBLE);
                        currentLayoutExpanded = layoutToUse;
                        currentExpandCollapseIndicator = expandCollapseIndicator;
                    }else{
                        currentLayoutExpanded = null;
                        currentExpandCollapseIndicator = null;
                        layoutToUse.setVisibility(View.GONE);
                        expandCollapseIndicator.setScaleY(1f);
                    }
                }
            });
        }
    }

    public RVAdapter(MultipleChoiceStep firstStep, List<MultipleChoiceStep> multipleChoiceSteps){
        this.multipleChoiceSteps = multipleChoiceSteps;
        List<MultipleChoiceStep> steps = new ArrayList<>();
        steps.add(firstStep);
        this.currentMultipleChoiceSteps = steps;
    }

    @Override
    public int getItemCount() {
        return currentMultipleChoiceSteps.size();
    }

    @Override
    public MultipleChoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resolution_step, viewGroup, false);
        MultipleChoiceViewHolder multipleChoiceViewHolder = new MultipleChoiceViewHolder(v, this.currentMultipleChoiceSteps);
        multipleChoiceViewHolders.add(multipleChoiceViewHolder);
        return multipleChoiceViewHolder;
    }

    @Override
    public void onBindViewHolder(MultipleChoiceViewHolder multipleChoiceViewHolder, int i) {
        multipleChoiceViewHolder.equationBase.setText("\\(" + currentMultipleChoiceSteps.get(i).getEquationBase() + "\\)");
        multipleChoiceViewHolder.newEquationBase.setText("\\(" + currentMultipleChoiceSteps.get(i).getEquationBase() + "\\)");
        multipleChoiceViewHolder.summary.setText(currentMultipleChoiceSteps.get(i).getSummary());
        multipleChoiceViewHolder.optionA.setText(currentMultipleChoiceSteps.get(i).getOptionA());
        multipleChoiceViewHolder.optionB.setText(currentMultipleChoiceSteps.get(i).getOptionB());
        multipleChoiceViewHolder.optionC.setText(currentMultipleChoiceSteps.get(i).getOptionC());
        multipleChoiceViewHolder.correctOption = currentMultipleChoiceSteps.get(i).getCorrectOption();
        multipleChoiceViewHolder.correctOptionJustification = currentMultipleChoiceSteps.get(i).getCorrectOptionJustification();
        multipleChoiceViewHolder.incorrectOptionJustification1 = currentMultipleChoiceSteps.get(i).getIncorrectOptionJustification1();
        multipleChoiceViewHolder.incorrectOptionJustification2 = currentMultipleChoiceSteps.get(i).getIncorrectOptionJustification2();
        multipleChoiceViewHolder.currentMultipleChoiceSteps = currentMultipleChoiceSteps;
        multipleChoiceViewHolder.multipleChoiceSteps = multipleChoiceSteps;
        multipleChoiceViewHolder.position = i;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
