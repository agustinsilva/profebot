package ar.com.profebot.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslCertificate;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.profebot.activities.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;
import io.github.kexanie.library.MathView;

public class EnterPolinomialActivity extends AppCompatActivity {

    public static Map<Integer, Double> polynomialTerms;
    private TextInputEditText coefficientTermInput;
    private TextInputEditText potentialTermInput;
    private ToggleButton signToogleButton;
    private String firstSign = "";
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.polinomial_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        polynomialTerms = new HashMap<>();

        Button enterPolinomial = (Button)findViewById(R.id.AddEquationButton);
        Button eraseLastTerm = (Button)findViewById(R.id.erase_last_term);
        Button deletePolinomial = (Button)findViewById(R.id.delete_polinomial);

        coefficientTermInput = (TextInputEditText) findViewById(R.id.coefficientTerm);
        potentialTermInput = (TextInputEditText) findViewById(R.id.potentialTerm);
        signToogleButton = (ToggleButton)findViewById(R.id.signToogleButton);

        CustomKeyboard customKeyboard = new CustomKeyboard();
        coefficientTermInput.setOnEditorActionListener(customKeyboard);
        potentialTermInput.setOnEditorActionListener(customKeyboard);

        ((Button)findViewById(R.id.clear_blackboard_id)).setVisibility(View.INVISIBLE);
        ((ImageButton)findViewById(R.id.solve_equation_id)).setVisibility(View.INVISIBLE);

        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                addTerm(button);
            }
        });

        eraseLastTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                deleteLastTerm();
            }
        });
        deletePolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                deletePolinomial();
            }
        });

        playButton = (Button)findViewById(R.id.start_resolution_id);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SolvePolynomialActivity.class);
                startActivity(intent);
            }
        });
    }

    class CustomKeyboard implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
            if(actionId == EditorInfo.IME_ACTION_DONE){
                if(coefficientTermInput.getText().length() == 0){
                    coefficientTermInput.requestFocus();
                }else if(potentialTermInput.getText().length() == 0){
                    potentialTermInput.requestFocus();
                }else{
                    coefficientTermInput.clearFocus();
                    potentialTermInput.clearFocus();
                    addTerm(v);
                }
            }
            return false;
        }
    }

    private void addTerm(View view){
        Integer exponent = Integer.parseInt(potentialTermInput.getText().toString());
        if (this.validTerms(coefficientTermInput,potentialTermInput) & reachLimitOfTerms(exponent) ){
            Double coefficient = Double.parseDouble(coefficientTermInput.getText().toString());
            coefficient = signToogleButton.isChecked() ? -1 * coefficient : coefficient;
            Double newCoefficient;
            if(!polynomialTerms.containsKey(exponent)){
                newCoefficient = coefficient;
            }else {
                newCoefficient = (double) polynomialTerms.get(exponent) + coefficient;
                polynomialTerms.remove(exponent);
            }
            if(newCoefficient != 0){
                polynomialTerms.put(exponent, newCoefficient);
            }

            String equation = FactoringManager.getPolynomialGeneralForm(polynomialTerms);
            if (!equation.isEmpty() && equation.substring(0,1).contains("-")){
                firstSign = "-";
                equation = equation.substring(1);
            } else {
                firstSign = "";
            }
            ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                    "MathJax.Hub.Config({\n"+
                            "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                            "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                            "         SVG: { linebreaks: { automatic: true } }\n"+
                            "});");

            equation = equation.replace("x", "a_1");
            System.out.println(equation);
            equation = firstSign + (equation.isEmpty() ? "" : FormulaParser.parseToLatex(equation).replace("{a}_{1}", "x"));

            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + equation + "}\\)" );
            coefficientTermInput.setText("");
            potentialTermInput.setText("");
            enablePlayButton();

            InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void enablePlayButton(){
        playButton.setEnabled(true);
    }

    private void disablePlayButton(){
        playButton.setEnabled(false);
    }

    private boolean validTerms(TextInputEditText coefficientTermInput, TextInputEditText potentialTermInput) {
        StringBuilder message = new StringBuilder("");
        if(coefficientTermInput.getText().toString().matches("")){
            message.append("¡No olvides ingresar el coeficiente!\n");
        }
        if(potentialTermInput.getText().toString().matches("")){
            message.append("¡No olvides ingresar el exponente!\n");
        }

        if (message.length() > 0) {
            Toast toast1 = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return false;
        } else {
            return true;
        }
    }

    public void deletePolinomial(){
        polynomialTerms = new HashMap<>();
        disablePlayButton();
        ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
    }

    public void deleteLastTerm(){
        if (polynomialTerms.size()!= 0) {
            polynomialTerms.remove(Collections.min(polynomialTerms.keySet()));
            if(polynomialTerms.size()!= 0){
                String equation = FactoringManager.getPolynomialGeneralForm(polynomialTerms);
                if (equation.substring(0,1).matches("-")){
                    firstSign = "-";
                    equation = equation.substring(1);
                } else {
                    firstSign = "";
                }
                ExpressionsManager.setPolinomialEquation(equation.concat("=0"), getApplicationContext());
                ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                        "MathJax.Hub.Config({\n"+
                                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                                "         SVG: { linebreaks: { automatic: true } }\n"+
                                "});");
                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + firstSign + ExpressionsManager.getPolinomialEquationAsLatex() + "}\\)" );
            }else{
                disablePlayButton();
                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
            }
        }
        else{
            disablePlayButton();
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
        }
    }
    private boolean reachLimitOfTerms(Integer exponent) {
        int maxSize = 10;
        if (polynomialTerms.size() >= maxSize && !polynomialTerms.containsKey(exponent)) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "El polinomio ingresado no puede tener más de " + maxSize + " términos", Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, EnterPolinomialEquationOptionsActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterPolinomialEquationOptionsActivity.class));
        return true;
    }
}

