package ar.com.profebot.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.Map;

import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.FunctionParserService;
import ar.com.profebot.resolutor.service.ResolutorService;
import ar.com.profebot.service.ExpressionsManager;
import io.github.kexanie.library.MathView;
import me.grantland.widget.AutofitTextView;

public class EnterFunctionActivity extends AppCompatActivity {
    private String firstSign = "";
    private String equation;
    private FunctionParserService.FunctionType equationType;
    AlertDialog.Builder builder1;
    AlertDialog.Builder builder2;
    AlertDialog.Builder builder3;
    View popUpView1;
    View popUpView2;
    View popUpView3;
    AlertDialog dialog1;
    AlertDialog dialog2;
    AlertDialog dialog3;
    private View spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_function);

        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        equation = this.getIntent().getExtras().getString("function");
        setFunctionView(equation);
        equationType = FunctionParserService.getFunctionType(equation+"=0");
    }

    public void imageBtn(View view) {
        //Set informational pop-up
        Boolean check  = getPreferenceCheck("popUpImageExplanation");
        if(!check) {
            //Set informational pop-up
            String title = getString(R.string.tituloImagenFuncion);
            String explanation = getString(R.string.explicacionImagenFuncion);
            setInformationalPopUp(title, explanation);
            //Set second Pop-up
            ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);
            ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
            forwardBtn.setOnClickListener(v -> {
                setCheckPreference("popUpImageExplanation");
                setTrivialPopUp();
            });
            backBtn.setOnClickListener(v -> {
                setCheckPreference("popUpImageExplanation");
                dialog1.hide();
            });
        }else{
            setTrivialPopUp();
        }
    }

    public void rootBtn(View view) {
        Boolean check = getPreferenceCheck("popUpRootExplanation");
        if (!check) {
            String title = getString(R.string.tituloRaicesFuncion);
            String explanation = getString(R.string.explicacionRaicesFuncion);
            setInformationalPopUp(title, explanation);
            ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);
            ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
            forwardBtn.setOnClickListener(v -> {
                setCheckPreference("popUpRootExplanation");
                setRootTrivialPopUp();
            });
            backBtn.setOnClickListener(v -> {
                setCheckPreference("popUpRootExplanation");
                dialog1.hide();
            });
        } else {
            setRootTrivialPopUp();
        }
    }

    public void originBtn(View view) {
        //first show information pop-up if check is valid
        Boolean check  = getPreferenceCheck("popUpOriginExplanation");

        if(!check) {
            setInformationalPopUp(getString(R.string.informacionOrdenadaOrigen), getString(R.string.ordenadaAlOrigenInformational));

            //Seteo el boton "Adelante" para configurar el proximo pop-up
            ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);
            ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
            forwardBtn.setOnClickListener(v -> {
                setCheckPreference("popUpOriginExplanation");
                setOriginTrivialPopUp();
            });
            backBtn.setOnClickListener(v -> {
                setCheckPreference("popUpOriginExplanation");
                dialog1.hide();
            });
        }
        else {
            setOriginTrivialPopUp();
        }
    }

    private void setOriginTrivialPopUp() {
        String equationModified, function;
        try {
            equationModified = equation.replaceAll("x", "0").replaceAll("X", "0") + "=x";
            String solution = (new ResolutorService()).resolveExpression(equationModified).substring(2);
            function = "\\operatorname{F}(0) = "+ solution +"\\implies y = " + solution;

            View OriginPopUp = setOriginTrivialPopUp(getString(R.string.solucionOrdenadaOrigen), getString(R.string.ordenadaAlOrigenEspecial), function);

            spinner = findViewById(R.id.main_activity_progress_bar_id);
            builder2.setView(OriginPopUp);
            dialog2 = builder2.create();
            dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            if (dialog1!=null){dialog1.hide();}
            spinner.postInvalidateOnAnimation();
            spinner.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTrivialPopUp(OriginPopUp);
                }
            }, 800);

        } catch (InvalidExpressionException e) {
            setTrivialPopUp(getString(R.string.solucionOrdenadaOrigen), getString(R.string.ordenadaAlOrigenTrivial));
        }
    }

    public void domainBtn(View view) {
        Boolean check  = getPreferenceCheck("popUpDomainExplanation");
        if(!check) {
            String title = getString(R.string.tituloDominio);
            String explanation = getString(R.string.explicacionDominio);
            setInformationalPopUp(title, explanation);
            ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);
            ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
            forwardBtn.setOnClickListener(v -> {
                setCheckPreference("popUpDomainExplanation");
                setDomainTrivialPopUp();
            });
            backBtn.setOnClickListener(v -> {
                setCheckPreference("popUpDomainExplanation");
                dialog1.hide();
            });
        }
        else{
            setDomainTrivialPopUp();
        }
    }

    private View setOriginTrivialPopUp(String title, String explanation1, String function) {
        builder2 = new AlertDialog.Builder(this);
        popUpView2 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView2.setElevation(0f);

        AutofitTextView titleATV = popUpView2.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView2.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation1);
        MathView first_equation = popUpView2.findViewById(R.id.first_equation_id);
        first_equation.setVisibility(View.VISIBLE);
        first_equation.setText("\\begin{aligned}{" + function + "}\\end{aligned}");
        TextView explanationTV2 = popUpView2.findViewById(R.id.explanation_pop_up_2);

        popUpView2.findViewById(R.id.forward_pop_up_id).setVisibility(View.GONE);
        popUpView2.findViewById(R.id.checkBox).setVisibility(View.GONE);
        popUpView2.setClipToOutline(true);
        return popUpView2;
    }

    private Boolean getPreferenceCheck(String preferenceKey){
        SharedPreferences prefs = this.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        Boolean check  = prefs.getBoolean(preferenceKey,false);
        return check;
    }

    private void setTrivialPopUp(String title, String explanation) {
        builder2 = new AlertDialog.Builder(this);
        popUpView2 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView2.setElevation(0f);

        AutofitTextView titleATV = popUpView2.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView2.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        //Hide button resolve
        Button EntendidoBtn = popUpView2.findViewById(R.id.resolve_pop_up_id);
        EntendidoBtn.setText(R.string.entendido);
        popUpView2.findViewById(R.id.forward_pop_up_id).setVisibility(View.GONE);
        popUpView2.findViewById(R.id.checkBox).setVisibility(View.GONE);
        popUpView2.setClipToOutline(true);
        builder2.setView(popUpView2);
        dialog2 = builder2.create();
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.show();

        ImageButton backBtn = popUpView2.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(v -> dialog2.hide());
        EntendidoBtn.setOnClickListener(v -> {
            if (dialog3!=null){dialog3.hide();}
            if (dialog2!=null){dialog2.hide();}
            if (dialog1!=null){dialog1.hide();}
        });

    }

    private void setCheckPreference(String preferenceKey){
        SharedPreferences prefs = this.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        CheckBox checkbox = popUpView1.findViewById(R.id.checkBox);
        if(checkbox.isChecked()){
            prefs.edit().putBoolean(preferenceKey, true).apply();
        }
    }
    private void setInformationalPopUp(String title, String explanation) {
        builder1 = new AlertDialog.Builder(this);
        popUpView1 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView1.setElevation(0f);

        AutofitTextView titleATV = popUpView1.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView1.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        //Hide button resolve
        Button resolveBtn = popUpView1.findViewById(R.id.resolve_pop_up_id);
        resolveBtn.setVisibility(View.GONE);

        popUpView1.setClipToOutline(true);
        builder1.setView(popUpView1);
        dialog1 = builder1.create();
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.show();

        ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(v -> dialog1.hide());

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterFunctionOptionsActivity.class));
        return true;
    }

    private void setRootTrivialPopUp(){
        switch(equationType){
            case CONSTANT:{
                setTrivialPopUp(getString(R.string.solucionRaices),getString(R.string.SinRaicesConstante));
                break;
            }
            case HOMOGRAPHIC:
            case LINEAR:
            case QUADRATIC:{
                String rootExpression = equation + " = 0";
                try {
                    String status = (new ResolutorService()).resolveExpression(rootExpression);
                    if((status == getString(R.string.sin_pasos)) || startsWithResolvingRoot(status)){
                        setTrivialPopUp(getString(R.string.solucionRaices),getString(R.string.SinRaices));
                    }
                    else {
                        View rootPopUp = setRootTrivialPopUp(getString(R.string.solucionRaices),getString(R.string.conRaices), status);
                        spinner = findViewById(R.id.main_activity_progress_bar_id);
                        builder2.setView(rootPopUp);
                        dialog2 = builder2.create();
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        if (dialog1!=null){dialog1.hide();}
                        spinner.postInvalidateOnAnimation();
                        spinner.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setTrivialPopUp(rootPopUp);
                            }
                        }, 800);
                    }

                } catch (InvalidExpressionException e) {
                    e.printStackTrace();
                }
                break;
            }
            case INVALID: setTrivialPopUp(getString(R.string.solucionRaices),getString(R.string.funcionInvalida));
                break;
            default:
                break;
        }
    }

    private View setRootTrivialPopUp(String title, String explanation1, String status) {
        builder2 = new AlertDialog.Builder(this);
        popUpView2 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView2.setElevation(0f);

        AutofitTextView titleATV = popUpView2.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView2.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation1);
        MathView first_equation = popUpView2.findViewById(R.id.first_equation_id);
        first_equation.setVisibility(View.VISIBLE);
        first_equation.setText("\\begin{aligned}{" + status + "}\\end{aligned}");

        popUpView2.findViewById(R.id.forward_pop_up_id).setVisibility(View.GONE);
        popUpView2.findViewById(R.id.checkBox).setVisibility(View.GONE);
        popUpView2.setClipToOutline(true);
        return popUpView2;
    }

    private boolean startsWithResolvingRoot(String status){
        String startWith = status.substring(0,2).toLowerCase();
        return !startWith.equals("x=");
    }

    private void setTrivialPopUp(){
        switch (equationType) {
            case CONSTANT:
                setTrivialPopUp("Función Constante", getString(R.string.explicacionInformativaImagen, "constante"));
                break;
            case LINEAR:
                setTrivialPopUp("Función Lineal", getString(R.string.explicacionInformativaImagen, "lineal"));
                break;
            case QUADRATIC:
                //Special Cases in Quadratic
                if (equation == "X^2"){
                    setTrivialPopUp("Función Cuadrática Trivial", getString(R.string.explicacionInformativaImagenCuadraticaTrivial, equation));
                }
                else{
                    //Analizo concavidad e intervalo
                    String concavidad;
                    String intervalo;
                    String second_equation = "y = \\frac{-b}{2*a}";
                    ExpressionsManager.setEquationDrawn(equation+"=0");
                    ExpressionsManager.expressionDrawnIsValid();
                    Map<Integer, Double> equationMapped = ExpressionsManager.parsePolinomialToHashMap(equation);
                    if (equationMapped.get(2) > 0 ) {
                        concavidad = "concavidad positiva";
                        if (equationMapped.get(1) == null) {
                            intervalo = "[ 0, + \\infty )";
                        } else {
                            intervalo = "[ \\frac{" + equationMapped.get(1) + "}{2*" + equationMapped.get(2) + "}, + \\infty)";
                        }
                        View popUp2 = SetImageTrivialPopUpView("Imagen - Función Cuadrática", getString(R.string.explicacionInformativaImagenCuadraticaResolucion, concavidad), intervalo, getString(R.string.explicacionInformativaImagenCuadraticaResolucionParte2), second_equation);
                        spinner = findViewById(R.id.main_activity_progress_bar_id);
                        builder2.setView(popUp2);
                        dialog2 = builder2.create();
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        if (dialog1!=null){dialog1.hide();}
                        spinner.postInvalidateOnAnimation();
                        spinner.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setTrivialPopUp(popUp2);
                            }
                        }, 800);
                    }
                    else{
                        if(equationMapped.get(1) == 0)
                        {
                            intervalo = "( - \\infty , 0 ]";
                        }
                        else{
                            intervalo = "[- \\infty , " + equationMapped.get(1)+"/2"+ equationMapped.get(2) +"]";
                        }
                        concavidad = "concavidad negativa";

                        View popUp2 = SetImageTrivialPopUpView("Imagen - Función Cuadrátic", getString(R.string.explicacionInformativaImagenCuadraticaResolucion, concavidad), intervalo, getString(R.string.explicacionInformativaImagenCuadraticaResolucionParte2 ), second_equation);
                        builder2.setView(popUp2);
                        dialog2 = builder2.create();
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        if (dialog1!=null){dialog1.hide();}
                        spinner = findViewById(R.id.main_activity_progress_bar_id);
                        spinner.postInvalidateOnAnimation();
                        spinner.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setTrivialPopUp(popUp2);
                            }
                        }, 800);
                    }

                }
                break;
            case HOMOGRAPHIC:
                //Special Cases in HOMOGRAPHIC
                int divisionPosition = equation.lastIndexOf("/");
                String denominatorHomographic = equation.substring(divisionPosition + 1).replaceAll("\\(","").replaceAll("\\)","");
                String numeratorHomographic = equation.substring(0,divisionPosition ).replaceAll("\\(","").replaceAll("\\)","");
                Map<Integer, Double> denominatorMapped = ExpressionsManager.parsePolinomialToHashMap(denominatorHomographic);
                Map<Integer, Double> numeratorMapped = ExpressionsManager.parsePolinomialToHashMap(numeratorHomographic);
                String solucion = "R - { "+ numeratorMapped.get(1) +"/"+denominatorMapped.get(1) + " }";

                setTrivialPopUp("Función Homográfica", getString(R.string.explicacionImagenHomografica, solucion));
                break;
            default:
                Log.d("Error Imagen Funcion", "NO encontro ningun tipo de funcion para analizar la imagen");
                break;
        }
    }

    private void setTrivialPopUp(View popUpView2) {


        spinner.setVisibility(View.GONE);
        dialog2.show();
        //Hide button resolve
        Button EntendidoBtn = popUpView2.findViewById(R.id.resolve_pop_up_id);
        EntendidoBtn.setText(R.string.entendido);

        ImageButton backBtn = popUpView2.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(v -> dialog2.hide());
        EntendidoBtn.setOnClickListener(v -> {
            if (dialog3!=null){dialog3.hide();}
            if (dialog2!=null){dialog2.hide();}
            if (dialog1!=null){dialog1.hide();}
        });

    }

    private View SetImageTrivialPopUpView(String title, String explanation1, String intervalo, String explanation2, String second_equation) {
        builder2 = new AlertDialog.Builder(this);
        popUpView2 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView2.setElevation(0f);

        AutofitTextView titleATV = popUpView2.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView2.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation1);
        MathView first_equation = popUpView2.findViewById(R.id.first_equation_id);
        first_equation.setVisibility(View.VISIBLE);
        first_equation.setText("\\begin{aligned}{" + intervalo + "}\\end{aligned}");
        TextView explanationTV2 = popUpView2.findViewById(R.id.explanation_pop_up_2);
        explanationTV2.setVisibility(View.VISIBLE);
        explanationTV2.setText(explanation2);
        MathView second_equationMV = popUpView2.findViewById(R.id.second_equation_id);
        second_equationMV.setVisibility(View.VISIBLE);
        second_equationMV.setText("\\begin{aligned}{" + second_equation + "}\\end{aligned}");

        popUpView2.findViewById(R.id.forward_pop_up_id).setVisibility(View.GONE);
        popUpView2.findViewById(R.id.checkBox).setVisibility(View.GONE);
        popUpView2.setClipToOutline(true);
        return popUpView2;
    }

    private void setDomainTrivialPopUp(){
        switch(equationType){
            case HOMOGRAPHIC:{
                int divisionPosition = equation.lastIndexOf("/");
                String denominatorHomographic = equation.substring(divisionPosition + 1).replaceAll("\\(","").replaceAll("\\)","");
                denominatorHomographic = denominatorHomographic + " = 0";
                String status;
                try {
                    status = (new ResolutorService()).resolveExpression(denominatorHomographic).substring(2);
                    setTrivialPopUp(getString(R.string.solucionDominio),getString(R.string.dominioTrivialHomografica,status));
                } catch (InvalidExpressionException e) {
                    e.printStackTrace();
                }
                break;
            }
            case CONSTANT:{
                setTrivialPopUp(getString(R.string.solucionDominio),getString(R.string.dominioTrivial));
                break;}
            case LINEAR:{
                setTrivialPopUp(getString(R.string.solucionDominio),getString(R.string.dominioTrivial));
                break;}
            case QUADRATIC:{
                setTrivialPopUp(getString(R.string.solucionDominio),getString(R.string.dominioTrivial));
                break;
            }
            case INVALID: setTrivialPopUp(getString(R.string.solucionDominio),getString(R.string.funcionInvalida));
                break;
            default:
                break;
        }
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
}
