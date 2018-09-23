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

import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;
import io.github.kexanie.library.MathView;
import me.grantland.widget.AutofitTextView;

public class EnterFunctionActivity extends AppCompatActivity {
    private String firstSign = "";
    private String equation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_function);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        equation = this.getIntent().getExtras().getString("function");
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
        //Analizar el dominio de la funcion, si requiere multiple choiceo se muestra una solucion en pop-up
/*        if (isTrivialSolution(equation)) {
                setTrivialPopUp(title, explanation);
        } else {*/
            String title = "Dominio de la función";
            String explanation = "En matemáticas, el dominio (conjunto de definición o conjunto de partida) " +
                    "de una función es el conjunto de existencia de ella misma, es decir, los valores para los cuales la" +
                    " función está definida. Es el conjunto de todos los objetos que puede transformar, se denota o bien .";
            setPopUpToMultipleChoice(title, explanation);
        //}
    }

    private void setPopUpToMultipleChoice(String title, String explanation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popUpView = this.getLayoutInflater().inflate(R.layout.function_pop_up, null);
        popUpView.setElevation(0f);

        AutofitTextView titleATV = (AutofitTextView) popUpView.findViewById(R.id.pop_up_title_id);
        titleATV.setText(title);
        TextView explanationTV = (TextView) popUpView.findViewById(R.id.explanation_pop_up);
        explanationTV.setText(explanation);



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
        ImageButton backBtn = (ImageButton) popUpView.findViewById(R.id.back_pop_up_id);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    public void rootBtn(View view) {
        String title = "Raices de la función";
        String explanation = "En matemática, se conoce como raíz (o cero) de un polinomio o de una función (definida sobre un cierto cuerpo algebraico) " +
                "f(x) a todo elemento x perteneciente al dominio de dicha función tal que se cumpla: . Se tiene que 2 y 4 " +
                "son raíces (ver ecuación de segundo grado) ya que f(2) = 0 y f(4) = 0.";
        setPopUpToMultipleChoice(title, explanation);
    }

    public void originBtn(View view) {
        String title = "Ordenada al origen";
        String explanation = "Ordenada en el origen. En la ecuación de la recta: El coeficiente de la x es la pendiente, m. " +
                "El término independiente, b, se llama ordenada en el origen de una recta, siendo (O, b) el punto de corte con el eje de ordenadas..";
        setPopUpToMultipleChoice(title, explanation);
    }

    public void imageBtn(View view) {
        String title = "Imagen de una función";
        String explanation = "la imagen, campo de valores o rango de una función {\\displaystyle f\\colon X\\to Y\\,} " +
                "{\\displaystyle f\\colon X\\to Y\\,}, también llamada la imagen de {\\displaystyle X} X bajo {\\displayst" +
                "yle f} f, es el conjunto contenido en {\\displaystyle Y} Y formado por todos los valores que puede llegar a tomar la función.";
        setPopUpToMultipleChoice(title, explanation);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterFunctionOptionsActivity.class));
        return true;
    }
}
