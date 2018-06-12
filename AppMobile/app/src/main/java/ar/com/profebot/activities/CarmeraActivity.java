package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.profebot.PhotoEcuation.MarshmallowPermissions;
import com.profebot.activities.R;

public class CarmeraActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_main);

        if(!MarshmallowPermissions.checkPermissionForCamera(this)) {
            MarshmallowPermissions.requestPermissionForCamera(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
