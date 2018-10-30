package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.profebot.activities.R;

import ar.com.profebot.service.LocalServer;

public class AuxActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aux);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        ((Button)findViewById(R.id.enter_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1 = ((EditText)findViewById(R.id.num1)).getText().toString();
                String num2 = ((EditText)findViewById(R.id.num2)).getText().toString();
                String num3 = ((EditText)findViewById(R.id.num3)).getText().toString();
                String num4 = ((EditText)findViewById(R.id.num4)).getText().toString();

                if(num1.isEmpty()){
                    LocalServer.setLocalUrl(null);
                }else{
                    String ip = num1 + "." + num2 + "." + num3 + "." + num4;
                    LocalServer.setLocalUrl(ip);
                }
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
