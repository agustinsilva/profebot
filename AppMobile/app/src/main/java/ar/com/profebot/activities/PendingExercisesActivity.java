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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import io.github.kexanie.library.MathView;

public class PendingExercisesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PendingExerciseAdapter adapter;
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
                },0);
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

    private void loadRecyclerViewData() {
        //Codigo para leer de un JSON
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(this).getString("pendingExercises","");
        if (pendingExercisesJson != "") {
        progressDialog = ProgressDialog.show(PendingExercisesActivity.this,"Generando nuevos ejercicios","Cargando...");
            try {
                JSONObject jsonObject = new JSONObject(pendingExercisesJson);
                JSONArray array = jsonObject.getJSONArray("pendingExercises");
                if(array.length() != 0){
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        PendingExercise item = new PendingExercise(o.getString("equation"));
                        pendingExerciseList.add(item);
                    }
                    PendingExercisesActivity thisActivity = this;
                    adapter = new PendingExerciseAdapter(pendingExerciseList, this, new OnItemClickListener() {
                        @Override
                        public void onItemClick(PendingExercise pendingExercise) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                            View view = getLayoutInflater().inflate(R.layout.pending_exercise_pop_up, null);
                            ((MathView) view.findViewById(R.id.equation_to_solve_id)).setText("$$" + ExpressionsManager.getEquationAsLatex(pendingExercise.getInfixEquation()) + "$$");
                            ((TextView) view.findViewById(R.id.pop_up_title_id)).setText(pendingExercise.getExerciseId());
                            view.setClipToOutline(true);
                            builder.setView(view);
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            ((Button) view.findViewById(R.id.cancel_id)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            ((Button) view.findViewById(R.id.delete_pending_exercise_id)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteExercise(pendingExercise);
                                    dialog.cancel();
                                }
                            });

                            ((Button) view.findViewById(R.id.solve_pending_equation_id)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String infixEquation = pendingExercise.getInfixEquation();
                                    // If last char is 'new line' --> delete last char
                                    if((int)(infixEquation.substring(infixEquation.length() - 1).charAt(0)) == 10){
                                        infixEquation = infixEquation.substring(0, infixEquation.length() - 1);
                                    }
                                    ExpressionsManager.setEquationDrawn(infixEquation);
                                    if(ExpressionsManager.expressionDrawnIsValid()){
                                        dialog.cancel();
                                        Intent intent = new Intent(thisActivity, SolveEquationActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(thisActivity,"No se pudo armar la resolución de la ecuación: " + ExpressionsManager.getEquationDrawn(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }else{
                    this.showDialogOfNoExercises();
                }
            } catch (Exception e) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
                e.printStackTrace();
            }
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    progressDialog.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 0); // after 2 second (or 2000 miliseconds), the task will be active.
        }else{
            this.showDialogOfNoExercises();
        }
    }

    private void showDialogOfNoExercises(){
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

    private void deleteExercise(PendingExercise exerciseToDelete){
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(this).getString("pendingExercises","");
        if (pendingExercisesJson != "") {
            try{
                JSONArray array = new JSONObject(pendingExercisesJson).getJSONArray("pendingExercises");

                JSONObject equationsJsonToStore = new JSONObject("{\"pendingExercises\":[]}");
                for (int i = 0; i < array.length(); i++) {
                    PendingExercise item = new PendingExercise(array.getJSONObject(i).getString("equation"));
                    if(!item.getInfixEquation().equals(exerciseToDelete.getInfixEquation())){
                        JSONObject pnObj = new JSONObject();
                        pnObj.put("equation", array.getJSONObject(i).getString("equation"));
                        equationsJsonToStore.accumulate("pendingExercises", pnObj);
                    }
                }

                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putString("pendingExercises", equationsJsonToStore.toString()).apply();
                pendingExerciseList.clear();
                loadRecyclerViewData();
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
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
