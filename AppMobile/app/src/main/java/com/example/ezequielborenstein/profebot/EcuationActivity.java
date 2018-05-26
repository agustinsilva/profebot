package com.example.ezequielborenstein.profebot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EcuationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_equation_keyboard_layout);

        Button enterEquation = (Button) findViewById(R.id.handwrite_ecuation);
        enterEquation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), EnterEquationsHandDrawActivity.class);
                startActivity(intent);
            }
        });
    }
}
