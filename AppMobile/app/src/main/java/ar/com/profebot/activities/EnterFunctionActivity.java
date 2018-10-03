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

import ar.com.profebot.parser.service.FunctionParserService;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        equation = this.getIntent().getExtras().getString("function");
        setFunctionView(equation);
        equationType = FunctionParserService.getFunctionType(equation);
    }

    public void imageBtn(View view) {
        String title = "Imagen de una función";
        String explanation = "la imagen, campo de valores o rango de una función {\\displaystyle f\\colon X\\to Y\\,} " +
                "{\\displaystyle f\\colon X\\to Y\\,}, también llamada la imagen de {\\displaystyle X} X bajo {\\displayst" +
                "yle f} f, es el conjunto contenido en {\\displaystyle Y} Y formado por todos los valores que puede llegar a tomar la función.";
        setInformationalPopUp(title, explanation);
    }

    public void rootBtn(View view) {
        String title = "Raices de la función";
        String explanation = "En matemática, se conoce como raíz (o cero) de un polinomio o de una función (definida sobre un cierto cuerpo algebraico) " +
                "f(x) a todo elemento x perteneciente al dominio de dicha función tal que se cumpla: . Se tiene que 2 y 4 " +
                "son raíces (ver ecuación de segundo grado) ya que f(2) = 0 y f(4) = 0.";
        setInformationalPopUp(title, explanation);
    }

    public void originBtn(View view) {
        //first show information pop-up if check is valid
        setInformationalPopUp("Información de ordenada al origen", getString(R.string.ordenadaAlOrigenInformational));

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
                setTrivialPopUp("Solución de ordenada al origen",getString(R.string.ordenadaAlOrigenTrivial));
            }
        });
    }

    public void domainBtn(View view) {
        //Analizar el dominio de la funcion, si requiere multiple choiceo se muestra una solucion en pop-up
/*        if (isTrivialSolution(equation)) {
                setTrivialPopUp(title, explanation);
        } else {*/
            String title = "Dominio de la función";
            String explanation = "En matemáticas, el dominio (conjunto de definición o conjunto de partida) " +
                    "de una función es el conjunto de existencia de ella misma, es decir, los valores para los cuales la" +
                    " función está definida. Es el conjunto de todos los objetos que puede transformar, se denota o bien .";
        setInformationalPopUp(title, explanation);
        //}
    }

    private void setPopUpToMultipleChoice(String title, String explanation) {
        builder3 = new AlertDialog.Builder(this);
        popUpView3 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView3.setElevation(0f);

        AutofitTextView titleATV = (AutofitTextView) popUpView1.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = (TextView) popUpView1.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        Button resolveBtn = (Button) popUpView1.findViewById(R.id.resolve_pop_up_id);
        resolveBtn.setVisibility(View.GONE);


        popUpView1.setClipToOutline(true);
        builder3.setView(popUpView1);
        dialog3 = builder3.create();
        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog3.show();
        ((Button) popUpView1.findViewById(R.id.resolve_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.hide();
            }
        });
        ImageButton backBtn = (ImageButton) popUpView1.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.hide();
            }
        });
    }


    private void setTrivialPopUp(String title, String explanation) {
        builder2 = new AlertDialog.Builder(this);
        popUpView2 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView2.setElevation(0f);

        AutofitTextView titleATV = (AutofitTextView) popUpView2.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = (TextView) popUpView2.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        //Hide button resolve
        Button EntendidoBtn = popUpView2.findViewById(R.id.resolve_pop_up_id);
        EntendidoBtn.setText("Entendido!!");
        popUpView2.findViewById(R.id.forward_pop_up_id).setVisibility(View.GONE);
        popUpView2.findViewById(R.id.checkBox).setVisibility(View.GONE);
        popUpView2.setClipToOutline(true);
        builder2.setView(popUpView2);
        dialog2 = builder2.create();
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.show();

        ImageButton backBtn = popUpView2.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.hide();
            }
        });
        EntendidoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.hide();
                dialog1.hide();
            }
        });

    }

    private void setInformationalPopUp(String title, String explanation) {
        builder1 = new AlertDialog.Builder(this);
        popUpView1 = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView1.setElevation(0f);

        AutofitTextView titleATV = (AutofitTextView) popUpView1.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = (TextView) popUpView1.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);
        //Hide button resolve
        Button resolveBtn = (Button) popUpView1.findViewById(R.id.resolve_pop_up_id);
        resolveBtn.setVisibility(View.GONE);

        popUpView1.setClipToOutline(true);
        builder1.setView(popUpView1);
        dialog1 = builder1.create();
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.show();

        ImageButton backBtn = popUpView1.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.hide();
            }
        });

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
