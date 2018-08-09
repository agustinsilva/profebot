package ar.com.profebot.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.profebot.activities.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ar.com.profebot.Models.PendingExercise;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.PendingExerciseAdapter;

public class PendingExercisesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PendingExercise> pendingExerciseList;
    ProgressDialog progressDialog;

    private static final String URL_DATA = "imagen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_exercises_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pendingExerciseList = new ArrayList<>();
        //aca fetcheo la data a las Cards
        loadRecyclerViewData();

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout)findViewById(R.id.swipe_exercises);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe","Cargando nuevos ejercicios..");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        pendingExerciseList.clear();
                        loadRecyclerViewData();
                    }
                },3000);
            }
        });
//
//        for (int i = 0; i<= 10; i++){
//            PendingExercise pendingExercise = new PendingExercise("Ejercicio: "+(i+1),"Ecuaciones");
//            pendingExerciseList.add(pendingExercise);
//        };
//        adapter = new PendingExerciseAdapter(pendingExerciseList, this);
//
//         recyclerView.setAdapter(adapter);
    }

    private void loadRecyclerViewData()
    {
        //Codigo para leer de un JSON
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(this).getString("pendingExercises","");
        if (pendingExercisesJson != "") {
        progressDialog = ProgressDialog.show(PendingExercisesActivity.this,"Generando nuevos ejercicios","Cargando...");
            try {
                JSONObject jsonObject = new JSONObject(pendingExercisesJson);
                JSONArray array = jsonObject.getJSONArray("pendingExercises");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    PendingExercise item = new PendingExercise(o.getString("equation"), o.getString("subject"));
                    pendingExerciseList.add(item);
                }
                adapter = new PendingExerciseAdapter(pendingExerciseList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    progressDialog.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 3000); // after 2 second (or 2000 miliseconds), the task will be active.
        }
        else{
            new AlertDialog.Builder(PendingExercisesActivity.this)
                    .setTitle("Todavía no tenes ejercicios")
                    .setMessage("Una vez que ejercites, te recomendaremos ejercicios para mejorar tus errores.")
                    .setNeutralButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            PendingExercisesActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        ExpressionsManager.setEquationDrawn(null);
        startActivity(new Intent(this, MainActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}
