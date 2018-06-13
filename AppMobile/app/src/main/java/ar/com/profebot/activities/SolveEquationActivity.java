package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;
import io.github.kexanie.library.MathView;

public class SolveEquationActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.solve_equation_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.equation_drawn_id)).setText(ExpressionsManager.getEquationDrawn());
        ((TextView) findViewById(R.id.equation_generated_id)).setText(ExpressionsManager.getEquationAsString());
        ((MathView) findViewById(R.id.equation_pretty_format_id)).setText("\\(" + ExpressionsManager.getEquationAsLatex() + "\\)");
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
