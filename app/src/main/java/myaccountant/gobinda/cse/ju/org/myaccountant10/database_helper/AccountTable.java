package myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;

/**
 * Created by gobinda22 on 7/20/2018.
 */

public class AccountTable extends SQLiteOpenHelper{

    private static final String DEBUG_TAG = "AccountTable";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "my_accountant";
    private static final String TABLE_NAME = "account_table";

    private static final String C1 = "c1_account_id";
    private static final String C2 = "c2_account_name";
    private static final String C3 = "c3_account_mobile_number";
    private static final String C4 = "c4_account_balance";
    private static final String C5 = "c5_account_address";
    private static final String C6 = "c6_account_image";

    public AccountTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            StringBuilder sbForAccount = new StringBuilder();
            sbForAccount.append("CREATE TABLE " + TABLE_NAME + "(");;
            sbForAccount.append(C1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
            sbForAccount.append(C2 + " TEXT NOT NULL, ");
            sbForAccount.append(C3 + " TEXT NOT NULL, ");
            sbForAccount.append(C4 + " INTEGER NOT NULL, ");
            sbForAccount.append(C5 + " TEXT NOT NULL, ");
            sbForAccount.append(C6 + " BLOB );");
            db.execSQL(sbForAccount.toString());
            Log.d("table creation","AccountTable created");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("table creation", "pporoblem while creating acccount table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAccount( Account account) throws SQLiteException{

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new  ContentValues();
            cv.put(C2, account.getAccountName());
            cv.put(C3, account.getAccountMobileNumber());
            cv.put(C4, account.getAccountBalance());
            cv.put(C5, account.getAccountAddress());
            cv.put(C6, account.getAccountImage());
            db.insert(TABLE_NAME, null, cv );
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public List<Account> getAllTheAccountFromTable(){
        ArrayList<Account> accountList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor rs = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);
            if (rs.moveToFirst()) {
                do {
                    Account acc = new Account();
                    acc.setAccountId(rs.getInt(0));
                    acc.setAccountName(rs.getString(1));
                    acc.setAccountMobileNumber(rs.getString(2));
                    acc.setAccountAddress(rs.getString(4));
                    acc.setAccountBalance(rs.getLong(3));
                    acc.setAccountImage(rs.getBlob(5));
                    accountList.add(acc);
                } while (rs.moveToNext());
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountList;
    }

    public Account getAccountAccordingToID(int accountID){

        Account acc = null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String[] selectedFileds = new String[]{C1, C2, C3, C4, C5, C6};
            String condition = C1 +" = ?";
            Log.d("accoID Database",String.valueOf(accountID));
            String[] conditionValues = new String[] {String.valueOf(accountID)};

            Cursor rs = db.query(TABLE_NAME, selectedFileds, condition, conditionValues, null, null, null, null);
            if(rs.moveToFirst()) {
                acc = new Account();
                acc.setAccountId(rs.getInt(0));
                acc.setAccountName(rs.getString(1));
                acc.setAccountMobileNumber(rs.getString(2));
                acc.setAccountAddress(rs.getString(4));
                acc.setAccountBalance(rs.getLong(3));
                acc.setAccountImage(rs.getBlob(5));
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return acc;
    }


}
