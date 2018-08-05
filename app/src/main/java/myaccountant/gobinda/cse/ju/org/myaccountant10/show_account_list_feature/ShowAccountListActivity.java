package myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_list_feature;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myaccountant.gobinda.cse.ju.org.myaccountant10.ExtraSupport.MyScreenSize;
import myaccountant.gobinda.cse.ju.org.myaccountant10.add_account_feature.AddAccountActivity;
import myaccountant.gobinda.cse.ju.org.myaccountant10.R;
import myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper.DbManager;
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;
import myaccountant.gobinda.cse.ju.org.myaccountant10.show_account_feature.ShowAccountActivity;


public class ShowAccountListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_list);

        initializeMyScreenSize();

        DbManager.initialize(this);

        RecyclerView recyclerView = findViewById(R.id.idForAccountList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        List<Account> accounts = DbManager.getAccountTableAccess().getAllTheAccountFromTable();
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(accounts,this);
        recyclerView.setAdapter(adapter);
    }

    public void initializeMyScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MyScreenSize.initializeScreenSize(size.x, size.y);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int choosenItemId = item.getItemId();
        if(choosenItemId == R.id.main_activity_menu_addnewaccount){
            AddAccountActivity.initializeUserVariables();
            Intent intent = new Intent(ShowAccountListActivity.this, AddAccountActivity.class);
            startActivity(intent);
            //finish();
            return true;
        }
        return false;
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
            Account currentAccount = accounts.get(i);

            //int gAccountId = currentAccount.getAccountId();
            String gAccountName = currentAccount.getAccountName();
            String gAccountMobileNumber = currentAccount.getAccountMobileNumber();
            //long gAccountBalance = currentAccount.getAccountBalance();

            byte[] imageFromDB = currentAccount.getAccountImage();
            Bitmap image = BitmapFactory.decodeByteArray(imageFromDB,0,imageFromDB.length);


            //gAccountName = (gAccountName.length()>15)?
            accountViewHolder.accountName.setText(gAccountName);
            accountViewHolder.accountMobileNumber.setText((gAccountMobileNumber == null || gAccountMobileNumber.length() == 0)? "Number Not Found": gAccountMobileNumber);
            accountViewHolder.accountImage.setImageBitmap(image);

            final int position = i;
            accountViewHolder.accountImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = accounts.get(position).getAccountId();
                    Intent intent = new Intent( ShowAccountListActivity.this , ShowAccountActivity.class);
                    intent.putExtra("ACCOUNT_ID",String.valueOf(id));
                    startActivity(intent);
                    finish();
                }
            });

            accountViewHolder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(mCtx, accountViewHolder.textViewOptions);
                    popup.inflate(R.menu.menu_account_list_per_item);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    Toast.makeText(ShowAccountListActivity.this,"menu 1 selected!",Toast.LENGTH_LONG).show();                                    break;
                                case R.id.menu2:
                                    Toast.makeText(ShowAccountListActivity.this,"menu 1 selected!",Toast.LENGTH_LONG).show();
                                    break;
                                case R.id.menu3:
                                    Toast.makeText(ShowAccountListActivity.this,"menu 1 selected!",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

        }
        class AccountViewHolder extends RecyclerView.ViewHolder {

            private TextView accountName;
            private TextView accountMobileNumber;
            private ImageView accountImage;
            private TextView textViewOptions;

            private AccountViewHolder(View itemView) {
                super(itemView);
                accountName = itemView.findViewById(R.id.idForAccountName);
                accountMobileNumber = itemView.findViewById(R.id.idForAccountMobileNumber);
                accountImage = itemView.findViewById(R.id.idForAccountImage);
                textViewOptions = itemView.findViewById(R.id.textViewOptions);
            }
        }

    }

}
