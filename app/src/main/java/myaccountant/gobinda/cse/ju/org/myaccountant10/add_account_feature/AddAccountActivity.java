package myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.MyImageProcessing;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DbManager;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;

public class AddAccountActivity extends AppCompatActivity {

    private static byte[] userSelectedImage;
    private static String userTypedUserName;
    private static String userTypedMobileNumber;

    private EditText enterName;
    private EditText enterMobileNumber;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);

        enterName = findViewById(R.id.enterAccountNameEditText);
        enterMobileNumber = findViewById(R.id.enterAccountMobileNumberEditText);
        imageView = findViewById(R.id.enterAccountImage);

        if(userTypedUserName!= null && userTypedUserName.length()!=0){
            enterName.setText(userTypedUserName);
        }

        if(userTypedMobileNumber!= null && userTypedMobileNumber.length()!=0){
            enterMobileNumber.setText(userTypedMobileNumber);
        }

        if(userSelectedImage == null){
            imageView.setImageResource(R.drawable.b);
            return;
        }

        Bitmap selectedImage = MyImageProcessing.convertIntoBitmap(userSelectedImage);
        selectedImage = MyImageProcessing.rotateImage(selectedImage, (CameraPreview.showingFrontCamera)? 270:90);
        selectedImage = MyImageProcessing.cropSquareImage(selectedImage);
        selectedImage = MyImageProcessing.getRoundedCornerBitmap(selectedImage);
        imageView.setImageBitmap(selectedImage);
    }

    public void saveAccountButtonPressed(View v){
        try{

            String name = enterName.getText().toString().trim();
            if(name.length() == 0){
                Toast.makeText(AddAccountActivity.this,"Name Can't be EMPTY!",Toast.LENGTH_SHORT).show();
                return;
            }
            String mobile = enterMobileNumber.getText().toString().trim();

            Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            bitmapImageFromImageView = MyImageProcessing.getRoundedCornerBitmap(bitmapImageFromImageView);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapImageFromImageView.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] byteArray =  bos.toByteArray();

            Account account = new Account();
            account.setAccountName(name);
            account.setAccountMobileNumber(mobile);
            account.setAccountImage(byteArray);

            if(DbManager.getAccountTableAccess().insertAccount(account)){
                Toast.makeText(AddAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

                //account saved so go back to the account list view
                Intent intent = new Intent(AddAccountActivity.this, ShowAccountListActivity.class);
                startActivity(intent);
                finish();

                //remove user typed or selected variables
                initializeUserVariables();
            } else {
                Toast.makeText(AddAccountActivity.this, "Account can't be created!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openTakeImageActivity(View v){
        //remove previous selected image
        userSelectedImage = null;

        //save currently typed username and mobile number
        userTypedUserName = enterName.getText().toString();
        userTypedMobileNumber = enterMobileNumber.getText().toString();

        Intent intent = new Intent(AddAccountActivity.this, TakeImageActivity.class);
        startActivity(intent);
    }

    public static  void setUserSelectedImage(byte[] imageArray){
        userSelectedImage = imageArray;
    }


    public static void initializeUserVariables(){
        userSelectedImage = null;
        userTypedUserName = null;
        userTypedMobileNumber = null;
    }
}
