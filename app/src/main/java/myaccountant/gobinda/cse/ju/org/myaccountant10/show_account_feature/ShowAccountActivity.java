package myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DbManager;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature.ShowAccountListActivity;

public class ShowAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_account);

        ImageView imageViewForImage = findViewById(R.id.idForShowingAccountImage);
        TextView textViewForName = findViewById(R.id.idForShowingAccountName);
        TextView textViewForMobile = findViewById(R.id.idForShowingAccountMobileNumber);
        TextView textViewForAddress = findViewById(R.id.idForShowingAccountAddress);
        TextView textViewForBalance = findViewById(R.id.idForShowingAccountBalance);
        TextView textViewForStatus = findViewById(R.id.idForShowingAccountStatus);

        Account currentAccount;
        try{
            String accountID = getIntent().getStringExtra("ACCOUNT_ID");
            int accID = Integer.parseInt(accountID);
            currentAccount = DbManager.getAccountTableAccess().getAccountAccordingToID(accID);
            if(currentAccount == null){
                Toast.makeText(ShowAccountActivity.this,"Account ID invalid1!",Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            Toast.makeText(ShowAccountActivity.this,"Account ID invalid2!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        //int gAccountId = currentAccount.getAccountId();
        String gAccountName = currentAccount.getAccountName();
        String gAccountMobileNumber = currentAccount.getAccountMobileNumber();
        long gAccountBalance = currentAccount.getAccountBalance();
        String gAccountAddress = currentAccount.getAccountAddress();

        textViewForName.setText(gAccountName);
        textViewForMobile.setText((gAccountMobileNumber == null || gAccountMobileNumber.length() == 0)? "Number Not Found": gAccountMobileNumber);
        textViewForAddress.setText((gAccountAddress == null || gAccountAddress.length() == 0)? "Address Not Found" : gAccountAddress);
        textViewForBalance.setText(String.valueOf(gAccountBalance));
        textViewForStatus.setText((gAccountBalance >= 0)? "জমা আছে":"খরচ হয়েছে");

        byte[] imageData = currentAccount.getAccountImage();
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageData,0,imageData.length);
        imageViewForImage.setImageBitmap(bitmapImage);
    }


    public void goBackToHomeActivity(View v){
        Intent intent = new Intent(ShowAccountActivity.this, ShowAccountListActivity.class);
        startActivity(intent);
        finish();
    }
}
