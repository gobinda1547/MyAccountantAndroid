package myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_transaction_feature;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.add_transaction_feature.AddTransactionActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

    private Account currentAccount;

    private TextView textViewForAccountBalance;
    private TextView textViewForAccountStatus;
    private ImageView imageViewForAccountImage;

    private TextView accountOptions;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_account_transaction);
        //setTitle(R.string.titleForShowAccountTransactionActivity);

        initializeVariables();
        displayScreen();
    }

    public void displayScreen(){

        setTitle(currentAccount.getAccountName());

        List<Transaction> transactions = DatabaseHelper.getInstance(this).getAllTheTransactionForTheAccountID(currentAccount.getAccountId());
        ShowAccountTransactionActivity.MyRecyclerViewAdapter adapter = new ShowAccountTransactionActivity.MyRecyclerViewAdapter(transactions);
        recyclerView.setAdapter(adapter);

        Bitmap image = InternalMemorySupport.getImageFileFromThisLocation(currentAccount.getAccountImageLocation());
        if(image == null){
            image = ImageProcessingSupport.getDefaultAccountImage(this);
        }
        imageViewForAccountImage.setImageBitmap(image);

        int balance = 0;
        for(Transaction transaction : transactions){
            balance += transaction.getTransactionBalance();
        }

        if(balance >= 0){
            textViewForAccountBalance.setText(String.valueOf(balance));
            textViewForAccountBalance.append(" "+NameRelatedSupport.TAKA);
            textViewForAccountBalance.setTextColor(Color.BLACK);
            textViewForAccountStatus.setTextColor(Color.BLACK);
            textViewForAccountStatus.setText(NameRelatedSupport.APNI_PABEN);
        }else{
            textViewForAccountBalance.setText(String.valueOf(-balance));
            textViewForAccountBalance.append(" "+NameRelatedSupport.TAKA);
            textViewForAccountBalance.setTextColor(Color.RED);
            textViewForAccountStatus.setTextColor(Color.RED);
            textViewForAccountStatus.setText(NameRelatedSupport.APNAR_KACE_PABE);
        }

    }

    private void initializeVariables() {
        textViewForAccountBalance = findViewById(R.id.ShowTransactionFeatureTextViewForShowingAccountBalance);
        textViewForAccountStatus = findViewById(R.id.ShowTransactionFeatureTextViewForShowingAccountStatus);
        imageViewForAccountImage = findViewById(R.id.ShowTransactionFeatureImageViewForShowingAccountImage);

        int currentAccountID = Integer.parseInt(getIntent().getStringExtra(NameRelatedSupport.ACCOUNT_ID));
        currentAccount = DatabaseHelper.getInstance(this).getAccountAccordingToID(currentAccountID);

        recyclerView = findViewById(R.id.ShowTransactionFeatureRecycleViewForShowingAllTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        accountOptions = findViewById(R.id.ShowTransactionFeatureTextViewForShowingAccountOptions);
        accountOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(ShowAccountTransactionActivity.this, accountOptions);
                popup.inflate(R.menu.menu_transaction_list_account_options);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.menuForDeleteAccount:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ShowAccountTransactionActivity.this);
                                builder1.setTitle("Delete This Account");
                                builder1.setMessage("Are you sure?");
                                builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseHelper.getInstance(ShowAccountTransactionActivity.this).deleteAccount(currentAccount);
                                        DatabaseHelper.getInstance(ShowAccountTransactionActivity.this).deleteAllTransaction(currentAccount);
                                        InternalMemorySupport.deleteImageFileFromInternalMemory(currentAccount.getAccountImageLocation());
                                        Toast.makeText(ShowAccountTransactionActivity.this,"Account Deleted!",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                                return true;
                            case R.id.menuForDeleteAllTransaction:
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ShowAccountTransactionActivity.this);
                                builder2.setTitle("Delete All Transactions");
                                builder2.setMessage("Are you sure?");
                                builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseHelper.getInstance(ShowAccountTransactionActivity.this).deleteAllTransaction(currentAccount);
                                        dialog.dismiss();
                                        Toast.makeText(ShowAccountTransactionActivity.this,"All Transaction Deleted!",Toast.LENGTH_SHORT).show();
                                        displayScreen();
                                    }
                                });
                                builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert2 = builder2.create();
                                alert2.show();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_account_transaction_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuForAddNewTransaction){
            Intent intent = new Intent(ShowAccountTransactionActivity.this, AddTransactionActivity.class);
            intent.putExtra(NameRelatedSupport.ACCOUNT_ID, String.valueOf(currentAccount.getAccountId()));
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScreen();
    }

    @Override
    public void onBackPressed() {
        finish();
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

        class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            private Transaction transaction;

            private TextView transactionDateTextView;
            private TextView transactionStatusTextView;
            private TextView transactionAmountTextView;


            private TransactionViewHolder( View itemView) {
                super(itemView);
                transactionDateTextView = itemView.findViewById(R.id.ShowTransactionListFeatureTextViewForShowingTransactionDateTime);
                transactionStatusTextView = itemView.findViewById(R.id.ShowTransactionListFeatureTextViewForShowingTransactionStatus);
                transactionAmountTextView = itemView.findViewById(R.id.ShowTransactionListFeatureTextViewForShowingTransactionAmount);

                itemView.setOnLongClickListener(this);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowAccountTransactionActivity.this);
                builder.setTitle("Delete This Transaction");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper.getInstance(ShowAccountTransactionActivity.this).deleteTransaction(transaction);
                        Toast.makeText(ShowAccountTransactionActivity.this,"Transaction Deleted!",Toast.LENGTH_SHORT).show();
                        displayScreen();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }

            private void showThisTransaction(Transaction transaction){
                this.transaction = transaction;
                transactionDateTextView.setText(transaction.getTransactionDate());
                transactionStatusTextView.setText((transaction.getTransactionBalance()>=0)? "জমা":"খরচ");
                transactionAmountTextView.setText(String.valueOf(transaction.getTransactionBalance()));

                if(transaction.getTransactionBalance() < 0){
                    transactionDateTextView.setTextColor(Color.RED);
                    transactionStatusTextView.setTextColor(Color.RED);
                    transactionAmountTextView.setTextColor(Color.RED);

                    transactionDateTextView.setText(transaction.getTransactionDate());
                    transactionStatusTextView.setText(R.string.takeMoney);
                    transactionAmountTextView.setText(String.valueOf(-transaction.getTransactionBalance()));
                }else{
                    transactionDateTextView.setTextColor(Color.BLACK);
                    transactionStatusTextView.setTextColor(Color.BLACK);
                    transactionAmountTextView.setTextColor(Color.BLACK);

                    transactionDateTextView.setText(transaction.getTransactionDate());
                    transactionStatusTextView.setText(R.string.giveMoney);
                    transactionAmountTextView.setText(String.valueOf(transaction.getTransactionBalance()));
                }
            }


        }

    }
}
