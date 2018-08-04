package myaccountant.gobinda.cse.ju.org.myaccountant10.database_helper;

import android.content.Context;

/**
 * Created by gobinda22 on 7/29/2018.
 */

public class DbManager {
    private static  DbManager dbManager;

    private AccountTable accountTable;
    private TransactionTable transactionTable;


    public static  void initialize(Context context){
        if(dbManager == null) {
            dbManager = new DbManager(context);
        }
    }

    public DbManager(Context context){
        accountTable = new AccountTable(context);
        transactionTable = new TransactionTable(context);
    }

    public static  AccountTable getAccountTableAccess() {
        return dbManager.accountTable;
    }

    public TransactionTable getTransactionTableAccess() {
        return dbManager.transactionTable;
    }
}
