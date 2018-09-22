package ar.com.profebot.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;
import io.github.kexanie.library.MathView;

public class EnterFunctionActivity extends AppCompatActivity {
    private String firstSign = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_function);

        String equation = this.getIntent().getExtras().getString("function");
        setFunctionView(equation);


    }



    private void setFunctionView(String equation) {
        MathView mathComponent = findViewById(R.id.equation_to_solve_id);
        mathComponent.config(
                "MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
        if (!equation.isEmpty() && equation.substring(0,1).contains("-")){
            firstSign = "-";
            equation = equation.substring(1);
        } else {
            firstSign = "";
        }

        mathComponent.setText("\\(\\color{White}{" + firstSign + equation + "}\\)" );
    }

    public void domainBtn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popUpView = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView.setElevation(0f);
        MathView originalEquation = ((MathView) popUpView.findViewById(R.id.original_equation_id));

        TextView rootsSummary = (TextView) popUpView.findViewById(R.id.roots_summary_id);

        rootsSummary.setText("El dominio es un texto que se usa para blablablabla");

        popUpView.setClipToOutline(true);
        builder.setView(popUpView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        ((Button) popUpView.findViewById(R.id.close_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    public void rootBtn(View view) {

    }

    public void originBtn(View view) {
    }

    public void imageBtn(View view) {
    }
}
