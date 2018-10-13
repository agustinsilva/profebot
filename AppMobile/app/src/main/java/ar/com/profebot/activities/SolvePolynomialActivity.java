package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;
import ar.com.profebot.service.JustificationsService;
import ar.com.profebot.service.Manager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;

public class SolvePolynomialActivity extends GlobalActivity {

    public static final String CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED = "polynomial_factorized";

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoiceAdapter adapter;
    private static Button seeSummary;
    public static RecyclerView recyclerView;
    public static SolvePolynomialActivity context;
    private static String title;
    private static Map<String, String> contextOfResolutionTexts;
    private Manager manager;

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
        manager = new FactoringManager();
        if(!FactoringManager.end){
            adapter = new RVMultipleChoiceAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps, manager);
        }else {
            adapter = new RVMultipleChoiceAdapter(null, new ArrayList<>(), manager);
        }
        recyclerView.setAdapter(adapter);

        LinearLayout equationSummarySection = findViewById(R.id.final_summary_section_id);
        LinearLayout recycleViewSection = findViewById(R.id.recycle_view_section_id);
        contextOfResolutionTexts = JustificationsService.getContextOfResolutionTexts(CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED, this);
        title = "Factoreo terminado";

        if(FactoringManager.end){
            equationSummarySection.findViewById(R.id.return_to_resolution_id).setVisibility(View.GONE);
            adapter.getManager().showFinalSummary(recycleViewSection, equationSummarySection,
                    contextOfResolutionTexts.get("first"), FactoringManager.originalPolynomial,
                    contextOfResolutionTexts.get("second"), FactoringManager.originalPolynomial, title, contextOfResolutionTexts.get("type"));
        }

        seeSummary = findViewById(R.id.see_summary_id);
        manager.disableSummary(seeSummary);
    }



    public void enableSummary(){
        manager.enableSummary(seeSummary);

        LinearLayout finalSummarySection = findViewById(R.id.final_summary_section_id);
        LinearLayout recycleViewSection = findViewById(R.id.recycle_view_section_id);
        seeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getManager().showFinalSummary(recycleViewSection, finalSummarySection,
                        contextOfResolutionTexts.get("first"), FactoringManager.originalPolynomial,
                        contextOfResolutionTexts.get("second"), FactoringManager.getEquationAsLatexAfterFactorizing(), title, contextOfResolutionTexts.get("type"));
            }
        });
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
