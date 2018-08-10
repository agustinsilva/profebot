package ar.com.profebot.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.List;

import ar.com.profebot.Models.PendingExercise;

public class PendingExerciseAdapter extends RecyclerView.Adapter<PendingExerciseAdapter.ViewHolder>{

    private List<PendingExercise> listExercise;
    private Context context;

    public PendingExerciseAdapter(List<PendingExercise> pendingExercises, Context context){
        this.listExercise = pendingExercises;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_exercise,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] equation = listExercise.get(position).getHead().split("=");
        holder.exerciseNumber.setText("Ejercicio " + (position  + 1));
        holder.description.setText(this.getDescription(equation));
        holder.difficulty.setText(this.getDifficulty(equation));
    }

    private String getDifficulty(String[] equation){
        if(equation[1].length() == 1){
            if(equation[0].length() <= 12){
                return "Facil";
            }else{
                return "Intermedio";
            }
        }

        return "Difícil";
    }

    private String getDescription(String[] equation){
        return ExpressionsManager.isQuadraticExpression(equation[0]) && ExpressionsManager.isQuadraticExpression(equation[1]) ? "Ecuación cuadrática" : "Ecuación lineal";
    }

    @Override
    public int getItemCount() {
        return listExercise.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseNumber;
        public TextView description;
        public TextView difficulty;

        public ViewHolder(View itemView) {
            super(itemView);

            exerciseNumber = (TextView) itemView.findViewById(R.id.exercise_number_id);
            description = (TextView) itemView.findViewById(R.id.description_id);
            difficulty = (TextView) itemView.findViewById(R.id.difficulty_id);
        }
    }
}
