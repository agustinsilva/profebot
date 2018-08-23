package ar.com.profebot.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.profebot.activities.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import ar.com.profebot.service.ExpressionsManager;
import io.github.kexanie.library.MathView;

public class EnterPolinomialActivity extends AppCompatActivity {
    private ArrayList<String> EquationBuilder;
    private ArrayList<String> EquationUnordered;
    private String firstSign = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polinomial_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EquationBuilder = new ArrayList<>();
        EquationUnordered = new ArrayList<>();
        Button enterPolinomial = (Button)findViewById(R.id.AddEquationButton);
        Button deleterTerm = (Button)findViewById(R.id.delete_last_term);

        TextInputEditText coefficientTermInput = (TextInputEditText) findViewById(R.id.coefficientTerm);
        TextInputEditText potentialTermInput = (TextInputEditText) findViewById(R.id.potentialTerm);
        ToggleButton signToogleButton = (ToggleButton)findViewById(R.id.signToogleButton);
        coefficientTermInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return true;
            }
        });
        potentialTermInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {}
                return true;
            }
        });
        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if (this.validTerms(coefficientTermInput,potentialTermInput) & reachLimitOfTerms() ){
                    String termSign = signToogleButton.isChecked() ? "-":"+";
                    EquationBuilder.add(termSign + coefficientTermInput.getText() + "x^" + potentialTermInput.getText());
                    EquationUnordered.add(termSign + coefficientTermInput.getText() + "x^" + potentialTermInput.getText());
                    //Mapping First Character and setting equation as latex
                    if (beautifierEquation().trim().substring(0,1).matches("-")){firstSign = "-";}
                    ExpressionsManager.setPolinomialEquation(beautifierEquation().trim().substring(1).concat("=0"), getApplicationContext());
                    ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                            "MathJax.Hub.Config({\n"+
                                    "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                                    "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                                    "         SVG: { linebreaks: { automatic: true } }\n"+
                                    "});");
                    ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + firstSign + ExpressionsManager.getPolinomialEquationAsLatex() + "}\\)" );
                    coefficientTermInput.setText("");
                    potentialTermInput.setText("");
                    coefficientTermInput.requestFocus();
            }
        }

        private boolean validTerms(TextInputEditText coefficientTermInput, TextInputEditText potentialTermInput) {
            if ((coefficientTermInput.getText().toString().matches("") || potentialTermInput.getText().toString().matches(""))) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Ingresá los terminos del polinomio", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.show();
                return false;
            } else {
                return true;
            }
        }
        });

        deleterTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                deleteLastTerm();
            }
        });
    }

    public void deleteLastTerm(){
        String lastTerm = this.EquationUnordered.get(EquationUnordered.size()-1);
        this.EquationBuilder.remove(lastTerm);
        this.EquationUnordered.remove(lastTerm);
        if (EquationBuilder.size()!= 0) {
            if (beautifierEquation().trim().substring(0,1).matches("-")){firstSign = "-";}
            ExpressionsManager.setPolinomialEquation(beautifierEquation().trim().substring(1).concat("=0"), getApplicationContext());
            ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                    "MathJax.Hub.Config({\n"+
                            "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                            "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                            "         SVG: { linebreaks: { automatic: true } }\n"+
                            "});");
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + firstSign + ExpressionsManager.getPolinomialEquationAsLatex() + "}\\)" );
        }
        else{
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
        }
    }
    private boolean reachLimitOfTerms() {
        if (this.EquationBuilder.size() > 9) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "No podés ingresar más de 10 términos", Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return false;
        }
        else{
            return true;
        }
    }

    public String beautifierEquation(){
        this.EquationBuilder.sort( new Comparator<String>() {
            public int compare(String str1, String str2) {
                int possubstr1 = str1.indexOf('^') + 1;
                int possubstr2 = str2.indexOf('^') + 1;
                String substr1 = str1.substring(possubstr1, str1.length()).trim();
                String substr2 = str2.substring(possubstr2, str2.length()).trim();

                return Integer.valueOf(substr2).compareTo(Integer.valueOf(substr1));
            }
        });
        return String.join("",this.EquationBuilder).replaceAll("x\\^0","");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}

