package ar.com.profebot.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.profebot.activities.R;
import java.util.ArrayList;

public class EnterPolinomialActivity extends AppCompatActivity {
    private ArrayList<String> EquationBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polinomial_layout);
        EquationBuilder = new ArrayList<>();
        Button enterPolinomial = (Button)findViewById(R.id.AddEquationButton);
        TextInputEditText coefficientTermInput = (TextInputEditText) findViewById(R.id.coefficientTerm);
        TextInputEditText potentialTermInput = (TextInputEditText) findViewById(R.id.potentialTerm);
        TextView finalEquation = (TextView) findViewById(R.id.finalEquation);
        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EquationBuilder.add(coefficientTermInput.getText()+"x"+potentialTermInput.getText());
                finalEquation.setText(EquationBuilder.toString());
            }
        });
    }
}
