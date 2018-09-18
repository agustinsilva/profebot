package ar.com.profebot.service;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.EnterEquationOptionsActivity;
import ar.com.profebot.activities.EnterPolinomialEquationOptionsActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;
import io.github.kexanie.library.MathView;

public class EquationManager {

    private static SolveEquationActivity context;
    private static TextView rootsSummary;
    private static AlertDialog dialog;

    public static void showPopUp(){
        dialog.show();
    }

    public static void setUpPopUp(Boolean isFirstStep){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.equation_results_pop_up, null);

        MathView originalEquation = ((MathView) view.findViewById(R.id.original_equation_id));
        originalEquation.setEngine(MathView.Engine.MATHJAX);
        originalEquation.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        originalEquation.setText("$$" + ExpressionsManager.mapToLatexAndReplaceComparator(ExpressionsManager.getEquationAsInfix()) + "$$");

        rootsSummary = (TextView) view.findViewById(R.id.roots_summary_id);

        view.setClipToOutline(true);
        builder.setView(view);
        dialog = builder.create();

        ((Button) view.findViewById(R.id.close_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstStep){
                    v.getContext().startActivity(new Intent(v.getContext(), EnterEquationOptionsActivity.class));
                    dialog.cancel();
                }
                dialog.hide();
            }
        });
    }

    public static void enableSummary(){
        EquationManager.setUpPopUp(false);
        context.enableSummary();
    }

    public static void setContext(SolveEquationActivity context) {
        EquationManager.context = context;
    }
}
