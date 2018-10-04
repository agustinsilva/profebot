package com.profebot.PhotoEcuation;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.profebot.PhotoEcuation.camera.CameraPreview;
import com.profebot.PhotoEcuation.camera.CameraUtil;
import com.profebot.PhotoEcuation.cropcontrol.CropController;
import com.profebot.activities.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ar.com.profebot.activities.EnterFunctionActivity;
import ar.com.profebot.activities.EnterPolinomialActivity;
import ar.com.profebot.activities.SolveEquationActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import ar.com.profebot.parser.service.FunctionParserService;
import ar.com.profebot.service.ExpressionsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kexanie.library.MathView;
import me.grantland.widget.AutofitTextView;

import static ar.com.profebot.activities.MainActivity.EQUATION;
import static ar.com.profebot.activities.MainActivity.FUNCTION;
import static ar.com.profebot.activities.MainActivity.POLINOMIAL;
import static ar.com.profebot.activities.MainActivity.photoReference;
import static ar.com.profebot.parser.service.FunctionParserService.FunctionType.INVALID;

public class CameraFragment extends Fragment {
    private static final String TAG = CameraFragment.class.getSimpleName();

    //region UI
    @BindView(R.id.takePhotoButton)
    ImageView mTakePhotoButton;
    @BindView(R.id.crop_status_text_view)
    TextView mCropStatusTextView;
    @BindView(R.id.helpButton)
    ImageButton helpButton;
    @BindView(R.id.help2_text_view)
    TextView Help2TextView;
    @BindView(R.id.help_text_view)
    TextView HelpTextView;
    @BindView(R.id.formula_one)
    MathView formula_one;
    @BindView(R.id.crop_control)
    RelativeLayout mCropControl;
    @BindView(R.id.image_view)
    ImageView mCropImageView;
    @BindView(R.id.camera_preview)
    FrameLayout mCameraPreview;
    @BindView(R.id.view_scan_line)
    View mScanLine;
    @BindView(R.id.camera_snapshot)
    ImageView mCameraSnapshot;

    @BindView(R.id.webview_container)
    ViewGroup mWebViewContainer;

    @BindView(R.id.webView)
    WebView mWebView;

    @OnClick(R.id.nextPhoto)
    void onNextPhotoClicked() {
        if (mWebViewContainer.getVisibility() == View.VISIBLE) {
            mWebViewContainer.setVisibility(View.INVISIBLE);


            mTakePhotoButton.setEnabled(true);
            startPreview();
        }
    }

    @OnClick(R.id.crop_control)
    void onTapCropView() {
        if (mPreview != null) {
            try {
                Log.e(TAG, "Start auto-focusing");
                mPreview.autoFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.takePhotoButton)
    void onTakePhotoButtonClicked() {
        if (mCamera == null) return;

        mCropStatusTextView.setText(R.string.taking_picture);
        Log.e(TAG, "Sacando foto");
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                stopPreview();

                Bitmap bm = ImageUtil.toBitmap(data);
                if (bm.getWidth() > bm.getHeight()) {
                    bm = ImageUtil.rotate(bm, 90);
                }
                Log.e(TAG, "got bitmap size = " + bm.getWidth() + ", " + bm.getHeight());

                Rect cropFrame = new Rect(mCropControl.getLeft(), mCropControl.getTop(), mCropControl.getRight(), mCropControl.getBottom());

                int inset = (int) getResources().getDimension(R.dimen.crop_corner_width_halved);
                int viewWidth = getView().getWidth();
                int viewHeight = getView().getHeight();
                int cropWidth = cropFrame.width() - inset * 2;
                int cropHeight = cropFrame.height() - inset * 2;

                int centerX = bm.getWidth() / 2;
                int centerY = bm.getHeight() / 2;
                int targetWidth = (cropWidth * bm.getWidth()) / viewWidth;
                int targetHeight = (cropHeight * bm.getHeight()) / viewHeight;

                Log.e(TAG, "screen size = " + viewWidth + ", " + viewHeight);
                Log.e(TAG, "target size = " + targetWidth + ", " + targetHeight);

                Bitmap result = Bitmap.createBitmap(bm, centerX - targetWidth / 2, centerY - targetHeight / 2, targetWidth, targetHeight);
                result = Bitmap.createScaledBitmap(result, targetWidth / 2, targetHeight / 2, false);

                //usando imagen hardcodeada
                Bitmap ecuacionDemo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ec_lineal_2);
                imageCropped(result);


                //region avoid black camera issue - create snapshot and load to imageview overlay
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

                        File files = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File outFile = new File(files, sdf.format(new Date()).concat(".").concat("jpg"));
                        FileOutputStream outStream = null;
                        try {
                            outStream = new FileOutputStream(outFile);
                            outStream.write(data);
                            outStream.flush();
                            outStream.close();
                            Log.e(TAG, "image saved to file " + outFile.getAbsolutePath());

                            Picasso.with(getContext()).load(outFile.getAbsolutePath()).into(mCameraSnapshot);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //endregion
            }
        });

