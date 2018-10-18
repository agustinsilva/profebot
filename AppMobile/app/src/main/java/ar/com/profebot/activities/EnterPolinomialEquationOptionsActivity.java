package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;

import static ar.com.profebot.activities.MainActivity.POLINOMIAL;
import static ar.com.profebot.activities.MainActivity.photoReference;

public class EnterPolinomialEquationOptionsActivity extends GlobalActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_exercise_options_layout);
        photoReference = POLINOMIAL;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        spinner = findViewById(R.id.options_progress_bar_id);

        restartScreen();

        restartScreen();

        ((LinearLayout) findViewById(R.id.option_1_section_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Intent intent = new Intent(v.getContext(), EnterPolinomialActivity.class);
                startActivity(intent);
            }
        });

        ((LinearLayout) findViewById(R.id.option_2_section_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CarmeraActivity.class);
                //String latex = "\\frac { 3 + 2 ^ { 4 } } { 3 x } = 5";
                //ExpressionsManager.setEquationPhoto(latex);
                //Intent intent = new Intent(button.getContext(), SolveEquationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void restartScreen(){
        ((TextView)findViewById(R.id.options_title_id)).setText("¡Elegí cómo ingresar tu polinomio!");
        ((TextView)findViewById(R.id.enter_manually_option_id)).setText("¡Escribilo!");
        ((ImageView)findViewById(R.id.enter_manually_image_option_id)).setBackgroundResource(R.drawable.polinomial_equation1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        ExpressionsManager.setEquationDrawn(null);
        startActivity(new Intent(this, MainActivity.class));
    }
}
