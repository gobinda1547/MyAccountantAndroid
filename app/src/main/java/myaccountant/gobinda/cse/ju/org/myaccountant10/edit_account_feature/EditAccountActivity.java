package myaccountant.gobinda.cse.ju.org.myaccountant10.edit_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.TakeImageActivity;

public class EditAccountActivity extends AppCompatActivity {

    private Account currentAccount;

    private EditText editTextForName;
    private EditText editTextForMobile;
    private ImageView imageViewForImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_account);
        setTitle(R.string.titleForEditAccountActivity);

        initializeVariables();
        displayScreen();
    }

    private void displayScreen() {

        Bitmap userSelectedNewImage = TakeImageActivity.getUserSelectedImage();
        if(userSelectedNewImage != null){
            imageViewForImage.setImageBitmap(userSelectedNewImage);
        }else{
            imageViewForImage.setImageBitmap(InternalMemorySupport.getImageFileFromThisLocation(currentAccount.getAccountImageLocation()));
        }
    }

    private void initializeVariables() {
        imageViewForImage = findViewById(R.id.EditAccountFeatureImageViewForEditingAccountImage);
        editTextForName = findViewById(R.id.EditAccountFeatureEditTextForEditingAccountName);
        editTextForMobile = findViewById(R.id.EditAccountFeatureEditTextForEditingAccountMobileNumber);

        int currentAccountId = Integer.parseInt(getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID));
        currentAccount = DatabaseHelper.getInstance(this).getAccountAccordingToID(currentAccountId);

        editTextForName.setText(currentAccount.getAccountName());
        editTextForMobile.setText(currentAccount.getAccountMobileNumber());
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScreen();
    }

    public void updateAccountInformation(View v){

        int accID = currentAccount.getAccountId();
        String accName = editTextForName.getText().toString();
        String accMobile = editTextForMobile.getText().toString();
        Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageViewForImage.getDrawable()).getBitmap();

        if(accName.length() == 0){
            Toast.makeText(EditAccountActivity.this,"Name Can't be EMPTY!",Toast.LENGTH_SHORT).show();
            return;
        }

        String imageFileName = InternalMemorySupport.writeImageFileInInternalMemory(this,bitmapImageFromImageView);
        if(imageFileName == null){
            Toast.makeText(EditAccountActivity.this,"Can not create image file into memory!",Toast.LENGTH_SHORT).show();
            return;
        }

        Account account = new Account(accID,accName,accMobile,imageFileName);
        if(DatabaseHelper.getInstance(this).updateAccount(account)){
            Toast.makeText(EditAccountActivity.this, "Account Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            InternalMemorySupport.deleteImageFileFromInternalMemory(imageFileName);
            Toast.makeText(EditAccountActivity.this, "Account can't be Updated!", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeImage(View v){
        //go to image activity to take an image
        Intent intent = new Intent(EditAccountActivity.this, TakeImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
