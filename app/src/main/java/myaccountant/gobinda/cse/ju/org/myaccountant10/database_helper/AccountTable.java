package myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Account;


public class AccountTable extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "my_accountant";
    private static final String TABLE_NAME = "account_table";

    private static final String C1 = "c1_account_id";
    private static final String C2 = "c2_account_name";
    private static final String C3 = "c3_account_mobile_number";
    private static final String C4 = "c4_account_image";

    AccountTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            String sbForAccount = "";
            sbForAccount = sbForAccount.concat("CREATE TABLE " + TABLE_NAME + "( ");
            sbForAccount = sbForAccount.concat(C1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C2 + " TEXT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C3 + " TEXT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C4 + " BLOB );");
            db.execSQL(sbForAccount);
        }catch (Exception e){
            e.printStackTrace();
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
            cv.put(C4, account.getAccountImage());
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
                    acc.setAccountImage(rs.getBlob(3));
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
            String[] selectedFields = new String[]{C1, C2, C3, C4};
            String condition = C1 +" = ?";
            String[] conditionValues = new String[] {String.valueOf(accountID)};

            Cursor rs = db.query(TABLE_NAME, selectedFields, condition, conditionValues, null, null, null, null);
            if(rs.moveToFirst()) {
                acc = new Account();
                acc.setAccountId(rs.getInt(0));
                acc.setAccountName(rs.getString(1));
                acc.setAccountMobileNumber(rs.getString(2));
                acc.setAccountImage(rs.getBlob(3));
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return acc;
    }


}