        mTakePhotoButton.setEnabled(false);
    }
    //endregion

    private CropController mCropController;
    Camera mCamera;
    private CameraPreview mPreview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);
        setHelpText();
        setupCropControl(rootView);
        return rootView;
    }

    private void setHelpText() {
        if(photoReference == POLINOMIAL)
        {
            HelpTextView.setText("Escaneá un polinomio del estilo..");
            formula_one.postDelayed(new Runnable() {
                @Override
                public void run() {
                    formula_one.setVisibility(View.VISIBLE);
                }
            }, 1500);
        }
        else if(photoReference == EQUATION){
            HelpTextView.setText("Escaneá una ecuación del estilo..");
            formula_one.setText("\\begin{aligned}\\LARGE\\color{White}{ 3 + 4.x = 30 }\\end{aligned}");
            formula_one.postDelayed(new Runnable() {
                @Override
                public void run() {
                    formula_one.setVisibility(View.VISIBLE);
                }
            }, 1500);
        }
        else if(photoReference == FUNCTION){
            HelpTextView.setText("Escaneá una función del tipo..");
            Help2TextView.setText("Lineal - Cuadrática\n" +
                    "Constante - Homográfica");
            helpButton.setVisibility(View.VISIBLE);
            helpButton.setOnClickListener(button -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View popUpView = getActivity().getLayoutInflater().inflate(R.layout.help_function_pop_up, null);
                popUpView.setElevation(0f);
                popUpView.setClipToOutline(true);
                builder.setView(popUpView);
                AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                popUpView.findViewById(R.id.ok_pop_up_id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

            } );
        }
    }

    //region Camera Preview
    @Override
    public void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }

    private void startPreview() {
        mCropImageView.setImageBitmap(null);
        mCameraSnapshot.setImageBitmap(null);
        resetDragControl();

        if (mCamera != null) {
            try {
                mCamera.startPreview();
                return;
            } catch (Exception e) {
//                e.printStackTrace();
                //jump to below
            }
        }

        try {
            mCamera = CameraUtil.getCameraInstance();

            if (mCamera == null) {
//                showAlert("Can not connect to camera.");
            } else {
                mPreview = new CameraPreview(getContext(), mCamera);
                mCameraPreview.removeAllViews();
                mCameraPreview.addView(mPreview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mPreview.getHolder().removeCallback(mPreview);
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    private void imageCropped(final Bitmap bitmap) {
        //esto debe comentarse para hardcodear
        mCropImageView.setImageBitmap(bitmap);
        mCropStatusTextView.setText(R.string.processing_image);
        uploadImage(bitmap);
    }

    private void uploadImage(Bitmap bitmap) {
        startScanAnimation();


        UploadImageTask.UploadParams params = new UploadImageTask.UploadParams(bitmap);
        UploadImageTask task = new UploadImageTask(new UploadImageTask.ResultListener() {
            @Override
            public void onError(String message) {
                stopScanAnimation();
                showErrorAndReset(message);
                mTakePhotoButton.setEnabled(true);
            }

            @Override
            public void onSuccess(String latex) {
                stopScanAnimation();
                mLatestLatex = latex;
                Log.d("Latex_NEW", mLatestLatex);
                if (latex != "") {
                    switch (photoReference){
                    case EQUATION :
                        ExpressionsManager.setEquationPhoto(mLatestLatex, getActivity().getApplicationContext());
                        ExpressionsManager.setEquationDrawn(null);
                        if (ExpressionsManager.getTreeOfExpression() != null) {


                            Intent intent = new Intent(getActivity(), SolveEquationActivity.class);
                            startActivity(intent);
                        } else {
                            showErrorAndReset("Hubo un error al procesar la imagen, por favor, volvé a intentarlo.");
                        }
                        break;
                    case POLINOMIAL :
                        try {
                            if(correctExpression(latex)) {
                                SetPolinomialForPolinomialActivity(latex);
                                Intent intent = new Intent(getActivity(), SolvePolynomialActivity.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception ex){
                            showErrorAndReset("La expresión no es un polinomio, por favor, volvé a intentarlo.");
                        }
                        break;
                    case FUNCTION :
                        if (FunctionParserService.getFunctionType(latex+"=0") != INVALID) {
                            Intent intent = new Intent(getActivity(), EnterFunctionActivity.class);
                            intent.putExtra("function", latex);
                            startActivity(intent);
                        }
                        else{
                            showErrorAndReset("Ingresaste una función invalida.");
                        }
                    }
                }
                else{
                    showErrorAndReset("La imagen no se pudo procesar, por favor, volvé a intentarlo.");
                }
            }
        });
        task.execute(params);
    }

    private boolean correctExpression(String latex) {
        try
        {
            double isOnlyNumeric = Double.parseDouble(latex);
            return false;
        }
        catch(NumberFormatException nfe)
        {
            return true;
        }
    }

    public void SetPolinomialForPolinomialActivity(String latex) {
        Map<Integer, Double> polynomialMap = new HashMap<>();
        String polinomialPhoto = ExpressionsManager.containsFrac(latex);
        polinomialPhoto = polinomialPhoto.replaceAll("-","+!").replaceAll("\\s+","");
        String[] terms = polinomialPhoto.split("\\+");

        for (String term : terms) {
            if(!term.isEmpty()) {
                String potential, coefficient;
                if (term.contains("x^")) {
                    int position = term.indexOf("x^");
                    switch (position) {
                        case 1:
                            if (term.substring(0, position).contains("!")) {
                                coefficient = "-1";
                            } else {
                                coefficient = term.substring(0, position);
                            }
                            break;
                        case 0:
                            coefficient = "1";
                            break;
                        default:
                            coefficient = term.substring(0, position).replace("!", "-");
                            break;
                    }
                    potential = term.substring(position + 2, term.length());
                    coefficient = coefficient.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\)", "").replaceAll("\\(", "");
                    potential = potential.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\)", "").replaceAll("\\(", "");
                    polynomialMap = AddTerm(potential,coefficient, polynomialMap);
                } else if (term.contains("x")) {//es un coeficeinte lineal
                    int positionLineal = term.indexOf("x");
                    switch (positionLineal) {
                        case 1:
                            if (term.substring(0, positionLineal).contains("!")) {
                                coefficient = "-1";
                            } else {
                                coefficient = term.substring(0, positionLineal);
                            }
                            break;
                        case 0:
                            coefficient = "1";
                            break;
                        default:
                            coefficient = term.substring(0, positionLineal).replace("!", "-");
                            break;
                    }
                    polynomialMap = AddTerm("1",coefficient, polynomialMap);
                } else {//es un termino independiente
                    polynomialMap = AddTerm("0",term, polynomialMap);
                }
            }
        }
        EnterPolinomialActivity.polynomialTerms = polynomialMap;
    }

    private Map<Integer, Double> AddTerm(String potential, String coefficient, Map<Integer, Double> polynomialMap) {
        Double newCoefficient;
        if(!polynomialMap.containsKey(Integer.parseInt(potential))){
            newCoefficient = fractionToDouble(coefficient);
        }else {
            newCoefficient = polynomialMap.get(Integer.parseInt(potential)) + fractionToDouble(coefficient);
            polynomialMap.remove(potential);
        }
        if(newCoefficient != 0){
            polynomialMap.put(Integer.parseInt(potential), newCoefficient);
        }
        return polynomialMap;
    }

    public static Double fractionToDouble(String fraction) {
        Double d = null;
        if (fraction != null) {
            if (fraction.contains("/")) {
                String[] numbers = fraction.split("/");
                if (numbers.length == 2) {
                    Double d1 = Double.valueOf(numbers[0]);
                    Double d2 = Double.valueOf(numbers[1]);
                    d = Double.parseDouble(String.format("%.2f", d1/d2).replace(",","."));
                }
            }
            else {
                d = Double.parseDouble(fraction);
            }
        }
        if (d == null) {
        }
        return d;
    }

    private String mLatestLatex;

    private void showErrorAndReset(String errMessage) {
        Toast.makeText(getContext(), errMessage, Toast.LENGTH_LONG).show();
        startPreview();
        resetDragControl();
    }

    //region CropControl
    public void setupCropControl(View view) {
        mCropStatusTextView.setText(R.string.start_dragging_crop);
        mCropController = new CropController(mCropControl, new CropController.TouchStateListener() {
            @Override
            public void onDragBegan() {
                mCropStatusTextView.setText(R.string.release_to_take_photo);
                formula_one.setVisibility(View.GONE);
                HelpTextView.setVisibility(View.GONE);
                Help2TextView.setVisibility(View.GONE);
            }

            @Override
            public void onDragEnded() {
            }
        });
    }

    public void resetDragControl() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCropImageView.setImageBitmap(null);
                mCropStatusTextView.setText(R.string.start_dragging_crop);
                ViewGroup.LayoutParams layoutParams = mCropControl.getLayoutParams();
                layoutParams.width = (int) getResources().getDimension(R.dimen.crop_control_width);
                layoutParams.height = (int) getResources().getDimension(R.dimen.crop_control_height);
                mCropControl.setLayoutParams(layoutParams);
            }
        }, 500);

        mTakePhotoButton.setEnabled(true);

        stopScanAnimation();
    }

    private void startScanAnimation() {
        int inset = (int) getResources().getDimension(R.dimen.crop_corner_width_halved);
        Rect cropFrame = new Rect(mCropControl.getLeft(), mCropControl.getTop(), mCropControl.getRight(), mCropControl.getBottom());
        final TranslateAnimation animation = new TranslateAnimation(inset, cropFrame.width() - inset, 0, 0);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanLine.setVisibility(View.VISIBLE);
                mScanLine.startAnimation(animation);
            }
        }, 100);

    }

    private void stopScanAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanLine.setAnimation(null);
                mScanLine.setVisibility(View.GONE);
            }
        }, 500);
    }
    //endregion
}
