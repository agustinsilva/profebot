package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.profebot.activities.R;

public class GlobalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalHelper.setUpMainMenuShortCut((TextView)findViewById(R.id.profebot_id));
    }
}
