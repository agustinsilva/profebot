package ar.com.profebot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.profebot.activities.R;

import ar.com.profebot.service.ExpressionsManager;

import static ar.com.profebot.activities.MainActivity.FUNCTION;
import static ar.com.profebot.activities.MainActivity.photoReference;

public class EnterFunctionOptionsActivity extends AppCompatActivity {
    private ImageView handdrawEquationPicture;
    private ImageView photoEquationPicture;
    private RadioButton handdrawEquationOption;
    private RadioButton photoEquationOption;
    private Button startResolution;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_function_options_layout);
        photoReference = FUNCTION;
        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);

        handdrawEquationPicture = findViewById(R.id.handdraw_equation_picture_id);
        photoEquationPicture = findViewById(R.id.photo_equation_picture_id);
        handdrawEquationOption = findViewById(R.id.handdraw_equation_id);
        photoEquationOption = findViewById(R.id.photo_equation_id);
        startResolution = findViewById(R.id.start_resolution_id);
        spinner = findViewById(R.id.options_progress_bar_id);
        restartScreen();

        // When i touch over images

        setImageView(handdrawEquationPicture,R.drawable.photo_equation1,R.drawable.handdraw_equation_selected1,false,true);
        setImageView(photoEquationPicture,R.drawable.photo_equation_selected1,R.drawable.handdraw_equation1,true,false);
        // When i pick radio buttons
        setRadioButton(handdrawEquationOption,R.drawable.photo_equation1,R.drawable.handdraw_equation_selected1);
        setRadioButton(photoEquationOption,R.drawable.photo_equation_selected1,R.drawable.handdraw_equation1);

        startResolution.setOnClickListener(v -> {
            if(handdrawEquationOption.isChecked()){
                spinner.setVisibility(View.VISIBLE);
                Intent intent = new Intent(v.getContext(), EnterEquationHandDrawActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(v.getContext(), CarmeraActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setImageView(ImageView imageView,int photoImage, int handdrawImage,boolean boolPhoto,boolean boolHanddraw){
        imageView.setOnClickListener(v -> {
            setImageOnClick(photoImage,handdrawImage);
            activateStartResolutionButton(true,R.drawable.rounded_corners_main_buttons,Color.WHITE);
            setCheckedOptions(boolPhoto,boolHanddraw);
        });
    }

    private void setRadioButton(RadioButton radioButton,int photoImage, int handdrawImage){
        radioButton.setOnClickListener(v -> {
            setImageOnClick(photoImage,handdrawImage);
            activateStartResolutionButton(true,R.drawable.rounded_corners_main_buttons,Color.WHITE);
        });
    }

    private void setImageOnClick(int idPhoto,int idHanddraw){
        photoEquationPicture.setImageResource(idPhoto);
        handdrawEquationPicture.setImageResource(idHanddraw);
    }
    private void setCheckedOptions(boolean checkedPhoto,boolean checkedHanddraw){
        photoEquationOption.setChecked(checkedPhoto);
        handdrawEquationOption.setChecked(checkedHanddraw);
    }

    private void restartScreen(){
        handdrawEquationPicture.setImageResource(R.drawable.handdraw_equation1);
        photoEquationPicture.setImageResource(R.drawable.photo_equation1);
        handdrawEquationOption.setChecked(false);
        photoEquationOption.setChecked(false);
        activateStartResolutionButton(false,R.drawable.rounded_corners_disable_button,Color.GRAY);
    }

    private void activateStartResolutionButton(boolean enable,int backgroundResource,int color){
        startResolution.setEnabled(enable);
        startResolution.setBackgroundResource(backgroundResource);
        startResolution.setTextColor(color);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        ExpressionsManager.setEquationDrawn(null);
        startActivity(new Intent(this, MainActivity.class));
    }
}
