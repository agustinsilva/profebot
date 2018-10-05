package ar.com.profebot.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.FunctionParserService;
import ar.com.profebot.resolutor.service.ResolutorService;
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
        String title = getString(R.string.tituloImagenFuncion);
        String explanation = getString(R.string.explicacionImagenFuncion);
        setInformationalPopUp(title, explanation);
    }

    public void rootBtn(View view) throws InvalidExpressionException {
        String title = getString(R.string.tituloRaicesFuncion);
        String explanation = getString(R.string.explicacionRaicesFuncion);
        setInformationalPopUp(title, explanation);
        String rootExpression = equation + " = 0";
        String status = (new ResolutorService()).resolveExpression(rootExpression);
        ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);
        String rootExplanation;
        String rootSolution = "Solución de raíces";
        if(status == "NO_STEPS"){
            rootExplanation = "La función no tiene raíces reales.";
        }
        else{
            rootExplanation = "La función posee las siguientes raices: " + status;
        }
        forwardBtn.setOnClickListener(v -> {
            setTrivialPopUp(rootSolution,rootExplanation);
        });
    }

    public void originBtn(View view) {
        //first show information pop-up if check is valid
        setInformationalPopUp(getString(R.string.informacionOrdenadaOrigen), getString(R.string.ordenadaAlOrigenInformational));

        //Seteo el boton "Adelante" para configurar el proximo pop-up
        ImageButton forwardBtn = popUpView1.findViewById(R.id.forward_pop_up_id);

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set explanation
                //Fijarse si el dominio de la funcion incluye al 0
                //- si lo incluye, ir al resolutor con la ecuacion
                //setPopUpToMultipleChoice(title, explanation);
                //- si lo excluye, ir al trivialPopUp con su texto explicando.
                setTrivialPopUp(getString(R.string.solucionOrdenadaOrigen),getString(R.string.ordenadaAlOrigenTrivial));
            }
        });
    }

    public void domainBtn(View view) {
        //Analizar el dominio de la funcion, si requiere multiple choiceo se muestra una solucion en pop-up
/*        if (isTrivialSolution(equation)) {
                setTrivialPopUp(title, explanation);
        } else {*/
            String title = getString(R.string.tituloDominio);
            String explanation = getString(R.string.explicacionDominio);
        setInformationalPopUp(title, explanation);
        //}
    }

    private void setPopUpToMultipleChoice(String title, String explanation) {
        builder3 = new AlertDialog.Builder(this);
        popUpView3 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView3.setElevation(0f);

        AutofitTextView titleATV = popUpView1.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = popUpView1.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        Button resolveBtn = popUpView1.findViewById(R.id.resolve_pop_up_id);
        resolveBtn.setVisibility(View.GONE);


        popUpView1.setClipToOutline(true);
        builder3.setView(popUpView1);
        dialog3 = builder3.create();
        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog3.show();
        popUpView1.findViewById(R.id.resolve_pop_up_id).setOnClickListener(v -> dialog3.hide());
        ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(v -> dialog1.hide());
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
            dialog2.hide();
            dialog1.hide();
        });

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
