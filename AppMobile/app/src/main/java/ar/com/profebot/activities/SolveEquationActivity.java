package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.profebot.activities.R;

public class SolveEquationActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);

        super.onCreate(savedInstanceState);
    }
}
