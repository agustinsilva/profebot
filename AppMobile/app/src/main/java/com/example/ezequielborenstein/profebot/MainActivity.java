package com.example.ezequielborenstein.profebot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);

        Button enterEquation = (Button)findViewById(R.id.enter_equation_id);
        enterEquation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(MainActivity.this, EnterEquationOptionsActivity.class);
                startActivity(intent);
            }
        });

        this.setMainMenuShortCut();
    }

    private void setMainMenuShortCut(){
        GlobalHelper.setMainActivity(this);
    }
}
