package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.profebot.activities.R;


public class EnterFunctionActivity extends AppCompatActivity {
    public static String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_function);
    }
}
