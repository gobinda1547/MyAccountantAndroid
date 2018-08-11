package myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.ImageProcessingSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.InternalMemorySupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.NameRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.SizeRelatedSupport;
import myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature.AddAccountActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DatabaseHelper;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_transaction_feature.ShowAccountTransactionActivity;


public class ShowAccountListActivity extends AppCompatActivity {

    private Dialog dialogForPopUp;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_list);
        setTitle(R.string.titleForShowAccountListActivity);

        initializeVariables();
        displayScreen();
    }

    private void displayScreen() {
        List<Account> accounts = DatabaseHelper.getInstance(this).getAllTheAccountFromTable();
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(accounts,this);
        recyclerView.setAdapter(adapter);
    }

    private void initializeVariables() {
        InternalMemorySupport.createAppFolder(this);
        if(!SizeRelatedSupport.screenSizeInitialized){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            SizeRelatedSupport.initializeScreenSize(size.x, size.y);
        }

        dialogForPopUp = new Dialog(this);
        recyclerView = findViewById(R.id.ShowAccountListFeatureRecycleViewForShowingAccountList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuForAddNewAccount){
            Intent intent = new Intent(ShowAccountListActivity.this, AddAccountActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScreen();
    }

    public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<MyRecyclerViewAdapter.AccountViewHolder>{

        private List<Account> accounts;
        private Context mCtx;

        private MyRecyclerViewAdapter(List<Account> accounts, Context mCtx){

            this.mCtx = mCtx;
            Collections.sort(accounts, new Comparator<Account>() {
                @Override
                public int compare(Account t1, Account t2) {
                    return t1.getAccountName().compareTo(t2.getAccountName());
                }
            });
            this.accounts = accounts;
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }

        @Override
        public AccountViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_account_list_per_item, viewGroup, false);
            return new AccountViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AccountViewHolder accountViewHolder, int i) {
            accountViewHolder.showThisAccount(accounts.get(i));
        }

        class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private Account account;

            private TextView accountNameTextView;
            private TextView accountMobileNumberTextView;
            private ImageView accountImageImageView;
            private TextView textViewOptions;

            private AccountViewHolder(View itemView) {
                super(itemView);

                accountNameTextView = itemView.findViewById(R.id.ShowAccountListFeatureTextViewForShowingAccountName);
                accountMobileNumberTextView = itemView.findViewById(R.id.ShowAccountListFeatureTextViewForShowingAccountMobile);
                accountImageImageView = itemView.findViewById(R.id.ShowAccountListFeatureImageViewForShowingAccountImage);
                textViewOptions = itemView.findViewById(R.id.ShowAccountListFeatureTextViewForShowingAccountOptions);

                itemView.setOnClickListener(this);
                /*
                accountImageImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( ShowAccountListActivity.this , EditAccountActivity.class);
                        intent.putExtra(NameRelatedSupport.ACCOUNT_ID,String.valueOf(account.getAccountId()));
                        intent.putExtra(NameRelatedSupport.PARENT_ACTIVITY_NAME, NameRelatedSupport.SHOW_ACCOUNT_LIST_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                });
                */

                accountImageImageView.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){

                        dialogForPopUp.setContentView(R.layout.popup_show_account_details);

                        ImageView popUpImage = dialogForPopUp.findViewById(R.id.ShowAccountDetailsFeaturePopUpAccountImage);
                        TextView popUpName = dialogForPopUp.findViewById(R.id.ShowAccountDetailsFeaturePopUpAccountName);
                        TextView popUpMobile = dialogForPopUp.findViewById(R.id.ShowAccountDetailsFeaturePopUpAccountMobileNumber);
                        Button popUpDismissButton = dialogForPopUp.findViewById(R.id.ShowAccountDetailsFeaturePopUpDismissButton);

                        Bitmap bitmap = InternalMemorySupport.getImageFileFromThisLocation(account.getAccountImageLocation());

                        popUpImage.setImageBitmap(bitmap);
                        popUpName.setText(account.getAccountName());
                        popUpMobile.setText(account.getAccountMobileNumber());
                        if(account.getAccountMobileNumber().length() == 0){
                            popUpMobile.setText(NameRelatedSupport.MOBILE_NOT_FOUND);
                        }

                        popUpDismissButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogForPopUp.dismiss();
                            }
                        });
                        dialogForPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogForPopUp.show();

                    }
                });


                textViewOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(mCtx, textViewOptions);
                        popup.inflate(R.menu.menu_account_list_per_item);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menuForCall:
                                        Toast.makeText(ShowAccountListActivity.this,"menu For Call",Toast.LENGTH_LONG).show();                                    break;
                                    case R.id.menuForMessage:
                                        Toast.makeText(ShowAccountListActivity.this,"menu for message",Toast.LENGTH_LONG).show();
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }
                });
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ShowAccountListActivity.this , ShowAccountTransactionActivity.class);
                intent.putExtra(NameRelatedSupport.ACCOUNT_ID, String.valueOf(account.getAccountId()));
                startActivity(intent);
            }

            private void showThisAccount(Account account){
                this.account = account;
                accountNameTextView.setText(account.getAccountName());

                //showing mobile number
                String mobileNumber = account.getAccountMobileNumber();
                if(mobileNumber.length() == 0){
                    accountMobileNumberTextView.setText(NameRelatedSupport.MOBILE_NOT_FOUND);
                }else{
                    accountMobileNumberTextView.setText(mobileNumber);
                }

                //reading file and showing
                Bitmap image = InternalMemorySupport.getImageFileFromThisLocation(account.getAccountImageLocation());
                if(image == null){
                    image = ImageProcessingSupport.getDefaultAccountImage(ShowAccountListActivity.this);
                }
                accountImageImageView.setImageBitmap(image);
            }


        }

    }

}
