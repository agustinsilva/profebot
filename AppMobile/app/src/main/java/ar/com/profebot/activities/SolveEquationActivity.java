package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;

public class SolveEquationActivity extends GlobalActivity {

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoiceAdapter adapter;
    public static RecyclerView recyclerView;
    public static SolveEquationActivity context;

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
        adapter = new RVMultipleChoiceAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps);
        recyclerView.setAdapter(adapter);
    }

    public static SolveEquationActivity getContext(){
        return context;
    }

    private List<MultipleChoiceStep> initializeMultipleChoiceSteps(){
        List<MultipleChoiceStep> steps = new ArrayList<>();
        //TODO: pedirle este objeto al backend
        steps.add(new MultipleChoiceStep("x+3*(2+x)=4", "x+6+3x=4", "Distribuir el 3",
                "Multiplicar por 3 a cada término del binomio 2+x", "", new HashMap<>(),
                "Pasar el 3 dividiendo", "", new HashMap<>(),
                "Pasar el (2+x) dividiendo", "", new HashMap<>(),
                1, "Se puede aplicar distributiva",
                "No se puede pasar el 3 dividiendo por separación de términos",
                "No se puede pasar el (2+x) dividiendo por separación de términos"));
        steps.add(new MultipleChoiceStep("x+6+3x=4", "x+3x=4-6", "Pasar el 6 restando",
                "Pasar el 6 sumando", "", new HashMap<>(),"Pasar el 6 restando", "", new HashMap<>(),
                "Pasar el 3 dividiendo", "", new HashMap<>(),2, "Separando en términos, se puede pasar el término del 6 con el signo contrario",
                "No se puede pasar el 6 sumando porque se debe invertir la operación. Si esta sumando, debe pasar restando.",
                "No se puede pasar el 3 dividiendo por separación de términos."));
        steps.add(new MultipleChoiceStep("x+3x=4-6", "x+3x=-2", "Aplicar la resta",
                "Pasar el 3 dividiendo", "", new HashMap<>(),
                "Aplicar la resta, dando -3 como resultado", "", new HashMap<>(),
                "Aplicar la resta, dando -2 como resultado", "", new HashMap<>(),3, "La diferencia entre 4 y 6 es 2, con signo negativo, por ser el 4 menor que 6",
                "No se puede pasar el 3 dividiendo por separación de términos.",
                "La resta no da -3. Para calcularlo, se hacer la resta 6-4, y cambiar de signo al resultado."));
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
