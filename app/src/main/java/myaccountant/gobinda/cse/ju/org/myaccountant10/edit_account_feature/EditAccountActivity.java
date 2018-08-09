package myaccountant.gobinda.cse.ju.org.myaccountant10.edit_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.TakeImageActivity;

public class EditAccountActivity extends AppCompatActivity {

    private static int currentAccountId = -1;

    private static String userTypedName;
    private static String userTypedMobileNumber;
    private static Bitmap userSelectedImage;

    private TextView textViewForName;
    private TextView textViewForMobile;
    private ImageView imageViewForImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_account);

        imageViewForImage = findViewById(R.id.idForShowingAccountImage);
        textViewForName = findViewById(R.id.idForShowingAccountName);
        textViewForMobile = findViewById(R.id.idForShowingAccountMobileNumber);

        String accIDfromParentActivity = getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID);
        if(accIDfromParentActivity == null || accIDfromParentActivity.length() == 0){
            textViewForName.setText(userTypedName);
            textViewForMobile.setText(userTypedMobileNumber);

            Bitmap userSelectedNewImage = TakeImageActivity.getUserSelectedImage();
            if(userSelectedNewImage != null){
                imageViewForImage.setImageBitmap(userSelectedNewImage);
            }else{
                imageViewForImage.setImageBitmap(userSelectedImage);
            }
            return;
        }

        //parsing current account id
        currentAccountId = Integer.parseInt(accIDfromParentActivity);

        //getting account object from database
        Account currentAccount = DatabaseHelper.getInstance(this).getAccountAccordingToID(currentAccountId);

        userTypedName = currentAccount.getAccountName();
        userTypedMobileNumber = currentAccount.getAccountMobileNumber();
        userSelectedImage = InternalMemorySupport.getImageFileFromThisLocation(currentAccount.getAccountImageLocation());
        if(userSelectedImage == null){
            userSelectedImage = ImageProcessingSupport.getDefaultAccountImage(this);
        }

        textViewForName.setText(userTypedName);
        textViewForMobile.setText(userTypedMobileNumber);
        imageViewForImage.setImageBitmap(userSelectedImage);
    }


    public void goBackToHomeActivity(View v){
        //remove saved variable first then go back
        removeSavedVariable();

        //go back to home
        Intent intent = new Intent(EditAccountActivity.this, ShowAccountListActivity.class);
        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.EDIT_ACCOUNT_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void updateAccountInformation(View v){

        String accName = textViewForName.getText().toString();
        String accMobile = textViewForMobile.getText().toString();
        Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageViewForImage.getDrawable()).getBitmap();

        if(accName.length() == 0){
            Toast.makeText(EditAccountActivity.this,"Name Can't be EMPTY!",Toast.LENGTH_SHORT).show();
            return;
        }

        String imageFileName = InternalMemorySupport.writeImageFileInInternalMemory(bitmapImageFromImageView);
        if(imageFileName == null){
            Toast.makeText(EditAccountActivity.this,"Can not create image file into memory!",Toast.LENGTH_SHORT).show();
            return;
        }

        Account account = new Account(currentAccountId,accName,accMobile,imageFileName);
        if(DatabaseHelper.getInstance(this).updateAccount(account)){
            Toast.makeText(EditAccountActivity.this, "Account Updated!", Toast.LENGTH_SHORT).show();

            //remove saved variable first then go back
            removeSavedVariable();

            //account saved so go back to the account list view
            Intent intent = new Intent(EditAccountActivity.this, ShowAccountListActivity.class);
            intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.EDIT_ACCOUNT_ACTIVITY);
            startActivity(intent);
            finish();
        } else {
            InternalMemorySupport.deleteImageFileFromInternalMemory(imageFileName);
            Toast.makeText(EditAccountActivity.this, "Account can't be Updated!", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeImage(View v){
        //first save the user edited information
        userTypedName = textViewForName.getText().toString();
        userTypedMobileNumber = textViewForMobile.getText().toString();

        //then go to image activity to take an image
        Intent intent = new Intent(EditAccountActivity.this, TakeImageActivity.class);
        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.EDIT_ACCOUNT_ACTIVITY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //remove saved variable first then go back
        removeSavedVariable();

        //go back to home
        Intent intent = new Intent(EditAccountActivity.this, ShowAccountListActivity.class);
        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.EDIT_ACCOUNT_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void removeSavedVariable(){
        currentAccountId = -1;
        userTypedName  = null;
        userTypedMobileNumber = null;
    }
}
