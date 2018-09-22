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
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;

public class SolvePolynomialActivity extends GlobalActivity {

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoiceAdapter adapter;
    private static Button seeSummary;
    public static RecyclerView recyclerView;
    public static SolvePolynomialActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        context = this;
        
        recyclerView = (RecyclerView)findViewById(R.id.rv_resolution_id);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        multipleChoiceSteps = this.initializeMultipleChoiceSteps();
        if(!FactoringManager.end){
            adapter = new RVMultipleChoiceAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps, new FactoringManager());
        }else {
            adapter = new RVMultipleChoiceAdapter(null, new ArrayList<>(), new FactoringManager());
        }
        recyclerView.setAdapter(adapter);
        if(FactoringManager.end){
            FactoringManager.setUpPopUp(true);
            FactoringManager.showPopUp();
        }

        seeSummary = (Button) findViewById(R.id.see_summary_id);
        seeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FactoringManager.showPopUp();
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

    public static SolvePolynomialActivity getContext(){
        return context;
    }

    private List<MultipleChoiceStep> initializeMultipleChoiceSteps(){
        FactoringManager.setContext(this);
        FactoringManager.setPolynomialTerms(EnterPolinomialActivity.polynomialTerms);
        List<MultipleChoiceStep> multipleChoiceSteps = new ArrayList<>();
        multipleChoiceSteps.add(FactoringManager.nextStep());
        return multipleChoiceSteps;
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
        startActivity(new Intent(this, EnterPolinomialEquationOptionsActivity.class));
    }
}
