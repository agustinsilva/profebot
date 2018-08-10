package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.profebot.activities.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ar.com.profebot.intelligent.module.IAModuleClient;
import ar.com.profebot.service.ExpressionsManager;

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
            IAModuleClient client = new IAModuleClient(root, expressions[0], expressions[1], this);
            client.execute();
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

        //saveEquationsFromIAModule(equations);
    }

    private void saveEquationsFromIAModule(String[] equations) {
        //Get equations from memory
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(this).getString("pendingExercises","");
        JSONObject equationsJsonToStore = new JSONObject();
        if (pendingExercisesJson != ""){
            try{
                equationsJsonToStore = new JSONObject(pendingExercisesJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            JSONObject equationsJson = new JSONObject(equations.toString());
            String equationStr = equationsJson.getString("equations");
            List<String> equationItems = Arrays.asList(equationStr.split("\\s*;\\s*"));
            for (Iterator<String> equationString = equationItems.iterator(); equationString.hasNext();) {
                JSONObject pnObj = new JSONObject();
                pnObj.put("equation", equationString.next());
                pnObj.put("difficulty", "Facil");
                pnObj.put("subject", "Ecuacion simple");
                equationsJsonToStore.accumulate("pendingExercises", pnObj);
            }
            //Save equations in memory
            PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit()
                    .putString("pendingExercises",equationsJsonToStore.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
