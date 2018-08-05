package myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.MyScreenSize;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public SurfaceHolder surfaceHolder;
    public Camera camera;

    public static boolean showingFrontCamera;

    public CameraPreview(Context context) {
        super(context);
        if(checkCameraAvailableOrNot()) {
            initializeCamera();
        }

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void swapCameraDirection(){
        if(checkFrontCameraHasOrNot()) {
            showingFrontCamera = !showingFrontCamera;
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(checkCameraAvailableOrNot()){
            setPreviewDisplay(holder);
            startPreview();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null || !checkCameraAvailableOrNot()){
            return;
        }
        stopPreview();
        setPreviewDisplay(holder);
        startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //stopPreview();
        //releaseCamera();
    }

    public void initializeCamera(){
        try {
            if(showingFrontCamera) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }else {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }

            Camera.Parameters camParams = camera.getParameters();

            Camera.Size previewSize = camParams.getSupportedPreviewSizes().get(0);
            int IMAGE_SIZE = Math.min(MyScreenSize.SCREEN_WIDTH, MyScreenSize.SCREEN_HEIGHT);

            Log.d("debug", String.format("image size len = %d",IMAGE_SIZE));
            for (Camera.Size size : camParams.getSupportedPreviewSizes()) {
                if (size.width >= IMAGE_SIZE && size.height >= IMAGE_SIZE) {
                    previewSize = size;
                    break;
                }
            }
            camParams.setPreviewSize(previewSize.width, previewSize.height);

            // Try to find the closest picture size to match the preview size.
            Camera.Size pictureSize = camParams.getSupportedPictureSizes().get(0);
            for (Camera.Size size : camParams.getSupportedPictureSizes()) {
                if (size.width == previewSize.width && size.height == previewSize.height) {
                    pictureSize = size;
                    break;
                }
            }
            camParams.setPictureSize(pictureSize.width, pictureSize.height);
            MyScreenSize.initializeCameraPictureSize(pictureSize.width,pictureSize.height);

            camera.setParameters(camParams);

            camera.setDisplayOrientation(90);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void releaseCamera(){
        try{
            camera.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder){
        try{
            camera.setPreviewDisplay(surfaceHolder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startPreview(){
        try{
            camera.startPreview();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreview(){
        try{
            camera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkCameraAvailableOrNot() {
        return this.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public boolean checkFrontCameraHasOrNot() {
        return this.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

}