package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.myscript.atk.math.widget.MathWidgetApi;
import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;

public class EnterEquationPhotoActivity extends GlobalActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_equation_photo_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
