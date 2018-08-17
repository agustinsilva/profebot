package ar.com.profebot.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.profebot.activities.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
        RadioGroup signRadioButton = (RadioGroup) findViewById(R.id.signRadioGroup);
        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                int selectedId = signRadioButton.getCheckedRadioButtonId();
                RadioButton radioSignButton = (RadioButton) findViewById(selectedId);
                EquationBuilder.add(radioSignButton.getText().toString()+coefficientTermInput.getText()+"x"+potentialTermInput.getText() + " ");

                finalEquation.setText(beautifierEquation());

                coefficientTermInput.setText("");
                potentialTermInput.setText("");
                coefficientTermInput.requestFocus();
            }
        });
    }

    public String beautifierEquation(){
        //this.EquationBuilder.sort((str1, str2) -> str1.substring(3).compareTo(str2.substring(3)) );

        this.EquationBuilder.sort( new Comparator<String>() {
            public int compare(String str1, String str2) {
                int possubstr1 = str1.indexOf('x') +1 ;
                int possubstr2 = str2.indexOf('x') +1 ;
                String substr1 = str1.substring(possubstr1, str1.length()).trim();
                String substr2 = str2.substring(possubstr2, str2.length()).trim();

                return Integer.valueOf(substr2).compareTo(Integer.valueOf(substr1));
            }
        });


        return String.join("",this.EquationBuilder);
    }
}
