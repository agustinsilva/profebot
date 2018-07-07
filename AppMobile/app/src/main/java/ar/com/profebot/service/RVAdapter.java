package ar.com.profebot.service;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        MultipleChoiceViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.step_id);
            equationBase = (MathView)itemView.findViewById(R.id.equation_base_id);
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
