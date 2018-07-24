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

public class EnterEquationOptionsActivity extends GlobalActivity {

    private ImageView handdrawEquationPicture;
    private ImageView photoEquationPicture;
    private RadioButton handdrawEquationOption;
    private RadioButton photoEquationOption;
    private Button startResolution;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_equation_options_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        handdrawEquationPicture = (ImageView) findViewById(R.id.handdraw_equation_picture_id);
        photoEquationPicture = (ImageView) findViewById(R.id.photo_equation_picture_id);
        handdrawEquationOption = (RadioButton) findViewById(R.id.handdraw_equation_id);
        photoEquationOption = (RadioButton) findViewById(R.id.photo_equation_id);
        startResolution = (Button) findViewById(R.id.start_resolution_id);
        spinner = findViewById(R.id.options_progress_bar_id);

        restartScreen();

        // When i touch over images

        handdrawEquationPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation_selected);
                photoEquationPicture.setImageResource(R.drawable.photo_equation);
                handdrawEquationOption.setChecked(true);
                photoEquationOption.setChecked(false);
                activateStartResolutionButton();
            }
        });

        photoEquationPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEquationPicture.setImageResource(R.drawable.photo_equation_selected);
                handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation);
                photoEquationOption.setChecked(true);
                handdrawEquationOption.setChecked(false);
                activateStartResolutionButton();
            }
        });

        // When i pick radio buttons

        handdrawEquationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation_selected);
                photoEquationPicture.setImageResource(R.drawable.photo_equation);
                activateStartResolutionButton();
            }
        });

        photoEquationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEquationPicture.setImageResource(R.drawable.photo_equation_selected);
                handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation);
                activateStartResolutionButton();
            }
        });

        startResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handdrawEquationOption.isChecked()){
                    spinner.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(v.getContext(), EnterEquationHandDrawActivity.class);
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
        handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation);
        photoEquationPicture.setImageResource(R.drawable.photo_equation);
        handdrawEquationOption.setChecked(false);
        photoEquationOption.setChecked(false);
        startResolution.setEnabled(false);
        startResolution.setBackgroundResource(R.color.colorGrey);
        startResolution.setTextColor(Color.GRAY);
    }

    private void activateStartResolutionButton(){
        startResolution.setEnabled(true);
        startResolution.setBackgroundResource(R.color.colorGreen);
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
