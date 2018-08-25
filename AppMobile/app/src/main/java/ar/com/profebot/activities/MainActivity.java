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
                Intent intent = new Intent(MainActivity.this, EnterEquationOptionsActivity.class);
                startActivity(intent);
            }
        });
        Button enterPolinomial = (Button)findViewById(R.id.enter_polynomial_id);
        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(MainActivity.this, EnterPolinomialEquationOptionsActivity.class);
                startActivity(intent);
            }
        });

        this.setMainMenuShortCut();
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
