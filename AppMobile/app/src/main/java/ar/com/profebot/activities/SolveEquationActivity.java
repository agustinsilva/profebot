package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.RVAdapter;

public class SolveEquationActivity extends GlobalActivity {

    private List<MultipleChoiceStep> multipleChoiceSteps;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        RecyclerView resolution = (RecyclerView)findViewById(R.id.rv_resolution_id);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resolution.setLayoutManager(llm);
        multipleChoiceSteps = this.initializeMultipleChoiceSteps();
        adapter = new RVAdapter(multipleChoiceSteps);
        resolution.setAdapter(adapter);

        ((TextView) findViewById(R.id.equation_generated_id)).setText(ExpressionsManager.getEquationAsString());
    }

    private List<MultipleChoiceStep> initializeMultipleChoiceSteps(){
        List<MultipleChoiceStep> steps = new ArrayList<>();
        //TODO: pedirle este objeto al backend
        steps.add(new MultipleChoiceStep(ExpressionsManager.getEquationAsLatex()));
        return steps;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
