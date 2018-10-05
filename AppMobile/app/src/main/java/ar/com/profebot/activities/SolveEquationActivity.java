package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.resolutor.service.ResolutorService;
import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;

public class SolveEquationActivity extends GlobalActivity {

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoiceAdapter adapter;
    public static RecyclerView recyclerView;
    public static SolveEquationActivity context;
    private static Button seeSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);

        Toolbar myToolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        context = this;
        
        recyclerView = findViewById(R.id.rv_resolution_id);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        multipleChoiceSteps = this.initializeMultipleChoiceSteps();

        if(!multipleChoiceSteps.isEmpty()){
            adapter = new RVMultipleChoiceAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps, new EquationManager());
        }else{
            adapter = new RVMultipleChoiceAdapter(null, new ArrayList<>(), null);
        }
        recyclerView.setAdapter(adapter);

        EquationManager.setContext(this);
        if(multipleChoiceSteps.isEmpty()){
            EquationManager.showPopUp();
        }

        seeSummary = findViewById(R.id.see_summary_id);
        seeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquationManager.showPopUp();
            }
        });
        disableSummary();
    }

    private void disableSummary(){
        seeSummary.setBackgroundResource(R.drawable.rounded_corners_disable_button);
        seeSummary.setTextColor(Color.GRAY);
        seeSummary.setEnabled(false);
    }

    public void enableSummary(){
        seeSummary.setBackgroundResource(R.drawable.rounded_corners_polynomial_summary);
        seeSummary.setTextColor(Color.WHITE);
        seeSummary.setEnabled(true);
    }

    public static SolveEquationActivity getContext(){
        return context;
    }

    private List<MultipleChoiceStep> initializeMultipleChoiceSteps(){
        List<MultipleChoiceStep> steps;
        steps = (new ResolutorService()).resolveExpression(ExpressionsManager.getTreeOfExpression(), context);
        return steps;
    }

    @Override
    public boolean onSupportNavigateUp() {
        returnToEnterNewEquation();
        return true;
    }

    @Override
    public void onBackPressed() {
        returnToEnterNewEquation();
    }

    private void returnToEnterNewEquation(){
        ExpressionsManager.setEquationDrawn(null);
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
    }
}
