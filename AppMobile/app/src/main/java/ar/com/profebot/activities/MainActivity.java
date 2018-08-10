package ar.com.profebot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.profebot.activities.R;

public class MainActivity extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);

        spinner = findViewById(R.id.main_activity_progress_bar_id);

        Button enterEquation = (Button)findViewById(R.id.enter_equation_id);
        enterEquation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                spinner.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, EnterEquationOptionsActivity.class);
                startActivity(intent);
            }
        });

        this.setMainMenuShortCut();
        //save Pending exercises json data
        String jsonHARDCODE = "{\n" +
                "  \"_id\": \"5b53c0e07ed96c51ba2f1757\",\n" +
                "  \"pendingExercises\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"equation\": \"(X+3)*2=30\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"equation\": \"(X+3)/2=10*X\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Operacion Fraccionaria\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"equation\": \"(X+3)*(X+1)=610\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva - Ecuacion Cuadratica\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"equation\": \"(X+3)*(X-10)=60\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva - Ecuacion Cuadratica\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"equation\": \"x+6-(x+3+5)*7=(4*5)*(x)-5\",\n" +
                "      \"difficulty\": 3,\n" +
                "      \"subject\": \"Distributiva - Ecuacion Cuadratica\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        //PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit().putString("pendingExercises",jsonHARDCODE.toString()).apply();
    }

    private void setMainMenuShortCut(){
        GlobalHelper.setMainActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_dropdown,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.see_exercises_pending_id:
                Intent intent = new Intent(MainActivity.this, PendingExercisesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
