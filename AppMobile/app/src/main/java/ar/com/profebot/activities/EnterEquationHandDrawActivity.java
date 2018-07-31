package ar.com.profebot.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.math.widget.MathWidgetApi;
import com.profebot.activities.BuildConfig;
import com.profebot.activities.R;

import ar.com.profebot.certificate.MyCertificate;
import ar.com.profebot.service.ExpressionsManager;

public class EnterEquationHandDrawActivity extends GlobalActivity implements
        MathWidgetApi.OnConfigureListener,
        MathWidgetApi.OnRecognitionListener{

    private ProgressBar spinner;
    private ImageButton playButton;
    private Toast invalidEquationMessage;

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

        spinner = (ProgressBar)findViewById(R.id.progress_bar_id);
        this.setUpButtons();

        ((RelativeLayout)findViewById(R.id.blackboard_layout_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpButtons(){
        // TODO: este botón va a dejar de existir --> es para probar la integración con el módulo inteligente
        ((Button) findViewById(R.id.modulo_inteligente_test_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), ModuloInteligenteTestActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.clear_blackboard_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                widget.clear(false);
                spinner.postInvalidateOnAnimation();
                disablePlayButton();
                invalidateToast();
            }
        });

        playButton = (ImageButton) findViewById(R.id.solve_equation_id);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if(ExpressionsManager.expressionDrawnIsValid()){
                    spinner.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(button.getContext(), SolveEquationActivity.class);
                    startActivity(intent);
                }else{
                    invalidEquationMessage = Toast.makeText(button.getContext(),"Fijate si la ecuación está bien escrita!", Toast.LENGTH_LONG);
                    invalidEquationMessage.setGravity(Gravity.CENTER, 0, 0);
                    invalidEquationMessage.show();
                }
            }
        });
    }

    private void disablePlayButton(){
        playButton.setBackgroundResource(R.color.colorGreyText);
        playButton.setEnabled(false);
    }

    private void enablePlayButton(){
        playButton.setBackgroundResource(R.color.colorAccent);
        playButton.setEnabled(true);
    }

    private void invalidateToast(){
        if(invalidEquationMessage != null){
            invalidEquationMessage.cancel();
        }
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
        widget.setOnRecognitionListener(new MathWidgetApi.OnRecognitionListener() {
            @Override
            public void onRecognitionBegin(MathWidgetApi mathWidgetApi) {
            }

            @Override
            public void onRecognitionEnd(MathWidgetApi mathWidgetApi) {
                disablePlayButton();
                if(!widget.isEmpty()){
                    spinner.setVisibility(View.VISIBLE);
                    spinner.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setVisibility(View.GONE);
                        }
                    }, 1200L);
                }

                playButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ExpressionsManager.setEquationDrawn(widget.getResultAsText());
                        if(!"".equals(ExpressionsManager.getEquationDrawn())){
                            enablePlayButton();
                        }
                    }
                }, 1200L);
            }
        });

        widget.setOnPenListener(new MathWidgetApi.OnPenListener() {
            @Override
            public void onPenDown(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {
                spinner.setVisibility(View.GONE);
                disablePlayButton();
                invalidateToast();
            }

            @Override
            public void onPenUp(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {
            }

            @Override
            public void onPenMove(MathWidgetApi mathWidgetApi, CaptureInfo captureInfo) {

            }

            @Override
            public void onPenAbort(MathWidgetApi mathWidgetApi) {
            }
        });

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        ExpressionsManager.setEquationDrawn(null);
        startActivity(new Intent(this, EnterEquationOptionsActivity.class));
    }
}
