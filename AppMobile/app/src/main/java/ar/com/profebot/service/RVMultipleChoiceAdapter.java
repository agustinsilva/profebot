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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import io.github.kexanie.library.MathView;

public class RVMultipleChoiceAdapter extends RecyclerView.Adapter<RVMultipleChoiceAdapter.MultipleChoiceViewHolder> {

    private List<MultipleChoiceStep> multipleChoiceSteps;
    private static List<MultipleChoiceStep> currentMultipleChoiceSteps;
    private static List<MultipleChoiceViewHolder> multipleChoiceViewHolders = new ArrayList<>();
    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public static class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
        MultipleChoiceStep multipleChoiceStep;
        CardView card;
        MathView equationBaseAsLatex;
        String equationBase;
        String newEquationBase;
        ImageView expandCollapseIndicator;
        ImageView expandCollapseIndicatorAColor;
        ImageView expandCollapseIndicatorBColor;
        ImageView expandCollapseIndicatorCColor;
        TextView summary;
        LinearLayout multipleChoiceResolutionStep;
        LinearLayout explanationStepLayout;
        RelativeLayout solveAndNextStepLayout;
        Button solveStep;
        Button nextStep;
        Button explanationStep;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        MathView equationOptionA;
        MathView equationOptionB;
        MathView equationOptionC;
        Integer chosenOption;
        Integer correctOption;
        Integer regularOption1;
        Integer regularOption2;
        String correctOptionJustification;
        String incorrectOptionJustification1;
        String incorrectOptionJustification2;
        List<MultipleChoiceStep> multipleChoiceSteps;
        TextView numberStep;
        public Boolean isSolved = false;

        private void setUpSolveButton(){
            if(!solveStep.isEnabled()){
                solveStep.setEnabled(true);
                solveStep.setBackgroundResource(R.drawable.rounded_corners_multiple_choice_buttons);
                solveStep.setTextColor(Color.WHITE);
            }
        }

        MultipleChoiceViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.step_id);
            numberStep = itemView.findViewById(R.id.number_step_id);
            equationBaseAsLatex = itemView.findViewById(R.id.equation_base_id);
            expandCollapseIndicator = itemView.findViewById(R.id.expand_collapse_indicator_id);
            expandCollapseIndicatorAColor = itemView.findViewById(R.id.expand_collapse_indicator_color_a_id);
            expandCollapseIndicatorBColor = itemView.findViewById(R.id.expand_collapse_indicator_color_b_id);
            expandCollapseIndicatorCColor = itemView.findViewById(R.id.expand_collapse_indicator_color_c_id);
            explanationStep = itemView.findViewById(R.id.explanation_step_id);
            explanationStepLayout = itemView.findViewById(R.id.explanation_step_layout_id);
            summary = itemView.findViewById(R.id.summary_id);
            solveAndNextStepLayout = itemView.findViewById(R.id.solve_and_next_step_layout_id);
            multipleChoiceResolutionStep = itemView.findViewById(R.id.multiple_choice_section_id);
            solveStep = itemView.findViewById(R.id.solve_step_id);
            nextStep = itemView.findViewById(R.id.next_step_id);
            optionA = itemView.findViewById(R.id.option_a_id);
            equationOptionA = itemView.findViewById(R.id.equation_option_a_id);
            optionB = itemView.findViewById(R.id.option_b_id);
            equationOptionB = itemView.findViewById(R.id.equation_option_b_id);
            optionC = itemView.findViewById(R.id.option_c_id);
            equationOptionC = itemView.findViewById(R.id.equation_option_c_id);
        }
    }

    public RVMultipleChoiceAdapter(MultipleChoiceStep firstStep, List<MultipleChoiceStep> multipleChoiceSteps, Manager manager){
        this.manager = manager;
        this.multipleChoiceSteps = multipleChoiceSteps;
        List<MultipleChoiceStep> steps = new ArrayList<>();
        if(firstStep != null){
            steps.add(firstStep);
        }
        currentMultipleChoiceSteps = steps;
        multipleChoiceViewHolders = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return currentMultipleChoiceSteps.size();
    }

    @Override
    public MultipleChoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resolution_step, viewGroup, false);
        MultipleChoiceViewHolder multipleChoiceViewHolder = new MultipleChoiceViewHolder(v);
        multipleChoiceViewHolders.add(multipleChoiceViewHolder);
        return multipleChoiceViewHolder;
    }

    @Override
    public void onBindViewHolder(MultipleChoiceViewHolder multipleChoiceViewHolder, int position) {
        multipleChoiceViewHolder.multipleChoiceStep = multipleChoiceSteps.get(position);
        multipleChoiceViewHolder.multipleChoiceStep.setMultipleChoiceViewHolder(multipleChoiceViewHolder);
        multipleChoiceViewHolder.equationBase = multipleChoiceViewHolder.multipleChoiceStep.getEquationBase();
        multipleChoiceViewHolder.newEquationBase = multipleChoiceViewHolder.multipleChoiceStep.getNewEquationBase();
        multipleChoiceViewHolder.equationBaseAsLatex.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.equationBaseAsLatex.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        multipleChoiceViewHolder.equationBaseAsLatex.setText("\\(" + ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceViewHolder.multipleChoiceStep.getEquationBase()) + "\\)");

        multipleChoiceViewHolder.correctOption = multipleChoiceViewHolder.multipleChoiceStep.getCorrectOption();
        multipleChoiceViewHolder.regularOption1 = multipleChoiceViewHolder.multipleChoiceStep.getRegularOption1();
        multipleChoiceViewHolder.regularOption2 = multipleChoiceViewHolder.multipleChoiceStep.getRegularOption2();
        multipleChoiceViewHolder.correctOptionJustification = multipleChoiceViewHolder.multipleChoiceStep.getCorrectOptionJustification();
        multipleChoiceViewHolder.incorrectOptionJustification1 = multipleChoiceViewHolder.multipleChoiceStep.getIncorrectOptionJustification1();
        multipleChoiceViewHolder.incorrectOptionJustification2 = multipleChoiceViewHolder.multipleChoiceStep.getIncorrectOptionJustification2();
        multipleChoiceViewHolder.multipleChoiceSteps = multipleChoiceSteps;
        multipleChoiceViewHolder.numberStep.setText((position+1) + ")");

        multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
        multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);

        if(position == 0 && !multipleChoiceViewHolder.multipleChoiceStep.getSolved()){
            multipleChoiceViewHolder.multipleChoiceStep.setExpanded(true);
        }

        if(multipleChoiceViewHolder.multipleChoiceStep.getSolved()){
            if(multipleChoiceViewHolder.multipleChoiceStep.getExpanded()){
                manager.expandCard(multipleChoiceViewHolder);
            }else{
                manager.collapseCard(multipleChoiceViewHolder);
            }

            multipleChoiceViewHolder.explanationStepLayout.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.explanationStep.setVisibility(View.VISIBLE);
            if(multipleChoiceViewHolder.multipleChoiceStep.getNextStepButtonWasPressed()){
                multipleChoiceViewHolder.solveAndNextStepLayout.setVisibility(View.GONE);
            }else {
                multipleChoiceViewHolder.solveAndNextStepLayout.setVisibility(View.VISIBLE);
            }

            manager.markOptionChosen(multipleChoiceViewHolder, multipleChoiceViewHolder.multipleChoiceStep.getChosenOption());
            manager.clearResolutionIcons(multipleChoiceViewHolder);
            if(multipleChoiceViewHolder.multipleChoiceStep.getChosenOption().equals(multipleChoiceViewHolder.multipleChoiceStep.getCorrectOption())){
                manager.showIconForOption(multipleChoiceViewHolder, multipleChoiceViewHolder.multipleChoiceStep.getChosenOption(), R.drawable.solved_right);
            }else {
                manager.showIconForOption(multipleChoiceViewHolder, multipleChoiceViewHolder.multipleChoiceStep.getCorrectOption(), R.drawable.solved_right);
                manager.showIconForOption(multipleChoiceViewHolder, multipleChoiceViewHolder.multipleChoiceStep.getCorrectOption(), R.drawable.solved_wrong);
            }
        }else{
            multipleChoiceViewHolder.card.setVisibility(View.GONE);
            manager.expandCard(multipleChoiceViewHolder);
            // La próxima card la hago visible solo si: es la primer card, o bien, ya se clickeó el botón de "next step" de la card anterior
            if(position == 0
                    || (currentMultipleChoiceSteps.get(position - 1).getSolved() && currentMultipleChoiceSteps.get(position - 1).getNextStepButtonWasPressed())){
                multipleChoiceViewHolder.card.setVisibility(View.VISIBLE);
            }

            multipleChoiceViewHolder.explanationStepLayout.setVisibility(View.GONE);
            multipleChoiceViewHolder.solveAndNextStepLayout.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.solveStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.solveStep.setEnabled(false);
            multipleChoiceViewHolder.solveStep.setBackgroundResource(R.drawable.rounded_corners_disable_button);
            multipleChoiceViewHolder.nextStep.setVisibility(View.GONE);

            multipleChoiceViewHolder.expandCollapseIndicatorAColor.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.expandCollapseIndicatorBColor.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.expandCollapseIndicatorCColor.setVisibility(View.INVISIBLE);

            multipleChoiceViewHolder.isSolved = false;

            multipleChoiceViewHolder.summary.setText("Pendiente");

            manager.setUpSolveButton(multipleChoiceViewHolder.solveStep, multipleChoiceViewHolder,
                    multipleChoiceSteps, currentMultipleChoiceSteps, multipleChoiceViewHolders);

            if(currentMultipleChoiceSteps.size() < multipleChoiceSteps.size()
                    && multipleChoiceViewHolder.solveStep.getVisibility() == View.INVISIBLE){
                multipleChoiceViewHolder.nextStep.setVisibility(View.VISIBLE);
            }

            multipleChoiceViewHolder.chosenOption = null;

            multipleChoiceViewHolder.optionA = multipleChoiceViewHolder.itemView.findViewById(R.id.option_a_id);
            multipleChoiceViewHolder.optionA.setChecked(false);
            multipleChoiceViewHolder.optionA.setEnabled(true);
            multipleChoiceViewHolder.optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.optionB.setChecked(false);
                    multipleChoiceViewHolder.optionC.setChecked(false);
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 1;
                }
            });

            multipleChoiceViewHolder.optionB = multipleChoiceViewHolder.itemView.findViewById(R.id.option_b_id);
            multipleChoiceViewHolder.optionB.setEnabled(true);
            multipleChoiceViewHolder.optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.optionA.setChecked(false);
                    multipleChoiceViewHolder.optionC.setChecked(false);
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 2;
                }
            });

            multipleChoiceViewHolder.optionC = multipleChoiceViewHolder.itemView.findViewById(R.id.option_c_id);
            multipleChoiceViewHolder.optionC.setChecked(false);
            multipleChoiceViewHolder.optionC.setEnabled(true);
            multipleChoiceViewHolder.optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.optionA.setChecked(false);
                    multipleChoiceViewHolder.optionB.setChecked(false);
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 3;
                }
            });
        }

        multipleChoiceViewHolder.expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!multipleChoiceViewHolder.multipleChoiceStep.getExpanded()){
                    manager.expandCard(multipleChoiceViewHolder);
                }else{
                    manager.collapseCard(multipleChoiceViewHolder);
                }
            }
        });

        multipleChoiceViewHolder.explanationStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleChoiceViewHolder.multipleChoiceStep.getDialog().show();
            }
        });
    }

    private String alignEquationCentered(String equationOption) {
        return "\\begin{aligned}{" + equationOption + "}\\end{aligned}";
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
