package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;

public class SolveEquationActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);

        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.equation_drawn_id)).setText(ExpressionsManager.getEquationDrawn());
        ((TextView) findViewById(R.id.equation_generated_id)).setText(ExpressionsManager.getTreeOfExpression().toExpression());
    }
}
