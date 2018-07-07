package ar.com.profebot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.profebot.PhotoEcuation.MarshmallowPermissions;
import com.profebot.activities.R;

public class CarmeraActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_main);
        GlobalHelper.setPhotoActivity(this);
        if(!MarshmallowPermissions.checkPermissionForCamera(this)) {
            MarshmallowPermissions.requestPermissionForCamera(this);
        }
        View cameraFragment = (View)findViewById(R.id.camera_photo_profe);
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
