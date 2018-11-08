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
import ar.com.profebot.resolutor.service.ResolutorService;
import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.JustificationsService;
import ar.com.profebot.service.Manager;
import ar.com.profebot.service.RVMultipleChoiceAdapter;

public class SolveEquationActivity extends GlobalActivity {

    public static final String CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS = "equation_finite_solutions";
    public static final String CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_INFINITE_SOLUTIONS = "equation_infinite_solutions";
    public static final String CONTEXT_OF_RESOLUTION_IS_EQUATION_WITHOUT_SOLUTIONS = "equation_without_solutions";

    public static final String CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS = "inequation_interval_solutions";
    public static final String CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITHOUT_SOLUTIONS = "inequation_without_solutions";

    public static final String CONTEXT_OF_RESOLUTION_IS_DOMAIN = "domain";
    public static final String CONTEXT_OF_RESOLUTION_IS_IMAGE = "image";
    public static final String CONTEXT_OF_RESOLUTION_IS_ROOTS = "roots_of_function";
    public static final String CONTEXT_OF_RESOLUTION_IS_ORIGIN_ORD = "origin_ord_of_function";

    public static final int EQUATION_OR_INEQUATION = 0;
    public static final int FUNCTION = 1;

    private static List<MultipleChoiceStep> multipleChoiceSteps;
    private static RVMultipleChoiceAdapter adapter;
    public static RecyclerView recyclerView;
    public static SolveEquationActivity context;
    private static Button seeSummary;
    private static String contextOfResolution;
    private static Integer typeOfContextOfResolution;
    private static String title;
    private static String lastEquation;
    private static Map<String, String> contextOfResolutionTexts;
    private Manager manager;

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
        manager = new EquationManager();
        if(!multipleChoiceSteps.isEmpty()){
            lastEquation = multipleChoiceSteps.get(multipleChoiceSteps.size() - 1).getNewEquationBase();
            if(lastEquation.contains("I=")){
                multipleChoiceSteps.remove(multipleChoiceSteps.size() - 1);
            }
            adapter = new RVMultipleChoiceAdapter(multipleChoiceSteps.get(0), multipleChoiceSteps, manager);
        }else{
            adapter = new RVMultipleChoiceAdapter(null, new ArrayList<>(), manager);
            lastEquation = ExpressionsManager.getTreeOfExpression().toExpression();
        }
        recyclerView.setAdapter(adapter);

        EquationManager.setContext(this);
        LinearLayout equationSummarySection = findViewById(R.id.final_summary_section_id);
        LinearLayout recycleViewSection = findViewById(R.id.recycle_view_section_id);
        setContextOfResolution();
        title = "Ecuación terminada";

        if(multipleChoiceSteps.isEmpty()){
            equationSummarySection.findViewById(R.id.return_to_resolution_id).setVisibility(View.GONE);
            adapter.getManager().showFinalSummary(recycleViewSection, equationSummarySection,
                    contextOfResolutionTexts.get("first"), ExpressionsManager.getTreeOfExpression().toExpression(),
                    contextOfResolutionTexts.get("second"), lastEquation, title, contextOfResolutionTexts.get("type"));
        }

