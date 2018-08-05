package myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature;


import myaccountant.gobinda.cse.ju.org.myaccountant10.R;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TakeImageActivity extends Activity {

    private static String captureString = "Capture";

    private byte[] currentSelectedImage;
    private FrameLayout frameLayout;
    private CameraPreview cameraPreview;
    private Button captureImageButton;

    private RelativeLayout overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_take_image);

        //extract all the elements
        frameLayout = findViewById(R.id.camera_preview);
        overlay = findViewById(R.id.overlay);
        captureImageButton = findViewById(R.id.captureImageButton);

        //initialize camera preview
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
        if(!captureImageButton.getText().toString().equals(captureString)){
            cameraPreview.startPreview();
            captureImageButton.setText(captureString);
            return;
        }

        try{
            cameraPreview.camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    currentSelectedImage = data;
                }

            });
        }catch(RuntimeException e){
            Toast.makeText(TakeImageActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
        }
        captureImageButton.setText(captureString.concat(" Again"));
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
            captureImageButton.setText(captureString);
        }catch (Exception e){
            Toast.makeText(TakeImageActivity.this,"Camera may not available!",Toast.LENGTH_LONG).show();
        }
    }

    public void okayWithCurrentImage(View v){
        //release camera first then change activity
        cameraPreview.stopPreview();
        cameraPreview.releaseCamera();

        if(currentSelectedImage == null){
            Toast.makeText(TakeImageActivity.this,"You didn't Capture Any Image!",Toast.LENGTH_LONG).show();
        }else{
            //supplying the selected image to add account activity
            AddAccountActivity.setUserSelectedImage(currentSelectedImage);
        }

        //going back to add account activity with selected image
        Intent intent = new Intent(TakeImageActivity.this, AddAccountActivity.class);
        startActivity(intent);
        finish();
    }

}
