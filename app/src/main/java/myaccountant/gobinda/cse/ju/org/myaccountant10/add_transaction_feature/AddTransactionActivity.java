package myaccountant.gobinda.cse.ju.org.myaccountant10.add_transaction_feature;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Transaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    private int accountID;
    private EditText editText;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_transaction);
        setTitle(R.string.titleForAddTransactionActivity);

        initializeVariables();
    }


    private void initializeVariables() {
        accountID = Integer.parseInt(getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID));
        editText = findViewById(R.id.AddTransactionFeatureEditTextForAddingTransactionAmount);
        radioGroup = findViewById(R.id.AddTransactionFeatureRadioGroup);
    }

    public void saveThisTransaction(View view){
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(AddTransactionActivity.this, "Please Select JOMa/Khoroch", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean optionOneSelected = (radioGroup.getCheckedRadioButtonId() == R.id.AddTransactionFeatureRadioButton1);
        Log.d("RadioButton: ", String.valueOf(optionOneSelected));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM 'at' hh:mm aaa", Locale.ENGLISH);
        String transactionDateTime = sdf.format(new Date());

        if(editText.getText().toString().length() == 0){
            Toast.makeText(AddTransactionActivity.this, "Amount can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        int transactionAmount = Integer.parseInt(editText.getText().toString());
        transactionAmount = (optionOneSelected)? transactionAmount : -transactionAmount;

        Transaction transaction = new Transaction(accountID,transactionAmount,transactionDateTime);
        if(DatabaseHelper.getInstance(this).insertTransaction(transaction)){
            Toast.makeText(AddTransactionActivity.this, "Transaction Added!", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(AddTransactionActivity.this, "Transaction Adding error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
