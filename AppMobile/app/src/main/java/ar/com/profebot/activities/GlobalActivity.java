package ar.com.profebot.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.profebot.activities.R;

public class GlobalActivity extends AppCompatActivity {

    private static Boolean occurredError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalHelper.setUpMainMenuShortCut((TextView)findViewById(R.id.profebot_id));

        this.setUpGlobalExceptionHandler();
        this.showErrorPopUpIfOccurredError();
    }

    private void setUpGlobalExceptionHandler(){
        GlobalActivity activity = this;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                System.out.println("************************************ ERROR: " + paramThrowable.getMessage() + " ************************************");
                for(StackTraceElement stackTraceElement : paramThrowable.getStackTrace()){
                    System.out.println("+++ Clase: " + stackTraceElement.getClassName() + " - Método: " + stackTraceElement.getMethodName() + " - Línea: " + stackTraceElement.getLineNumber());
                }
                occurredError = true;
                startActivity(new Intent(activity, MainActivity.class));
                System.exit(2);
            }
        });
    }

    // TODO: fixear
    protected void showErrorPopUpIfOccurredError(){
        if(occurredError){
            GlobalActivity activity = this;
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Tuve un error mientras resolvía el ejercicio :(");
            alert.setMessage("Lamentablemente ocurrió un error mientras intentaba resolver el ejercicio que ingresaste. ¡Pero no te preocupes! Ahora te llevo al menú principal para que puedas resolver otros ejercicios.");
            alert.setPositiveButton("OK", null);
            alert.show();
            occurredError = false;
        }
    }
}
