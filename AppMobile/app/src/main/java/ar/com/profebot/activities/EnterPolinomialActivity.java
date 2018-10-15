package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;
import io.github.kexanie.library.MathView;

public class EnterPolinomialActivity extends GlobalActivity {

    // (exponente, coeficiente)
    public static Map<Integer, Double> polynomialTerms;
    // [(exponente, coeficiente)]
    public static List<Map<Integer, Double>> polynomialTermsEntered;
    private TextInputEditText coefficientTermInput;
    private TextInputEditText potentialTermInput;
    private ToggleButton signToogleButton;
    private String firstSign = "";
    private ImageView plusTerm;
    private ImageView minusTerm;
    private ImageView back;
    private ImageView deletePlynomial;
    private Button goToNextStep;
    private EditText editPolynomial;
    private TextView polynomialText;
    private String nextSign = "+";
    private Boolean enteringCoefficient = true;
    private Boolean enterWasPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.polinomial_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        polynomialTermsEntered = new ArrayList<>();

        plusTerm = (ImageView) findViewById(R.id.plus_term_id);
        minusTerm = (ImageView) findViewById(R.id.minus_term_id);
        back = (ImageView) findViewById(R.id.back_id);
        goToNextStep = (Button) findViewById(R.id.go_to_next_step_id);
        deletePlynomial = (ImageView) findViewById(R.id.delete_polynomial_id);
        editPolynomial = (EditText) findViewById(R.id.edit_term_id);
        polynomialText = (TextView) findViewById(R.id.equation_to_solve_id);
        polynomialText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plusTerm.getVisibility() != View.VISIBLE){
                    editPolynomial.requestFocus();
                    InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(editPolynomial, 0);
                }
            }
        });

        enteringCoefficient = true;

        editPolynomial.setText("");
        editPolynomial.setOnEditorActionListener(new CustomKeyboard());
        editPolynomial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Map<Integer, Double> term = getLastTerm();
                Integer exponent = FactoringManager.getExponentFrom(term);
                Double coefficient = term.get(exponent);
                if(!"".equals(editPolynomial.getText().toString())){
                    if(enteringCoefficient){
                        term.put(exponent, Double.parseDouble(editPolynomial.getText().toString()));
                    }else{
                        term.clear();
                        term.put(Integer.parseInt(editPolynomial.getText().toString()), coefficient);
                    }
                }else{
                    if(!enterWasPressed){
                        if(enteringCoefficient){
                            term.clear();
                            term.put(null, null);
                        }else{
                            term.put(null, coefficient);
                        }
                    }
                }
                polynomialText.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredAsText(polynomialTermsEntered, "+", enteringCoefficient)));
            }
        });

        ((Button)findViewById(R.id.clear_blackboard_id)).setVisibility(View.INVISIBLE);
        ((ImageButton)findViewById(R.id.solve_equation_id)).setVisibility(View.INVISIBLE);

        polynomialText.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredAsText(polynomialTermsEntered, nextSign, enteringCoefficient)));

        disableGoToNextStepButton();
        disableBackButton();
        disableDeletePlynomial();

        plusTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSign = "+";

                disableDeletePlynomial();
                disableGoToNextStepButton();
                disableMinusTermButton();
                disablePlusTermButton();
                disableBackButton();

                enteringCoefficient = true;
                enterWasPressed = false;

                polynomialTermsEntered.add(new HashMap<Integer, Double>(){{
                    put(null, null);
                }});
                polynomialText.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredAsText(polynomialTermsEntered, "+", enteringCoefficient)));

                editPolynomial.requestFocus();
                InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editPolynomial, 0);
            }
        });


        // TODO: OnClick del goToNextStep
    }

    class CustomKeyboard implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Boolean keepKeyboardAlive = true;
                enterWasPressed = true;

                if(enterShouldDoNothing()){
                    enterWasPressed = false;
                    keepKeyboardAlive = true;
                }else if(isSwitchingToExponent()){
                    enteringCoefficient = !enteringCoefficient;
                    editPolynomial.setText("");
                    enterWasPressed = false;
                    keepKeyboardAlive = true;
                }else if(isCreatingANewTerm()){
                    enteringCoefficient = !enteringCoefficient;
                    editPolynomial.setText("");
                    enterWasPressed = true;
                    keepKeyboardAlive = false;

                    enablePlusTermButton();
                    enableMinusTermButton();
                    enableBackButton();
                    enableDeletePlynomial();
                    enableGoToNextStepButton();
                }

                polynomialText.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredAsText(polynomialTermsEntered, "+", enteringCoefficient)));
                editPolynomial.requestFocus();
                return keepKeyboardAlive;
            }
            return true;
        }
    }

    private Boolean isCreatingANewTerm(){
        Map<Integer, Double> term = getLastTerm();
        Integer exponent = FactoringManager.getExponentFrom(term);
        Double coefficient = term.get(exponent);

        return coefficient != null && exponent != null && !enteringCoefficient;
    }

    private Boolean isSwitchingToExponent(){
        Map<Integer, Double> term = getLastTerm();
        Integer exponent = FactoringManager.getExponentFrom(term);
        Double coefficient = term.get(exponent);

        return coefficient != null && enteringCoefficient && exponent == null && !"".equals(editPolynomial.getText().toString());
    }

    private Boolean enterShouldDoNothing(){
        Map<Integer, Double> term = getLastTerm();
        Integer exponent = FactoringManager.getExponentFrom(term);
        Double coefficient = term.get(exponent);

        return coefficient == null || (!enteringCoefficient && exponent == null);
    }

    private Map<Integer, Double> getLastTerm(){
        return polynomialTermsEntered.get(polynomialTermsEntered.size() - 1);
    }

    private void addTerm(View view){
        if (this.validTerms(coefficientTermInput,potentialTermInput)){
            Integer exponent = Integer.parseInt(potentialTermInput.getText().toString());
            if(!reachLimitOfTerms(exponent)){
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

                ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                        "MathJax.Hub.Config({\n"+
                                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                                "         SVG: { linebreaks: { automatic: true } }\n"+
                                "});");

                String equation = ExpressionsManager.mapToLatexAndReplaceComparator(FactoringManager.getPolynomialGeneralForm(polynomialTerms));

                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + equation + "}\\)" );
                coefficientTermInput.setText("");
                potentialTermInput.setText("");
                //enablePlayButton();

                InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
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
       // disablePlayButton();
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
                //disablePlayButton();
                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
            }
        }
        else{
        //    disablePlayButton();
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
        }
    }
    private boolean reachLimitOfTerms(Integer exponent) {
        int maxSize = 10;
        if (polynomialTerms.size() >= maxSize && !polynomialTerms.containsKey(exponent)) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "El polinomio ingresado no puede tener más de " + maxSize + " términos", Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return true;
        }
        else{
            return false;
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

    private void disableGoToNextStepButton(){
        goToNextStep.setVisibility(View.GONE);
    }

    private void disableBackButton(){
        back.setVisibility(View.GONE);
    }

    private void disableDeletePlynomial(){
        deletePlynomial.setVisibility(View.GONE);
    }

    private void disablePlusTermButton(){
        plusTerm.setVisibility(View.GONE);
    }

    private void disableMinusTermButton(){
        minusTerm.setVisibility(View.GONE);
    }

    private void enableGoToNextStepButton(){
        goToNextStep.setVisibility(View.VISIBLE);
    }

    private void enableBackButton(){
        back.setVisibility(View.VISIBLE);
    }

    private void enableDeletePlynomial(){
        deletePlynomial.setVisibility(View.VISIBLE);
    }

    private void enablePlusTermButton(){
        plusTerm.setVisibility(View.VISIBLE);
    }

    private void enableMinusTermButton(){
        minusTerm.setVisibility(View.VISIBLE);
    }
}

