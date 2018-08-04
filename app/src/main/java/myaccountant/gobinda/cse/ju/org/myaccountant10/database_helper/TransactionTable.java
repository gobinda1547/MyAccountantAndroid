package myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gobinda22 on 7/20/2018.
 */

public class TransactionTable extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_accountant";
    private static final String TABLE_NAME = "transaction_table";

    private static final String C1 = "c1_transaction_id";
    private static final String C2 = "c2_transaction_account_id";
    private static final String C3 = "c3_transaction_date_time";
    private static final String C4 = "c4_transaction_type";
    private static final String C5 = "c5_transaction_amount";

    public TransactionTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            StringBuilder sbForTransaction = new StringBuilder();
            sbForTransaction.append("CREATE TABLE " + TABLE_NAME + "(");
            sbForTransaction.append(C1 +"  INTEGER NOT NULL AUTO_INCREMENT,");
            sbForTransaction.append(C2 + " INTEGER NOT NULL,");
            sbForTransaction.append(C3 + " TEXT NOT NULL,");
            sbForTransaction.append(C4 + " flag INTEGER DEFAULT 0,");
            sbForTransaction.append(C5 + " INTEGER NOT NULL,");
            sbForTransaction.append("PRIMARY KEY ("+C1+"))");
            db.execSQL(sbForTransaction.toString());
            //db.close();
            Log.d("table creation","TransactionTable created");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
