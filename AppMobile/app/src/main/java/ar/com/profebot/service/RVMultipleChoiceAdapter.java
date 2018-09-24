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
    private static Manager manager;

    public static class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
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
        multipleChoiceViewHolder.equationBase = multipleChoiceSteps.get(position).getEquationBase();
        multipleChoiceViewHolder.newEquationBase = multipleChoiceSteps.get(position).getNewEquationBase();
        multipleChoiceViewHolder.equationBaseAsLatex.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.equationBaseAsLatex.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        multipleChoiceViewHolder.equationBaseAsLatex.setText("\\(" + ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getEquationBase()) + "\\)");

        String equationAsLatexOption;

        multipleChoiceViewHolder.optionA.setText(multipleChoiceSteps.get(position).getOptionA());
        equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getEquationOptionA());
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

        multipleChoiceViewHolder.optionB.setText(multipleChoiceSteps.get(position).getOptionB());
        equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getEquationOptionB());
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

        multipleChoiceViewHolder.optionC.setText(multipleChoiceSteps.get(position).getOptionC());
        equationAsLatexOption = ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getEquationOptionC());
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

        multipleChoiceViewHolder.correctOption = multipleChoiceSteps.get(position).getCorrectOption();
        multipleChoiceViewHolder.correctOptionJustification = multipleChoiceSteps.get(position).getCorrectOptionJustification();
        multipleChoiceViewHolder.incorrectOptionJustification1 = multipleChoiceSteps.get(position).getIncorrectOptionJustification1();
        multipleChoiceViewHolder.incorrectOptionJustification2 = multipleChoiceSteps.get(position).getIncorrectOptionJustification2();
        multipleChoiceViewHolder.multipleChoiceSteps = multipleChoiceSteps;
        multipleChoiceViewHolder.numberStep.setText((position+1) + ")");

        if(currentMultipleChoiceSteps.size() == 1){
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);
        }else {
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.GONE);
            multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(1f);
        }

        if(multipleChoiceSteps.get(position).getSolved()){
            multipleChoiceViewHolder.explanationStepLayout.setVisibility(View.VISIBLE);
        }else{
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

            manager.setUpSolveButton(multipleChoiceViewHolder.solveStep, multipleChoiceViewHolder, multipleChoiceSteps, currentMultipleChoiceSteps);

            if(currentMultipleChoiceSteps.size() < multipleChoiceSteps.size()
                    && multipleChoiceViewHolder.solveStep.getVisibility() == View.INVISIBLE){
                multipleChoiceViewHolder.nextStep.setVisibility(View.VISIBLE);
            }

            multipleChoiceViewHolder.chosenOption = null;

            multipleChoiceViewHolder.optionA = multipleChoiceViewHolder.itemView.findViewById(R.id.option_a_id);
            multipleChoiceViewHolder.optionA.setChecked(false);
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
            multipleChoiceViewHolder.optionB.setChecked(false);
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
                boolean shouldExpand = multipleChoiceViewHolder.multipleChoiceResolutionStep.getVisibility() == View.GONE;
                if(shouldExpand){
                    multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);
                    multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
                }else{
                    multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.GONE);
                    multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(1f);
                }
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
