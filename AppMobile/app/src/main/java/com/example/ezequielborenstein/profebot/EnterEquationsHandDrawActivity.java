package com.example.ezequielborenstein.profebot;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.myscript.atk.math.widget.MathWidgetApi;

public class EnterEquationsHandDrawActivity extends AppCompatActivity implements
        MathWidgetApi.OnConfigureListener,
        MathWidgetApi.OnRecognitionListener{

    private static final String TAG = "MathDemo";

    private MathWidgetApi widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_equation_handdraw_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(myToolbar);

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
            Toast.makeText(getApplicationContext(), widget.getErrorString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to configure the Math Widget: " + widget.getErrorString());
            return;
        }
        Toast.makeText(getApplicationContext(), "Math Widget Configured", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Math Widget configured!");
    }

    @Override
    public void onRecognitionBegin(MathWidgetApi widget) {
    }

    @Override
    public void onRecognitionEnd(MathWidgetApi widget) {
        if(BuildConfig.DEBUG) {
            // TODO: tomar el widget.getResultAsText() y el widget.getResultAsLaTeX()
        }
    }
}
