package ar.com.profebot.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.myscript.atk.math.widget.MathWidgetApi;
import com.profebot.activities.BuildConfig;
import com.profebot.activities.R;

import ar.com.profebot.certificate.MyCertificate;
import ar.com.profebot.service.ExpressionsManager;

public class EnterEquationHandDrawActivity extends GlobalActivity implements
        MathWidgetApi.OnConfigureListener,
        MathWidgetApi.OnRecognitionListener{

    private static final String TAG = "MathDemo";

    private MathWidgetApi widget;

    private String equation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_equation_handdraw_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        this.setUpWidget();

        this.setUpButtons();
    }

    private void setUpButtons(){
        ((Button) findViewById(R.id.enter_equation_photo_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), EnterEquationPhotoActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.clear_blackboard_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                widget.clear(false);
            }
        });

        ((Button) findViewById(R.id.solve_equation_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if(ExpressionsManager.expressionDrawnIsValid()){
                    Intent intent = new Intent(button.getContext(), SolveEquationActivity.class);
                    startActivity(intent);
                }else{
                    ExpressionsManager.showInvalidEquationMessage(button.getContext());
                }
            }
        });
    }

    private void setUpWidget(){
        widget = (MathWidgetApi) findViewById(R.id.math_widget);
        if (!widget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }

        // Listen to widget events (see onConfigureEnd and onRecognitionEnd APIs)
        widget.setOnConfigureListener(this);
        widget.setOnRecognitionListener(this);

        // references assets directly from the APK to avoid extraction in application
        // file system
        widget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf");

        // The configuration is an asynchronous operation. Callbacks are provided to
        // monitor the beginning and end of the configuration process and update the UI
        // of the input method accordingly.
        //
        // "math" references the conf/math/math.conf file in your assets.
        // "standard" references the configuration name in math.conf
        widget.configure("math", "standard");
    }

    @Override
    protected void onDestroy() {
        widget.setOnRecognitionListener(null);
        widget.setOnConfigureListener(null);

        // release widget's resources
        widget.release();
        super.onDestroy();
    }

    @Override
    public void onConfigureBegin(MathWidgetApi widget) {
    }

    @Override
    public void onConfigureEnd(MathWidgetApi widget, boolean success) {
        if(!success) {
            Log.e(TAG, "Unable to configure the Math Widget: " + widget.getErrorString());
            return;
        }
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Math Widget configured!");
    }

    @Override
    public void onRecognitionBegin(MathWidgetApi widget) {
    }

    @Override
    public void onRecognitionEnd(MathWidgetApi widget) {
        ExpressionsManager.setEquationDrawn(widget.getResultAsText());
        //ExpressionsManager.setEquationDrawnAsLatex(widget.getResultAsLaTeX());
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }
}
