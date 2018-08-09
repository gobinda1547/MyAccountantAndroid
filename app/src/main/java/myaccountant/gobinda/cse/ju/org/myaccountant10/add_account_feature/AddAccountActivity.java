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

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.take_image_feature.TakeImageActivity;

public class AddAccountActivity extends AppCompatActivity {

    private static String userTypedUserName;
    private static String userTypedMobileNumber;

    private EditText enterName;
    private EditText enterMobileNumber;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);

        enterName = findViewById(R.id.addAccountEditTextAccountName);
        enterMobileNumber = findViewById(R.id.addAccountEditTextAccountMobileNumber);
        imageView = findViewById(R.id.addAccountImageViewAccountImage);

        if(userTypedUserName!= null && userTypedUserName.length()!=0){
            enterName.setText(userTypedUserName);
        }

        if(userTypedMobileNumber!= null && userTypedMobileNumber.length()!=0){
            enterMobileNumber.setText(userTypedMobileNumber);
        }

        Bitmap userSelectedImage = TakeImageActivity.getUserSelectedImage();
        if(userSelectedImage == null){
            imageView.setImageResource(R.drawable.b);
        }else {
            imageView.setImageBitmap(userSelectedImage);
        }
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

            String imageFileName = InternalMemorySupport.writeImageFileInInternalMemory(bitmapImageFromImageView);
            if(imageFileName == null){
                Toast.makeText(AddAccountActivity.this,"Can not write image file into memory!",Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = new Account(name,mobile,imageFileName);
            if(DatabaseHelper.getInstance(this).insertAccount(account)){
                Toast.makeText(AddAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

                //remove saved variable first then go back
                removeSavedVariable();

                //account saved so go back to the account list view
                Intent intent = new Intent(AddAccountActivity.this, ShowAccountListActivity.class);
                intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.ADD_ACCOUNT_ACTIVITY);
                startActivity(intent);
                finish();
            } else {
                InternalMemorySupport.deleteImageFileFromInternalMemory(imageFileName);
                Toast.makeText(AddAccountActivity.this, "Account can't be created!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openTakeImageActivity(View v){

        //save currently typed username and mobile number
        userTypedUserName = enterName.getText().toString();
        userTypedMobileNumber = enterMobileNumber.getText().toString();

        Intent intent = new Intent(AddAccountActivity.this, TakeImageActivity.class);
        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.ADD_ACCOUNT_ACTIVITY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //remove saved variable first then go back
        removeSavedVariable();

        //go back to home
        Intent intent = new Intent(AddAccountActivity.this, ShowAccountListActivity.class);
        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.ADD_ACCOUNT_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void removeSavedVariable(){
        userTypedUserName = null;
        userTypedMobileNumber = null;
    }
}
