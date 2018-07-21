package ar.com.profebot.activities;

import android.app.LauncherActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.PendingExercise;
import ar.com.profebot.service.PendingExerciseAdapter;

public class PendingExercisesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PendingExercise> pendingExerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_exercises_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pendingExerciseList = new ArrayList<>();

        for (int i = 0; i<= 10; i++){
            PendingExercise pendingExercise = new PendingExercise("Ejercicio: "+(i+1),"Ecuaciones");
            pendingExerciseList.add(pendingExercise);
        };
        adapter = new PendingExerciseAdapter(pendingExerciseList, this);

         recyclerView.setAdapter(adapter);
    }
}
