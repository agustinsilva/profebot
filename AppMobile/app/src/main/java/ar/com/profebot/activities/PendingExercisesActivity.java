package ar.com.profebot.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.profebot.activities.R;
import com.x5.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.PendingExercise;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.PendingExerciseAdapter;

public class PendingExercisesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PendingExercise> pendingExerciseList;
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
                },300);
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando ejercicios...");
        progressDialog.setMax(5);
        progressDialog.show();
        //Codigo para leer de un JSON
        String json = null;
        String jsonHARDCODE = "{\n" +
                "  \"_id\": \"5b53c0e07ed96c51ba2f1757\",\n" +
                "  \"pendingExercises\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"equation\": \"(X+3)*2=10\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"equation\": \"(X+3)/2=10*X\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Operacion Fraccionaria\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"equation\": \"(X+3)*(X+1)=610\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva - Ecuacion Cuadratica\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"equation\": \"(X+3)*(X-10)=60\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva - Ecuacion Cuadratica\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
        JSONObject jsonObject = new JSONObject(jsonHARDCODE);
        JSONArray array = jsonObject.getJSONArray("pendingExercises");
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            PendingExercise item = new PendingExercise(o.getString("equation"), o.getString("subject"));
            pendingExerciseList.add(item);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    adapter = new PendingExerciseAdapter(pendingExerciseList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    if (progressDialog.getProgress() == progressDialog.getMax()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //deberia ser un POST la request
//        StringRequest stringRequestToIAModule = new StringRequest(Request.Method.GET,
//                URL_DATA,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            progressDialog.dismiss();
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray array = jsonObject.getJSONArray("pendingExercises");
//
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject o = array.getJSONObject(i);
//                                PendingExercise item = new PendingExercise(o.getString("equation"),o.getString("subject"));
//                                pendingExerciseList.add(item);
//                            }
//                            adapter = new PendingExerciseAdapter(pendingExerciseList,getApplicationContext());
//                            recyclerView.setAdapter(adapter);
//
//                        } catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequestToIAModule);
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
