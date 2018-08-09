package myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_transaction_feature;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.add_transaction_feature.AddTransactionActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Transaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowAccountTransactionActivity extends AppCompatActivity {

    private static int currentAccountID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_account_transaction);

        TextView accountBalanceTextView = findViewById(R.id.showTransactionAccountBalanceTextView);
        TextView accountTypeTextView = findViewById(R.id.showTransactionAccountTypeTextView);
        ImageView accountImageImageView = findViewById(R.id.showTransactionAccountImageImageView);


        String idFromPreviousActivity = getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID);
        if(idFromPreviousActivity != null && idFromPreviousActivity.length() != 0){
            currentAccountID = Integer.parseInt(idFromPreviousActivity);
        }

        Account currentAccount = DatabaseHelper.getInstance(this).getAccountAccordingToID(currentAccountID);


        RecyclerView recyclerView = findViewById(R.id.idForAccountTransactionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Transaction> transactions = DatabaseHelper.getInstance(this).getAllTheTransactionForTheAccountID(currentAccountID);
        ShowAccountTransactionActivity.MyRecyclerViewAdapter adapter = new ShowAccountTransactionActivity.MyRecyclerViewAdapter(transactions);
        recyclerView.setAdapter(adapter);

        int balance = 0;
        for(Transaction transaction : transactions){
            balance += transaction.getTransactionBalance();
        }

        //reading file and showing
        Bitmap image = InternalMemorySupport.getImageFileFromThisLocation(currentAccount.getAccountImageLocation());
        if(image == null){
            accountImageImageView.setImageResource(R.drawable.b);
        }else{
            accountImageImageView.setImageBitmap(image);
        }

        accountBalanceTextView.setText((balance<0)? String.valueOf(balance*-1):String.valueOf(balance));
        accountBalanceTextView.setTextColor((balance<0)? Color.RED : Color.BLACK);
        accountTypeTextView.setText((balance<0)? "BAKI ACHE":"JOMA ACHE");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_account_transaction_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addTransactionMenuItem){
            Intent intent = new Intent(ShowAccountTransactionActivity.this, AddTransactionActivity.class);
            intent.putExtra(NameRelatedSupport.ACCOUNT_ID, String.valueOf(currentAccountID));
            intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.SHOW_ACCOUNT_TRANSACTION_ACTIVITY);
            startActivity(intent);
            finish();
        }
        return true;
    }




    public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<ShowAccountTransactionActivity.MyRecyclerViewAdapter.TransactionViewHolder>{

        private List<Transaction> transactions;

        private MyRecyclerViewAdapter(List<Transaction> transactions){

            Collections.sort(transactions, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction t1, Transaction t2) {
                    return t1.getTransactionDate().compareTo(t2.getTransactionDate());
                }
            });
            this.transactions = transactions;
            Log.d("print", String.valueOf(transactions.size()));
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }

        @Override
        public ShowAccountTransactionActivity.MyRecyclerViewAdapter.TransactionViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transaction_list_per_item, viewGroup, false);
            return new ShowAccountTransactionActivity.MyRecyclerViewAdapter.TransactionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ShowAccountTransactionActivity.MyRecyclerViewAdapter.TransactionViewHolder transactionViewHolder, int i) {
            transactionViewHolder.showThisTransaction(transactions.get(i));
        }

        class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private Transaction transaction;

            private TextView transactionDateTextView;
            private TextView transactionTypeTextView;
            private TextView transactionAmountTextView;

            private TransactionViewHolder(View itemView) {
                super(itemView);

                transactionDateTextView = itemView.findViewById(R.id.idForTransactionDateTime);
                transactionTypeTextView = itemView.findViewById(R.id.idForTransactionType);
                transactionAmountTextView = itemView.findViewById(R.id.idForTransactionAmount);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(ShowAccountTransactionActivity.this,String.valueOf(transaction.getTransactionBalance()),Toast.LENGTH_LONG).show();
            }

            private void showThisTransaction(Transaction transaction){
                this.transaction = transaction;
                Log.d("dbe", transaction.getTransactionDate());
                transactionDateTextView.setText(transaction.getTransactionDate());
                transactionTypeTextView.setText( (transaction.getTransactionBalance()>=0)? "JOMA":"KHOROCH");
                transactionAmountTextView.setText(String.valueOf(transaction.getTransactionBalance()));
            }
        }

    }
}
