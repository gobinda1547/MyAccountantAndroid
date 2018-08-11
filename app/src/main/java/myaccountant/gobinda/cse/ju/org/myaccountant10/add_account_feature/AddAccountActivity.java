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

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.TakeImageActivity;

public class AddAccountActivity extends AppCompatActivity {

    private EditText enterName;
    private EditText enterMobileNumber;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);
        setTitle(R.string.titleForAddAccountActivity);

        initializeVariables();
        displayScreen();
    }

    private void initializeVariables() {
        enterName = findViewById(R.id.AddAccountFeatureEditTextForAddingAccountName);
        enterMobileNumber = findViewById(R.id.AddAccountFeatureEditTextForAddingAccountMobile);
        imageView = findViewById(R.id.AddAccountFeatureImageViewForAddingAccountImage);
    }

    public void displayScreen(){
        Bitmap userSelectedImage = TakeImageActivity.getUserSelectedImage();
        if(userSelectedImage == null){
            userSelectedImage = ImageProcessingSupport.getDefaultAccountImage(this);
        }
        imageView.setImageBitmap(userSelectedImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScreen();
    }

    public void saveAccountButtonPressed(View v){
        try{
            String name = enterName.getText().toString().trim();
            String mobile = enterMobileNumber.getText().toString().trim();
            Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            if(name.length() == 0){
                Toast.makeText(AddAccountActivity.this,"Name Can't be EMPTY!",Toast.LENGTH_SHORT).show();
                return;
            }

            String imageFileName = InternalMemorySupport.writeImageFileInInternalMemory(this,bitmapImageFromImageView);
            if(imageFileName == null){
                Toast.makeText(AddAccountActivity.this,"Can not write image file into memory!",Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = new Account(name,mobile,imageFileName);
            if(DatabaseHelper.getInstance(this).insertAccount(account)){
                Toast.makeText(AddAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

                //go back to parent activity
                finish();
            } else {
                InternalMemorySupport.deleteImageFileFromInternalMemory(imageFileName);
                Toast.makeText(AddAccountActivity.this, "Account can't be created!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goToTakeImageActivity(View v){
        Intent intent = new Intent(AddAccountActivity.this, TakeImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
