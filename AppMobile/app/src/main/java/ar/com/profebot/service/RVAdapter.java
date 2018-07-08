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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import io.github.kexanie.library.MathView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MultipleChoiceViewHolder> {

    private List<MultipleChoiceStep> multipleChoiceSteps;

    public List<MultipleChoiceStep> getMultipleChoiceSteps() {
        return multipleChoiceSteps;
    }

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

        private void setUpSolveButton(){
            if(!solveStep.isEnabled()){
                solveStep.setEnabled(true);
                solveStep.setBackgroundResource(R.color.colorGreen);
                solveStep.setTextColor(Color.WHITE);
                solveStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        summary.setVisibility(View.VISIBLE);
                        multipleChoiceResolutionStep.setVisibility(View.GONE);
                        multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
                        layoutToUse = multipleChoiceSolvedResolutionStep;

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
                    }
                });
            }
        }

        MultipleChoiceViewHolder(View itemView) {
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
                expandCollapseIndicator.setScaleY(-1f);
                multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
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

                    if(shouldExpand){
                        expandCollapseIndicator.setScaleY(-1f);
                        layoutToUse.setVisibility(View.VISIBLE);
                    }else{
                        layoutToUse.setVisibility(View.GONE);
                        expandCollapseIndicator.setScaleY(1f);
                    }
                }
            });
        }
    }

    public RVAdapter(List<MultipleChoiceStep> multipleChoiceSteps){
        this.multipleChoiceSteps = multipleChoiceSteps;
    }

    @Override
    public int getItemCount() {
        return multipleChoiceSteps.size();
    }

    @Override
    public MultipleChoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resolution_step, viewGroup, false);
        return new MultipleChoiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MultipleChoiceViewHolder multipleChoiceViewHolder, int i) {
        multipleChoiceViewHolder.equationBase.setText("\\(" + multipleChoiceSteps.get(i).getEquationBase() + "\\)");
        multipleChoiceViewHolder.newEquationBase.setText("\\(" + multipleChoiceSteps.get(i).getEquationBase() + "\\)");
        multipleChoiceViewHolder.summary.setText(multipleChoiceSteps.get(i).getSummary());
        multipleChoiceViewHolder.optionA.setText(multipleChoiceSteps.get(i).getOptionA());
        multipleChoiceViewHolder.optionB.setText(multipleChoiceSteps.get(i).getOptionB());
        multipleChoiceViewHolder.optionC.setText(multipleChoiceSteps.get(i).getOptionC());
        multipleChoiceViewHolder.correctOption = multipleChoiceSteps.get(i).getCorrectOption();
        multipleChoiceViewHolder.correctOptionJustification = multipleChoiceSteps.get(i).getCorrectOptionJustification();
        multipleChoiceViewHolder.incorrectOptionJustification1 = multipleChoiceSteps.get(i).getIncorrectOptionJustification1();
        multipleChoiceViewHolder.incorrectOptionJustification2 = multipleChoiceSteps.get(i).getIncorrectOptionJustification2();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
