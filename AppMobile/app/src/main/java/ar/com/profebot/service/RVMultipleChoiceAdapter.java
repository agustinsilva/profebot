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

public class RVMultipleChoiceAdapter extends RecyclerView.Adapter<RVMultipleChoiceAdapter.MultipleChoiceViewHolder> {

    private List<MultipleChoiceStep> multipleChoiceSteps;
    private static List<MultipleChoiceStep> currentMultipleChoiceSteps;
    private static List<MultipleChoiceViewHolder> multipleChoiceViewHolders = new ArrayList<>();
    private static Manager manager;

    public static class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        MathView equationBase;
        MathView newEquationBase;
        ImageView expandCollapseIndicator;
        ImageView expandCollapseIndicatorColor;
        TextView summary;
        LinearLayout multipleChoiceResolutionStep;
        LinearLayout multipleChoiceSolvedResolutionStep;
        Button solveStep;
        Button nextStep;
        LinearLayout layoutToUse;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        MathView equationOptionA;
        MathView equationOptionB;
        MathView equationOptionC;
        RadioButton correctOptionRadio;
        RadioButton incorrectOptionRadio;
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
                solveStep.setBackgroundResource(R.color.colorGreen);
                solveStep.setTextColor(Color.WHITE);
                manager.setUpSolveButton(solveStep, this, multipleChoiceSteps, currentMultipleChoiceSteps);
            }
        }

        MultipleChoiceViewHolder(View itemView, List<MultipleChoiceStep> currentMultipleChoiceSteps) {
            super(itemView);
            card = itemView.findViewById(R.id.step_id);
            numberStep = itemView.findViewById(R.id.number_step_id);
            equationBase = itemView.findViewById(R.id.equation_base_id);
            newEquationBase = itemView.findViewById(R.id.new_equation_base_id);
            expandCollapseIndicator = itemView.findViewById(R.id.expand_collapse_indicator_id);
            expandCollapseIndicatorColor = itemView.findViewById(R.id.expand_collapse_indicator_color_id);
            summary = itemView.findViewById(R.id.summary_id);

            multipleChoiceResolutionStep = itemView.findViewById(R.id.multiple_choice_section_id);
            multipleChoiceSolvedResolutionStep = itemView.findViewById(R.id.multiple_choice_solved_section_id);
            multipleChoiceResolutionStep.setVisibility(View.GONE);
            multipleChoiceSolvedResolutionStep.setVisibility(View.GONE);

            correctOptionRadio = itemView.findViewById(R.id.option_correct_id);
            incorrectOptionRadio = itemView.findViewById(R.id.option_incorrect_id);

            if(!isSolved){
                // When new card is added to RV. It indicates if has to be initialized as expanded or collapsed (default expanded)
                expandCollapseIndicator.setScaleY(-1f);
                multipleChoiceResolutionStep.setVisibility(View.VISIBLE);

                layoutToUse = multipleChoiceResolutionStep;

                solveStep = itemView.findViewById(R.id.solve_step_id);
                solveStep.setEnabled(false);
                nextStep = itemView.findViewById(R.id.next_step_id);

                MultipleChoiceViewHolder viewHolder = this;

                optionA = itemView.findViewById(R.id.option_a_id);
                equationOptionA = itemView.findViewById(R.id.equation_option_a_id);
                optionA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 1;
                    }
                });

                optionB = itemView.findViewById(R.id.option_b_id);
                equationOptionB = itemView.findViewById(R.id.equation_option_b_id);
                optionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 2;
                    }
                });

                optionC = itemView.findViewById(R.id.option_c_id);
                equationOptionC = itemView.findViewById(R.id.equation_option_c_id);
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
        MultipleChoiceViewHolder multipleChoiceViewHolder = new MultipleChoiceViewHolder(v, currentMultipleChoiceSteps);
        multipleChoiceViewHolders.add(multipleChoiceViewHolder);
        return multipleChoiceViewHolder;
    }

    @Override
    public void onBindViewHolder(MultipleChoiceViewHolder multipleChoiceViewHolder, int position) {
        multipleChoiceViewHolder.equationBase.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.equationBase.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        multipleChoiceViewHolder.equationBase.setText("\\(" + ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getEquationBase()) + "\\)");

        multipleChoiceViewHolder.newEquationBase.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.newEquationBase.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        multipleChoiceViewHolder.newEquationBase.setText("$$" + ExpressionsManager.mapToLatexAndReplaceComparator(multipleChoiceSteps.get(position).getNewEquationBase()) + "$$");

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

        if(multipleChoiceSteps.get(position).getSolved()){
            multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.layoutToUse = multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep;
        }else{
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.layoutToUse = multipleChoiceViewHolder.multipleChoiceResolutionStep;

            multipleChoiceViewHolder.expandCollapseIndicatorColor.setVisibility(View.INVISIBLE);

            multipleChoiceViewHolder.isSolved = false;

            multipleChoiceViewHolder.summary.setText("Pendiente");

            // When new card is added to RV. It indicates if has to be initialized as expanded or collapsed (default expanded)
            multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);

            multipleChoiceViewHolder.solveStep.setEnabled(false);
            if(currentMultipleChoiceSteps.size() < multipleChoiceSteps.size()){
                multipleChoiceViewHolder.nextStep.setVisibility(View.VISIBLE);
            }

            multipleChoiceViewHolder.chosenOption = null;

            multipleChoiceViewHolder.optionA = multipleChoiceViewHolder.itemView.findViewById(R.id.option_a_id);
            multipleChoiceViewHolder.optionA.setChecked(false);
            multipleChoiceViewHolder.optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 1;
                }
            });

            multipleChoiceViewHolder.optionB = multipleChoiceViewHolder.itemView.findViewById(R.id.option_b_id);
            multipleChoiceViewHolder.optionB.setChecked(false);
            multipleChoiceViewHolder.optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 2;
                }
            });

            multipleChoiceViewHolder.optionC = multipleChoiceViewHolder.itemView.findViewById(R.id.option_c_id);
            multipleChoiceViewHolder.optionC.setChecked(false);
            multipleChoiceViewHolder.optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 3;
                }
            });
        }

        multipleChoiceViewHolder.expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean shouldExpand = multipleChoiceViewHolder.layoutToUse.getVisibility() == View.GONE;
                if(shouldExpand){
                    multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);
                    multipleChoiceViewHolder.layoutToUse.setVisibility(View.VISIBLE);
                }else{
                    multipleChoiceViewHolder.layoutToUse.setVisibility(View.GONE);
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
