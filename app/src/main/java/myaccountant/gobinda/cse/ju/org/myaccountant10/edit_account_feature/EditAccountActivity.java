package myaccountant.gobinda.cse.ju.org.myaccountant10.edit_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature.AddAccountActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DbManager;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.TakeImageActivity;

public class EditAccountActivity extends AppCompatActivity {

    private static int currentAccountId = -1;

    private static String userTypedName;
    private static String userTypedMobileNumber;

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

        if(currentAccountId == NameRelatedSupport.NULL_ID) {
            try {
                String accountID = getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID);
                currentAccountId = Integer.parseInt(accountID);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(EditAccountActivity.this,"Account ID invalid!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Account currentAccount = DbManager.getAccountTableAccess().getAccountAccordingToID(currentAccountId);
        textViewForName.setText(currentAccount.getAccountName());
        textViewForMobile.setText(currentAccount.getAccountMobileNumber());

        if(userTypedName != null){
            textViewForName.setText(userTypedName);
        }

        if(userTypedMobileNumber != null){
            textViewForMobile.setText(userTypedMobileNumber);
        }

        Bitmap userSelectedNewImage = TakeImageActivity.getUserSelectedImage();
        if(userSelectedNewImage == null){
            byte[] imageData = currentAccount.getAccountImage();
            imageViewForImage.setImageBitmap(ImageProcessingSupport.convertIntoBitmap(imageData));
        }else{
            imageViewForImage.setImageBitmap(userSelectedNewImage);
        }

        Log.d("1Debug","in the on create method!");
        if(userTypedName != null)
            Log.d("1Debug_name",userTypedName);
        if(userTypedMobileNumber != null)
            Log.d("1Debug_mobile",userTypedMobileNumber);

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
        if(accName.length() == 0){
            Toast.makeText(EditAccountActivity.this,"Name Can't be EMPTY!",Toast.LENGTH_SHORT).show();
            return;
        }
        String accMobile = textViewForMobile.getText().toString();

        Bitmap bitmapImageFromImageView = ((BitmapDrawable) imageViewForImage.getDrawable()).getBitmap();
        bitmapImageFromImageView = ImageProcessingSupport.getRoundedCornerBitmap(bitmapImageFromImageView);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImageFromImageView.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] byteArray =  bos.toByteArray();


        Account account = new Account(currentAccountId,accName,accMobile,byteArray);
        if(DbManager.getAccountTableAccess().updateAccount(account)){
            Toast.makeText(EditAccountActivity.this, "Account Updated!", Toast.LENGTH_SHORT).show();

            //remove saved variable first then go back
            removeSavedVariable();

            //account saved so go back to the account list view
            Intent intent = new Intent(EditAccountActivity.this, ShowAccountListActivity.class);
            intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.EDIT_ACCOUNT_ACTIVITY);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(EditAccountActivity.this, "Account can't be Updated!", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeImage(View v){
        //first save the user edited information
        userTypedName = textViewForName.getText().toString();
        userTypedMobileNumber = textViewForMobile.getText().toString();
        Log.d("Debug","saved okay");
        Log.d("Debug_name",userTypedName);
        Log.d("Debug_mobile",userTypedMobileNumber);


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
