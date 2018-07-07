package ar.com.profebot.service;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.List;

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
        ImageView expandCollapseIndicator;
        TextView summary;
        LinearLayout multipleChoiceResolutionStep;
        LinearLayout multipleChoiceSolvedResolutionStep;

        MultipleChoiceViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.step_id);
            equationBase = itemView.findViewById(R.id.equation_base_id);
            expandCollapseIndicator = itemView.findViewById(R.id.expand_collapse_indicator_id);
            summary = itemView.findViewById(R.id.summary_id);
            multipleChoiceResolutionStep = itemView.findViewById(R.id.multiple_choice_section_id);
            multipleChoiceSolvedResolutionStep = itemView.findViewById(R.id.multiple_choice_solved_section_id);
            multipleChoiceResolutionStep.setVisibility(View.GONE);
            multipleChoiceSolvedResolutionStep.setVisibility(View.GONE);

            expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout layoutToUse = summary.getVisibility() == View.GONE ? multipleChoiceResolutionStep : multipleChoiceSolvedResolutionStep;
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
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
