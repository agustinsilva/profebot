package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.IAModuleClient;

public class ModuloInteligenteTestActivity extends GlobalActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.modulo_inteligente_test_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        if (ExpressionsManager.expressionDrawnIsValid()) {
            String root = "="; // TODO: parametrizar el root
            // TODO: cambiar a la lógica real que toma el término y el contexto
            String[] expressions = ExpressionsManager.getEquationAsInfix().split(root);
            String jsonBody = "{\"root\": \"" + root + "\",\"term\":\"" + expressions[0] + "\",\"context\":\"" + expressions[1] + "\"}";
            IAModuleClient client = new IAModuleClient(jsonBody, "POST", 3600000, this);
            client.execute("http://10.0.2.2:8080/more/practice");
        }
    }

    @Override
    public boolean onSupportNavigateUp () {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }

    public void updateUI(String response){
        String[] equations = response.split(";");

        ((TextView) findViewById(R.id.ecuacion1)).setText(equations[0]);
        ((TextView) findViewById(R.id.ecuacion2)).setText(equations[1]);
        ((TextView) findViewById(R.id.ecuacion3)).setText(equations[2]);
    }
}
