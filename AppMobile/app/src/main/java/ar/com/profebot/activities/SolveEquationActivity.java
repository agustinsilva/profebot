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

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVAdapter adapter;
    private static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView)findViewById(R.id.rv_resolution_id);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        multipleChoiceSteps = this.initializeMultipleChoiceSteps();
        adapter = new RVAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps);
        recyclerView.setAdapter(adapter);
        ((TextView) findViewById(R.id.equation_generated_id)).setText(ExpressionsManager.getEquationAsString());
    }

    public static void refreshRV(){
        recyclerView.scrollToPosition(0);
        recyclerView.stopScroll();
    }

    private List<MultipleChoiceStep> initializeMultipleChoiceSteps(){
        List<MultipleChoiceStep> steps = new ArrayList<>();
        //TODO: pedirle este objeto al backend
        steps.add(new MultipleChoiceStep(ExpressionsManager.getEquationAsLatex(), ExpressionsManager.getEquationAsLatex(), "Pasar restando el 1",
                "Pasar restando el 1 para reducir la suma", "Distribuir el cuadrado en la suma",
                "Pasar multiplicando el 3 que divide a la X", 1, "A - era la opción correcta",
                "B no era la opción correcta porque no se puede distribuir el cuadrado en una suma o resta",
                "C no era la correcta porque no se puede pasar dividiendo el 3 si antes no se despeja el término con X"));
        steps.add(new MultipleChoiceStep(ExpressionsManager.getEquationAsLatex(), ExpressionsManager.getEquationAsLatex(), "Pasar restando el 2",
                "Pasar restando el 2 para reducir la suma", "Distribuir el cuadrado en la suma",
                "Pasar multiplicando el 3 que divide a la X", 1, "A - era la opción correcta",
                "B no era la opción correcta porque no se puede distribuir el cuadrado en una suma o resta",
                "C no era la correcta porque no se puede pasar dividiendo el 3 si antes no se despeja el término con X"));
        steps.add(new MultipleChoiceStep(ExpressionsManager.getEquationAsLatex(), ExpressionsManager.getEquationAsLatex(), "Pasar restando el 3",
                "Pasar restando el 3 para reducir la suma", "Distribuir el cuadrado en la suma",
                "Pasar multiplicando el 3 que divide a la X", 1, "A - era la opción correcta",
                "B no era la opción correcta porque no se puede distribuir el cuadrado en una suma o resta",
                "C no era la correcta porque no se puede pasar dividiendo el 3 si antes no se despeja el término con X"));
        steps.add(new MultipleChoiceStep(ExpressionsManager.getEquationAsLatex(), ExpressionsManager.getEquationAsLatex(), "Pasar restando el 4",
                "Pasar restando el 4 para reducir la suma", "Distribuir el cuadrado en la suma",
                "Pasar multiplicando el 3 que divide a la X", 1, "A - era la opción correcta",
                "B no era la opción correcta porque no se puede distribuir el cuadrado en una suma o resta",
                "C no era la correcta porque no se puede pasar dividiendo el 3 si antes no se despeja el término con X"));
        return steps;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
