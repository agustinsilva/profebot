package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import com.profebot.activities.R;
import ar.com.profebot.service.ExpressionsManager;
import static ar.com.profebot.activities.MainActivity.POLINOMIAL;
import static ar.com.profebot.activities.MainActivity.photoReference;

public class EnterPolinomialEquationOptionsActivity extends GlobalActivity {

    private ImageView polinomialEquationPicture;
    private ImageView photoEquationPicture;
    private RadioButton polinomialEquationOption;
    private RadioButton photoEquationOption;
    private Button startResolution;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_polinomial_options_layout);
        photoReference = POLINOMIAL;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        polinomialEquationPicture = (ImageView) findViewById(R.id.polinomial_equation_id);
        photoEquationPicture = (ImageView) findViewById(R.id.photo_equation_picture_id);
        polinomialEquationOption = (RadioButton) findViewById(R.id.polinomial_rb_equation_id);
        photoEquationOption = (RadioButton) findViewById(R.id.photo_equation_id);
        startResolution = (Button) findViewById(R.id.start_resolution_id);
        spinner = findViewById(R.id.options_progress_bar_id);

        restartScreen();

        // When i touch over images

        polinomialEquationPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polinomialEquationPicture.setImageResource(R.drawable.polinomial_equation_selected);
                photoEquationPicture.setImageResource(R.drawable.photo_equation);
                polinomialEquationOption.setChecked(true);
                photoEquationOption.setChecked(false);
                activateStartResolutionButton();
            }
        });

        photoEquationPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEquationPicture.setImageResource(R.drawable.photo_equation_selected);
                polinomialEquationPicture.setImageResource(R.drawable.polinomial_equation);
                photoEquationOption.setChecked(true);
                polinomialEquationOption.setChecked(false);
                activateStartResolutionButton();
            }
        });

        // When i pick radio buttons

        polinomialEquationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polinomialEquationPicture.setImageResource(R.drawable.polinomial_equation_selected);
                photoEquationPicture.setImageResource(R.drawable.photo_equation);
                activateStartResolutionButton();
            }
        });

        photoEquationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEquationPicture.setImageResource(R.drawable.photo_equation_selected);
                polinomialEquationPicture.setImageResource(R.drawable.polinomial_equation);
                activateStartResolutionButton();
            }
        });

        startResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(polinomialEquationOption.isChecked()){
                    spinner.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(v.getContext(), EnterPolinomialActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(v.getContext(), CarmeraActivity.class);
                    //String latex = "\\frac { 3 + 2 ^ { 4 } } { 3 x } = 5";
                    //ExpressionsManager.setEquationPhoto(latex);
                    //Intent intent = new Intent(button.getContext(), SolveEquationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void restartScreen(){
        polinomialEquationPicture.setImageResource(R.drawable.polinomial_equation);
        photoEquationPicture.setImageResource(R.drawable.photo_equation);
        polinomialEquationOption.setChecked(false);
        photoEquationOption.setChecked(false);
        startResolution.setEnabled(false);
        startResolution.setBackgroundResource(R.drawable.rounded_corners_disable_button);
        startResolution.setTextColor(Color.GRAY);
    }

    private void activateStartResolutionButton(){
        startResolution.setEnabled(true);
        startResolution.setBackgroundResource(R.drawable.rounded_corners_main_buttons);
        startResolution.setTextColor(Color.WHITE);
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
