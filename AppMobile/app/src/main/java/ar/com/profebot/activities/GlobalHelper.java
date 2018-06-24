package ar.com.profebot.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GlobalHelper {

    private static AppCompatActivity mainActivity;
    private static CarmeraActivity photoActivity;

    public static void setMainActivity(AppCompatActivity activity) {
        if(mainActivity == null){
            mainActivity = activity;
        }
    }

    public static void setPhotoActivity(CarmeraActivity activity) {
        if(photoActivity == null){
            photoActivity = activity;
        }
    }

    public static void setUpMainMenuShortCut(TextView shortCut){
        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View title) {
                Intent intent = new Intent(title.getContext(), mainActivity.getClass());
                mainActivity.startActivity(intent);
            }
        });
    }

    public static AppCompatActivity getMainActivity() {
        return mainActivity;
    }
    public static CarmeraActivity getPhotoActivity() {
        return photoActivity;
    }
}
