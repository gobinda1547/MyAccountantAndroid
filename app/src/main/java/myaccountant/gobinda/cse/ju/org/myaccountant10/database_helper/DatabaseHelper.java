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
import myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes.Transaction;

/**
 * Created by gobinda22 on 8/8/2018.
 */

public class DatabaseHelper  extends SQLiteOpenHelper {

    private static DatabaseHelper dbHelper;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_accountant";

    private static final String TABLE_NAME1 = "account_table";
    private static final String TABLE_NAME2 = "transaction_table";

    private static final String C1 = "account_id";
    private static final String C2 = "account_name";
    private static final String C3 = "account_mobile_number";
    private static final String C4 = "account_image_location";

    private static final String T1 = "transaction_id";
    private static final String T2 = "transaction_account_id";
    private static final String T3 = "transaction_amount";
    private static final String T4 = "transaction_date_time";










    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //creating
        try{
            String sbForAccount = "";
            sbForAccount = sbForAccount.concat("CREATE TABLE " + TABLE_NAME1 + "( ");
            sbForAccount = sbForAccount.concat(C1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C2 + " TEXT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C3 + " TEXT NOT NULL, ");
            sbForAccount = sbForAccount.concat(C4 + " TEXT NOT NULL );");
            db.execSQL(sbForAccount);
        }catch (Exception e){
            e.printStackTrace();
        }

        //creating transaction table
        try{
            String sbForTransaction = "";
            sbForTransaction = sbForTransaction.concat("CREATE TABLE " + TABLE_NAME2 + "(");
            sbForTransaction = sbForTransaction.concat(T1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
            sbForTransaction = sbForTransaction.concat(T2 + " INTEGER NOT NULL, ");
            sbForTransaction = sbForTransaction.concat(T3 + " INTEGER NOT NULL, ");
            sbForTransaction = sbForTransaction.concat(T4 + " TEXT NOT NULL );");
            db.execSQL(sbForTransaction);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }


    //methods for account table

    public boolean insertAccount( Account account) throws SQLiteException {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new  ContentValues();
            cv.put(C2, account.getAccountName());
            cv.put(C3, account.getAccountMobileNumber());
            cv.put(C4, account.getAccountImageLocation());
            db.insert(TABLE_NAME1, null, cv );
            db.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public List<Account> getAllTheAccountFromTable(){
        ArrayList<Account> accountList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor rs = db.rawQuery("SELECT  * FROM " + TABLE_NAME1, null);
            if (rs.moveToFirst()) {
                do {
                    Account acc = new Account( rs.getInt(0), rs.getString(1), rs.getString(2), rs.getString(3));
                    accountList.add(acc);
                } while (rs.moveToNext());
            }
            rs.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountList;
    }

    public boolean updateAccount(Account account){
        try{
            String whereCondition = C1+" = ?";
            String[] whereConditionValues = new String[]{String.valueOf(account.getAccountId())};
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(C1, account.getAccountId());
            values.put(C2, account.getAccountName());
            values.put(C3, account.getAccountMobileNumber());
            values.put(C4, account.getAccountImageLocation());
            db.update(TABLE_NAME1,values,whereCondition,whereConditionValues);
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Account getAccountAccordingToID(int accountID){
        Account acc = null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String[] selectedFields = new String[]{C1, C2, C3, C4};
            String condition = C1 +" = ?";
            String[] conditionValues = new String[] {String.valueOf(accountID)};

            Cursor rs = db.query(TABLE_NAME1, selectedFields, condition, conditionValues, null, null, null, null);
            if(rs.moveToFirst()) {
                acc = new Account( rs.getInt(0), rs.getString(1), rs.getString(2), rs.getString(3));
            }
            rs.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return acc;
    }




    public List<Transaction> getAllTheTransactionForTheAccountID(int accountID){
        ArrayList<Transaction> transactionList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String[] selectedFields = new String[]{T1, T2, T3, T4};
            String condition = T2 +" = ?";
            String[] conditionValues = new String[] {String.valueOf(accountID)};

            Cursor rs = db.query(TABLE_NAME2, selectedFields, condition, conditionValues, null, null, null, null);
            if(rs.moveToFirst()) {
                do{
                    Transaction transaction = new Transaction(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getString(3));
                    transactionList.add(transaction);
                }while(rs.moveToNext());
            }
            rs.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return transactionList;
    }


    public boolean insertTransaction(Transaction transaction){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new  ContentValues();
            cv.put(T2, transaction.getAccountId());
            cv.put(T3, transaction.getTransactionBalance());
            cv.put(T4, transaction.getTransactionDate());
            db.insert(TABLE_NAME2, null, cv );
            db.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }


}
