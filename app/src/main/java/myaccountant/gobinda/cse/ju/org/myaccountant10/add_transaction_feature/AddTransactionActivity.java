package myaccountant.gobinda.cse.ju.org.myaccountant10.add_transaction_feature;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Transaction;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_transaction_feature.ShowAccountTransactionActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity {

    private int accountID;
    private ToggleButton toggleButton;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_transaction);

        accountID = Integer.parseInt(getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID));

        toggleButton = findViewById(R.id.addTransactionToggleButtonForJomaKhoroch);
        toggleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if ( toggleButton.getText().toString().equalsIgnoreCase("JOMA")) {
                    toggleButton.setTextOff("TOGGLE ON");
                    toggleButton.setChecked(true);
                } else if ( toggleButton.getText().toString().equalsIgnoreCase("KHOROCH")) {
                    toggleButton.setTextOn("TOGGLE OFF");
                    toggleButton.setChecked(false);
                }
            }
        });
        editText = findViewById(R.id.addTransactionEditTextForAmount);

    }

    public void saveThisTransaction(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        boolean jomaKorece = toggleButton.isChecked();
        if(editText.getText().toString().length() == 0){
            Toast.makeText(AddTransactionActivity.this, "Amount can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        int ammount = Integer.parseInt(editText.getText().toString());
        ammount = (jomaKorece)? ammount:-ammount;
        Transaction transaction = new Transaction(accountID,ammount,sdf.format(new Date()));
        if(DatabaseHelper.getInstance(this).insertTransaction(transaction)){
            Toast.makeText(AddTransactionActivity.this, "Transaction Added!", Toast.LENGTH_SHORT).show();

            //account saved so go back to the account list view
            Intent intent = new Intent(AddTransactionActivity.this, ShowAccountTransactionActivity.class);
            intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.ADD_ACCOUNT_TRANSACTION_ACTIVITY);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(AddTransactionActivity.this, "Transaction Adding error!", Toast.LENGTH_SHORT).show();
        }
    }
}
