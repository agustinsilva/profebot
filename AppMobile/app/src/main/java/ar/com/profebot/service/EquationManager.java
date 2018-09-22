package ar.com.profebot.service;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;

import ar.com.profebot.activities.EnterEquationOptionsActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import io.github.kexanie.library.MathView;

public class EquationManager {

    private static SolveEquationActivity context;
    private static TextView rootsSummary;
    private static AlertDialog dialog;

    public static void showPopUp(){
        dialog.show();
    }

    public static void setUpPopUp(Boolean isFirstStep){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.equation_results_pop_up, null);

        rootsSummary = (TextView) view.findViewById(R.id.roots_summary_id);

        view.setClipToOutline(true);
        builder.setView(view);
        dialog = builder.create();

        ((Button) view.findViewById(R.id.close_pop_up_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstStep){
                    v.getContext().startActivity(new Intent(v.getContext(), EnterEquationOptionsActivity.class));
                    dialog.cancel();
                }
                dialog.hide();
            }
        });
    }

    public static void enableSummary(){
        EquationManager.setUpPopUp(false);
        context.enableSummary();
    }

    public static void setContext(SolveEquationActivity context) {
        EquationManager.context = context;
    }
}
