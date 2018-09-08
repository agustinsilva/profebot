package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;
import ar.com.profebot.service.RVMultipleChoicePlynomialAdapter;

public class SolvePolynomialActivity extends GlobalActivity {

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoicePlynomialAdapter adapter;
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
        recyclerView.setLayoutManager(llm);
        multipleChoiceSteps = this.initializeMultipleChoiceSteps();
        if(!FactoringManager.end){
            adapter = new RVMultipleChoicePlynomialAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps);
        }else {
            adapter = new RVMultipleChoicePlynomialAdapter(null, new ArrayList<>());
        }
        recyclerView.setAdapter(adapter);
        if(FactoringManager.end){
            FactoringManager.showPopUp();
        }
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
        startActivity(new Intent(this, EnterPolinomialActivity.class));
    }
}
