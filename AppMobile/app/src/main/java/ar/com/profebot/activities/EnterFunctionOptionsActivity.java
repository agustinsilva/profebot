package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static ar.com.profebot.activities.MainActivity.FUNCTION;
import static ar.com.profebot.activities.MainActivity.photoReference;

public class EnterFunctionOptionsActivity extends AppCompatActivity {
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_exercise_options_layout);
        photoReference = FUNCTION;
        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);

        spinner = findViewById(R.id.options_progress_bar_id);

        restartScreen();

        ((LinearLayout) findViewById(R.id.option_1_section_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Intent intent = new Intent(v.getContext(), EnterEquationHandDrawActivity.class);
                startActivity(intent);
            }
        });

        ((LinearLayout) findViewById(R.id.option_2_section_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CarmeraActivity.class);
                startActivity(intent);
            }
        });
    }

    private void restartScreen(){
        ((TextView)findViewById(R.id.options_title_id)).setText("¡Elegí cómo ingresar tu función!");
        ((TextView)findViewById(R.id.enter_manually_option_id)).setText("¡Dibujala!");
        ((ImageView)findViewById(R.id.enter_manually_image_option_id)).setBackgroundResource(R.drawable.handdraw_equation1);
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
