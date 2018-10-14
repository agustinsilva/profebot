package ar.com.profebot.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.profebot.activities.R;

import ar.com.profebot.service.Manager;

public class MainActivity extends AppCompatActivity {
    public static final String EQUATION = "EQUATION";
    public static final String POLINOMIAL = "POLINOMIAL";
    public static final String FUNCTION = "FUNCTION";
    public static String photoReference;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSettings();
        Toolbar myToolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);
        spinner = findViewById(R.id.main_activity_progress_bar_id);

        Button sectionButton;
        sectionButton = findViewById(R.id.enter_equation_id);
        sectionButton.setOnClickListener(button -> {
            SolveEquationActivity.setTypeOfContextOfResolution(SolveEquationActivity.EQUATION_OR_INEQUATION);
            Intent intent = new Intent(MainActivity.this, EnterEquationOptionsActivity.class);
            startActivity(intent);
        });

        sectionButton = findViewById(R.id.enter_polynomial_id);
        sectionButton.setOnClickListener(button -> {
            Intent intent = new Intent(MainActivity.this, EnterPolinomialEquationOptionsActivity.class);
            startActivity(intent);
        });

        sectionButton = findViewById(R.id.enter_function_id);
        sectionButton.setOnClickListener(button -> {
            SolveEquationActivity.setTypeOfContextOfResolution(SolveEquationActivity.FUNCTION);
            Intent intent = new Intent(MainActivity.this, EnterFunctionOptionsActivity.class);
            startActivity(intent);
        });

        this.setMainMenuShortCut();
    }

    private void initializeSettings() {
        this.getSharedPreferences("popUpDomainExplanation", Context.MODE_PRIVATE).edit().putBoolean("popUpDomainExplanation", false).apply();
        this.getSharedPreferences("popUpOriginExplanation", Context.MODE_PRIVATE).edit().putBoolean("popUpOriginExplanation", false).apply();
        this.getSharedPreferences("popUpRootExplanation", Context.MODE_PRIVATE).edit().putBoolean("popUpRootExplanation", false).apply();
        this.getSharedPreferences("popUpImageExplanation", Context.MODE_PRIVATE).edit().putBoolean("popUpImageExplanation", false).apply();
    }

    private void setMainMenuShortCut(){
        GlobalHelper.setMainActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_dropdown,menu);
        MenuItem item = menu.getItem(0);
        if(Manager.hasPendingExercises(this)){
            item.setIcon(R.drawable.more_exercises_notification);
        }else{
            item.setIcon(R.drawable.more_exercises);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.see_exercises_pending_id:
                item.setIcon(R.drawable.more_exercises);
                Intent intent = new Intent(MainActivity.this, PendingExercisesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.profebot_cara)
                .setTitle(R.string.salir)
                .setMessage(R.string.esperamos)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (arg0, arg1) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).create().show();
    }
}
