package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.profebot.activities.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.service.ExpressionsManager;

public class ModuloInteligenteTestActivity extends GlobalActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.modulo_inteligente_test_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        TextView ecuacion1 = (TextView) findViewById(R.id.ecuacion1);
        TextView ecuacion2 = (TextView) findViewById(R.id.ecuacion2);
        TextView ecuacion3 = (TextView) findViewById(R.id.ecuacion3);

        if (ExpressionsManager.expressionDrawnIsValid()) {
            String root = "="; // TODO: parametrizar el root
            // TODO: cambiar a la lógica real que toma el término y el contexto
            String[] expressions = ExpressionsManager.getEquationAsInfix().split(root);

            String url = "http://192.168.1.3:8080";

            // Optional Parameters to pass as POST request
            JSONObject body = new JSONObject();
            try {
                // TODO: reemplazar por las asignaciones correctas
                body.put("root", root);
                body.put("term", expressions[0]);
                body.put("context", expressions[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, url, body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String[] equations = response.getString("equations").split(";");
                                ecuacion1.setText(equations[0]);
                                ecuacion2.setText(equations[1]);
                                ecuacion3.setText(equations[2]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error: " + error.getMessage());
                }
            }) {

                // Passing some request headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            // Adding request to request queue
            Volley.newRequestQueue(this).add(jsonObjReq);
        }
    }

    @Override
    public boolean onSupportNavigateUp () {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
