package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

public class EnterEquationOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_equation_options_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GlobalHelper.setUpMainMenuShortCut((TextView)findViewById(R.id.profebot_id));

        Button option;

        option = (Button) findViewById(R.id.handdraw_equation_id);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), EnterEquationHandDrawActivity.class);
                startActivity(intent);
            }
        });

        option = (Button) findViewById(R.id.photo_equation_id);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), EnterEquationPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}
