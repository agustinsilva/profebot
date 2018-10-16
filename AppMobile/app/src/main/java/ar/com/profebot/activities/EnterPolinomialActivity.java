package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.profebot.activities.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ImageView enterExponent;
    private ImageView saveTerm;
    private Button goToNextStep;
    private EditText editPolynomial;
    private TextView polynomialText;
    private String nextSign = "+";
    private Boolean enteringCoefficient = true;
    private Boolean enterWasPressed = false;
    private RelativeLayout extraButtonsSection;

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

        extraButtonsSection = (RelativeLayout) findViewById(R.id.extra_buttons_section_id);
        disableExtraButtonsSection();

        enterExponent = (ImageView) findViewById(R.id.enter_exponent_id);
        disableEnterExponent();
        enterExponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageImeActionDone();
                disableExtraButtonsSection();
                showKeyboard();
            }
        });

        saveTerm = (ImageView) findViewById(R.id.save_term_id);
        disableSaveTerm();
        saveTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageImeActionDone();

                hideKeyboard();

                disableEnterExponent();
                disableSaveTerm();
                if(!polynomialTermsEntered.isEmpty()){
                    enableBackButton();
                    enableDeletePlynomial();
                    enableExtraButtonsSection();
                }
            }
        });

        editPolynomial = (EditText) findViewById(R.id.edit_term_id);
        polynomialText = (TextView) findViewById(R.id.polynomial_to_solve_id);
        polynomialText.setMovementMethod(ScrollingMovementMethod.getInstance());
        polynomialText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plusTerm.getVisibility() != View.VISIBLE){
                    editPolynomial.requestFocus();
                    showKeyboard();
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
                if(!polynomialTermsEntered.isEmpty()) {
                    Map<Integer, Double> term = getLastTerm();
                    Integer exponent = FactoringManager.getExponentFrom(term);
                    Double coefficient = term.get(exponent);
                    if (!"".equals(editPolynomial.getText().toString())) {
                        if (enteringCoefficient) {
                            Double newCoefficient = Double.parseDouble(editPolynomial.getText().toString());
                            if(newCoefficient != 0){
                                int factor = 1;
                                if ("-".equals(nextSign)) {
                                    factor = -1;
                                }
                                term.put(exponent, factor * Math.abs(newCoefficient));
                                enableExtraButtonsSection();
                                enableEnterExponent();
                            }
                        } else {
                            disableEnterExponent();
                            enableSaveTerm();
                            enableExtraButtonsSection();
                            term.clear();
                            term.put(Integer.parseInt(editPolynomial.getText().toString()), coefficient);
                        }
                    } else {
                        if (!enterWasPressed) {
                            if (enteringCoefficient) {
                                term.clear();
                                term.put(null, null);
                            } else {
                                term.put(null, coefficient);
                            }
                            disableEnterExponent();
                            disableSaveTerm();
                            disableExtraButtonsSection();
                        }
                    }
                    updatePolynomialTextVisor();
                }
            }
        });

        ((Button)findViewById(R.id.clear_blackboard_id)).setVisibility(View.INVISIBLE);
        ((ImageButton)findViewById(R.id.solve_equation_id)).setVisibility(View.INVISIBLE);

        updatePolynomialTextVisor();

        disableGoToNextStepButton();
        disableBackButton();
        disableDeletePlynomial();

        plusTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusAndMinusLogic("+");
            }
        });

        minusTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusAndMinusLogic("-");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!polynomialTermsEntered.isEmpty()){
                    polynomialTermsEntered.remove(polynomialTermsEntered.size() - 1);
                    updatePolynomialTextVisor();
                }

                hideKeyboard();

                enablePlusTermButton();
                enableMinusTermButton();

                disableEnterExponent();
                disableSaveTerm();

                if(!polynomialTermsEntered.isEmpty()){
                    enableBackButton();
                    enableDeletePlynomial();
                    enableExtraButtonsSection();
                    enableGoToNextStepButton();

                    disableEnterExponent();
                    disableSaveTerm();
                }else{
                    disableGoToNextStepButton();
                    disableBackButton();
                    disableDeletePlynomial();
                    disableExtraButtonsSection();
                }
            }
        });

        deletePlynomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polynomialTermsEntered.clear();
                updatePolynomialTextVisor();
                enableAndDisableButtonsAfterDeletition();
            }
        });

        goToNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.enter_polynomial_section_id)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.sort_and_simplify_section_id)).setVisibility(View.VISIBLE);

                TextView polynomialSortedAndSimplified = (TextView) findViewById(R.id.polynomial_sorted_and_simplified);
                polynomialSortedAndSimplified.setMovementMethod(ScrollingMovementMethod.getInstance());
                polynomialTerms = FactoringManager.getCurrentPolynomialEnteredSortedAndSimplified(polynomialTermsEntered);

                polynomialSortedAndSimplified.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredSortedAndSimplifiedAsText(polynomialTerms)));
            }
        });

        // Segunda pantalla

        ((TextView) findViewById(R.id.sort_and_simplification_justification_id)).setMovementMethod(ScrollingMovementMethod.getInstance());
        ((Button) findViewById(R.id.go_to_edition_section_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.enter_polynomial_section_id)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.sort_and_simplify_section_id)).setVisibility(View.GONE);
            }
        });

        ((Button) findViewById(R.id.go_to_resolution_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SolvePolynomialActivity.class));
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(editPolynomial.getWindowToken(), 0);
    }

    private void showKeyboard(){
        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(editPolynomial, 0);
    }

    private void enableAndDisableButtonsAfterDeletition(){
        disableBackButton();
        disableGoToNextStepButton();
        disableDeletePlynomial();
        disableExtraButtonsSection();

        enablePlusTermButton();
        enableMinusTermButton();
    }

    private void updatePolynomialTextVisor(){
        polynomialText.setText(Html.fromHtml(FactoringManager.getCurrentPolynomialEnteredAsText(polynomialTermsEntered, nextSign, enteringCoefficient)));
    }

    private void plusAndMinusLogic(String sign){
        nextSign = sign;

        disableDeletePlynomial();
        disableGoToNextStepButton();
        disableSaveTerm();
        disableEnterExponent();
        disableExtraButtonsSection();
        disableMinusTermButton();
        disablePlusTermButton();

        enableBackButton();

        enteringCoefficient = true;
        enterWasPressed = false;

        polynomialTermsEntered.add(new HashMap<Integer, Double>(){{
            put(null, null);
        }});

        editPolynomial.setText("");
        updatePolynomialTextVisor();

        editPolynomial.requestFocus();
        showKeyboard();
    }

    class CustomKeyboard implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
            if(actionId == EditorInfo.IME_ACTION_DONE){
                return manageImeActionDone();
            }
            return true;
        }
    }

    private Boolean manageImeActionDone(){
        Boolean keepKeyboardAlive = true;
        enterWasPressed = true;

        if(enterShouldDoNothing()){
            enterWasPressed = false;
            keepKeyboardAlive = true;
        }else if(isSwitchingToExponent()){
            keepKeyboardAlive = switchToExponent();
        }else if(isCreatingANewTerm()){
            keepKeyboardAlive = saveNewTerm();
        }

        updatePolynomialTextVisor();
        editPolynomial.requestFocus();
        return keepKeyboardAlive;
    }

    private Boolean saveNewTerm(){
        enteringCoefficient = !enteringCoefficient;
        editPolynomial.setText("");
        enterWasPressed = true;

        disableSaveTerm();
        enablePlusTermButton();
        enableMinusTermButton();
        enableBackButton();
        enableDeletePlynomial();
        enableGoToNextStepButton();

        return false;
    }

    private Boolean switchToExponent(){
        enteringCoefficient = !enteringCoefficient;
        editPolynomial.setText("");
        enterWasPressed = false;

        disableEnterExponent();
        disableSaveTerm();
        disableExtraButtonsSection();

        return true;
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

    private void disableEnterExponent(){
        enterExponent.setVisibility(View.GONE);
    }

    private void disableSaveTerm(){
        saveTerm.setVisibility(View.GONE);
    }

    private void disablePlusTermButton(){
        plusTerm.setVisibility(View.GONE);
    }

    private void disableMinusTermButton(){
        minusTerm.setVisibility(View.GONE);
    }

    private void disableExtraButtonsSection(){
        extraButtonsSection.setVisibility(View.GONE);
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

    private void enableEnterExponent(){
        enterExponent.setVisibility(View.VISIBLE);
    }

    private void enableSaveTerm(){
        saveTerm.setVisibility(View.VISIBLE);
    }

    private void enablePlusTermButton(){
        plusTerm.setVisibility(View.VISIBLE);
    }

    private void enableMinusTermButton(){
        minusTerm.setVisibility(View.VISIBLE);
    }

    private void enableExtraButtonsSection(){
        extraButtonsSection.setVisibility(View.VISIBLE);
    }
}

