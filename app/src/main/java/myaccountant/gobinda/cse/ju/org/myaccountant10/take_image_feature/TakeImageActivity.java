package myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature;


import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TakeImageActivity extends Activity {

    private static Bitmap userSelectedImage;

    private FrameLayout frameLayout;
    private CameraPreview cameraPreview;
    private Button captureImageButton;

    private RelativeLayout overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_take_image);

        initializeVariables();
    }

    private void initializeVariables() {
        userSelectedImage = null;
        frameLayout = findViewById(R.id.TakeImageFeatureFrameLayoutForCameraPreview);
        overlay = findViewById(R.id.TakeImageFeatureRelativeLayoutOverlay);
        captureImageButton = findViewById(R.id.TakeImageFeatureButtonForCaptureImage);

        cameraPreview = new CameraPreview(TakeImageActivity.this);
        if(! cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(TakeImageActivity.this, "Camera not available!", Toast.LENGTH_SHORT).show();
            return;
        }
        frameLayout.addView(cameraPreview);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Get the preview size
        int previewWidth = cameraPreview.getMeasuredWidth();
        int previewHeight = cameraPreview.getMeasuredHeight();

        // Set the height of the overlay so that it makes the preview a square
        RelativeLayout.LayoutParams overlayParams = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
        overlayParams.height = previewHeight - previewWidth;
        overlay.setLayoutParams(overlayParams);
    }

    public void captureImageFromLiveCamera(View v){

        //first of all check camera available or not
        if(!cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(TakeImageActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }

        //if button shows capture then we will capture otherwise
        //we will just start camera preview
        if(!captureImageButton.getText().toString().equals(NameRelatedSupport.CAPTURE)){
            cameraPreview.startPreview();
            captureImageButton.setText(NameRelatedSupport.CAPTURE);

            //capturing again means forget previous selected image
            userSelectedImage = null;
            return;
        }

        try{
            cameraPreview.camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    userSelectedImage = ImageProcessingSupport.convertIntoBitmap(data);
                    userSelectedImage = ImageProcessingSupport.rotateImage(userSelectedImage, (CameraPreview.showingFrontCamera)? 270:90);
                    userSelectedImage = ImageProcessingSupport.cropSquareImage(userSelectedImage);
                    userSelectedImage = ImageProcessingSupport.getRoundedCornerBitmap(userSelectedImage);
                }

            });
        }catch(RuntimeException e){
            Toast.makeText(TakeImageActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
        }
        captureImageButton.setText(NameRelatedSupport.CAPTURE.concat(" Again"));
    }

    public void swapCameraDirection(View v){

        //checking camera available or not
        if(!cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(TakeImageActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }

        //check whether the phone has a front camera or not
        if(!cameraPreview.checkFrontCameraHasOrNot()){
            Toast.makeText(TakeImageActivity.this,"Front Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }
        try{
            //first stop & release the camera then swap direction
            cameraPreview.stopPreview();
            cameraPreview.releaseCamera();
            frameLayout.removeView(cameraPreview);
            cameraPreview.swapCameraDirection();
            cameraPreview = new CameraPreview(TakeImageActivity.this);
            frameLayout.addView(cameraPreview);

            //initialize capture button text
            captureImageButton.setText(NameRelatedSupport.CAPTURE);

            //swapping camera so forget previous selected image
            userSelectedImage = null;
        }catch (Exception e){
            Toast.makeText(TakeImageActivity.this,"Camera may not available!",Toast.LENGTH_LONG).show();
        }
    }

    public void okayWithCurrentImageNowGoBackToYourParentActivity(View v){
        //release camera first then change activity
        cameraPreview.stopPreview();
        cameraPreview.releaseCamera();

        if(userSelectedImage == null){
            Toast.makeText(TakeImageActivity.this,"You didn't Capture Any Image!",Toast.LENGTH_LONG).show();
        }

        finish();
    }

    public static Bitmap getUserSelectedImage(){
        Bitmap ret = userSelectedImage;
        userSelectedImage = null;
        return ret;
    }


    @Override
    public void onBackPressed() {
        if(userSelectedImage == null){
            Toast.makeText(TakeImageActivity.this,"You didn't Capture Any Image!",Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
