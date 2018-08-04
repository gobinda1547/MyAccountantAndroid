package myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.MyImageProcessing;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.MyScreenSize;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DbManager;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;

public class AddAccountActivity extends AppCompatActivity {

    private EditText enterName;
    private EditText enterMobileNumber;
    private EditText enterAddress;
    private ImageView imageView;
    private FrameLayout frameLayout;

    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);

        enterName = findViewById(R.id.enterAccountNameEditText);
        enterMobileNumber = findViewById(R.id.enterAccountMobileNumberEditText);
        enterAddress = findViewById(R.id.enterAccountAddressEditText);
        frameLayout = findViewById(R.id.camera_preview);
        imageView = findViewById(R.id.enterAccountImage);
        imageView.setImageResource(R.drawable.b);

        //frameLayout.setMinimumHeight(MyScreenSize.getWidth()/2);

        cameraPreview = new CameraPreview(AddAccountActivity.this);
        if(! cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(AddAccountActivity.this, "Camera not available!", Toast.LENGTH_SHORT).show();
            return;
        }
        frameLayout.addView(cameraPreview);
    }

    public void saveAccountButtonPressed(View v){
        try{

            String name = enterName.getText().toString().trim();
            if(name.length() < 3){
                Toast.makeText(AddAccountActivity.this,"Minimum Name Length 3",Toast.LENGTH_SHORT).show();
                return;
            }
            String mobile = enterMobileNumber.getText().toString().trim();
            String address = enterAddress.getText().toString().trim();

            Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapImageFromImageView.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] byteArray =  bos.toByteArray();

            Account account = new Account();
            account.setAccountName(name);
            account.setAccountMobileNumber(mobile);
            account.setAccountBalance(0);
            account.setAccountAddress(address);
            account.setAccountImage(byteArray);

            if(DbManager.getAccountTableAccess().insertAccount(account)){
                Toast.makeText(AddAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddAccountActivity.this, ShowAccountListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AddAccountActivity.this, "Account can't be created!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void takePictureFromLiveCamera(View v){
        if(!cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(AddAccountActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }
        try{
            cameraPreview.camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap picture = MyImageProcessing.processImage(data);
                    //Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (picture != null){
                        int rotation = (CameraPreview.showingFrontCamera)? 270:90;
                        picture = MyImageProcessing.rotateImage(picture,rotation);
                        //picture = MyImageProcessing.getRoundedCornerBitmap(picture);

                        imageView.setImageBitmap(picture);
                    }
                    cameraPreview.startPreview();
                }

            });
        }catch(RuntimeException e){
            Toast.makeText(AddAccountActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
        }
    }


    public void swapCameraDirection(View v){
        if(!cameraPreview.checkCameraAvailableOrNot()){
            Toast.makeText(AddAccountActivity.this,"Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }
        if(!cameraPreview.checkFrontCameraHasOrNot()){
            Toast.makeText(AddAccountActivity.this,"Front Camera not available!",Toast.LENGTH_LONG).show();
            return;
        }
        try{
            cameraPreview.stopPreview();
            cameraPreview.releaseCamera();
            frameLayout.removeView(cameraPreview);
            cameraPreview.swapCameraDirection();
            cameraPreview = new CameraPreview(AddAccountActivity.this);
            frameLayout.addView(cameraPreview);
        }catch (Exception e){
            Toast.makeText(AddAccountActivity.this,"Camera may not available!",Toast.LENGTH_LONG).show();
        }
    }

}
