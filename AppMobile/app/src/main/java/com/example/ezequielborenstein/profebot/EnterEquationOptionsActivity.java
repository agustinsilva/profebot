package com.example.ezequielborenstein.profebot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterEquationOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_equation_options_layout);

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
}