        seeSummary = findViewById(R.id.see_summary_id);
        final String lastEquationToButton = lastEquation;
        seeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getManager().showFinalSummary(recycleViewSection, equationSummarySection,
                        contextOfResolutionTexts.get("first"), ExpressionsManager.getTreeOfExpression().toExpression(),
                        contextOfResolutionTexts.get("second"), lastEquationToButton, title, contextOfResolutionTexts.get("type"));
            }
        });
        manager.disableSummary(seeSummary);
    }

    public static void setTypeOfContextOfResolution(Integer typeOfContextOfResolution) {
        SolveEquationActivity.typeOfContextOfResolution = typeOfContextOfResolution;
    }

    private void setContextOfResolution() {
        contextOfResolution = "";
        if(typeOfContextOfResolution == EQUATION_OR_INEQUATION){
            String comparatorOperator = ExpressionsManager.getRootOfEquation(lastEquation);
            String[] members = lastEquation.split(comparatorOperator);
            // Equation
            if(comparatorOperator.equals("=")){
                if(members[0].equals(members[1])){
                    contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_INFINITE_SOLUTIONS;
                }else if(members[0].toUpperCase().equals("X")){
                    contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS;
                }else{
                    try{
                        Double.parseDouble(members[0]);
                        Double.parseDouble(members[1]);
                        contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITHOUT_SOLUTIONS;
                    }catch (Exception e){
                        // Raíces múltiples
                        contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS;
                    }
                }
            }else{ // Inequation
                if(members[0].equals(members[1])){
                    if(lastEquation.contains("=")){
                        // Ejemplo: 2 >= 2. En este caso, no hay inecuación (el > nunca se cumple)
                        contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_INFINITE_SOLUTIONS;
                    }else{
                        // Ejemplo: 2 > 2. En este caso, no hay inecuación (el > nunca se cumple). Se podría decir que es una ecuación con 0 soluciones
                        contextOfResolution = CONTEXT_OF_RESOLUTION_IS_EQUATION_WITHOUT_SOLUTIONS;
                    }
                }else if(members[1].equals("VACIO")){
                    contextOfResolution = CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITHOUT_SOLUTIONS;
                }else{
                    contextOfResolution = CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS;
                }
            }
        }

        contextOfResolutionTexts = JustificationsService.getContextOfResolutionTexts(contextOfResolution, this);
        String[] members;
        String comparatorOperator = ExpressionsManager.getRootOfEquation(lastEquation);
        switch (contextOfResolution){
            case CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS:
                contextOfResolutionTexts = EquationManager.fixResolutionTextsForRoots(contextOfResolutionTexts, lastEquation);
                break;
            case CONTEXT_OF_RESOLUTION_IS_DOMAIN:
                contextOfResolutionTexts = EquationManager.fixResolutionTextsForFunctionIntervals(contextOfResolutionTexts, lastEquation);
                break;
            case CONTEXT_OF_RESOLUTION_IS_IMAGE:
                contextOfResolutionTexts = EquationManager.fixResolutionTextsForFunctionIntervals(contextOfResolutionTexts, lastEquation);
                break;
            case CONTEXT_OF_RESOLUTION_IS_ROOTS:
                contextOfResolutionTexts = EquationManager.fixResolutionTextsForRoots(contextOfResolutionTexts, lastEquation);
                break;
            case CONTEXT_OF_RESOLUTION_IS_ORIGIN_ORD:
                members = lastEquation.split(comparatorOperator);
                contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/valor/", members[1]);
                break;
            case CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS:
                members = lastEquation.split(comparatorOperator);
                try{
                    Double val = Double.parseDouble(members[1]);
                    StringBuilder interval = new StringBuilder("");
                    switch (comparatorOperator){
                        case ">":
                            interval.append("(" + val + ", +∞ )");
                            break;
                        case ">=":
                            interval.append("[" + val + ", +∞ )");
                            break;
                        case "<":
                            interval.append("(-∞ , " + val + ")");
                            break;
                        case "<=":
                            interval.append("(-∞ , " + val + "]");
                            break;
                    }
                    contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/intervalos/", interval.toString());
                }catch (Exception e){
                    String interval = members[1].replace("INF", "∞");
                    contextOfResolutionTexts = JustificationsService.replacePatterns(contextOfResolutionTexts, "second", "/intervalos/", interval);
                }
                break;
        }
    }

    public void enableSummary(){
        manager.enableSummary(seeSummary);

        LinearLayout finalSummarySection = findViewById(R.id.final_summary_section_id);
        LinearLayout recycleViewSection = findViewById(R.id.recycle_view_section_id);
        seeSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getManager().showFinalSummary(recycleViewSection, finalSummarySection,
                        contextOfResolutionTexts.get("first"), ExpressionsManager.getTreeOfExpression().toExpression(),
                        contextOfResolutionTexts.get("second"), lastEquation, title, contextOfResolutionTexts.get("type"));
            }
        });
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
