package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;

/**
 * Transaction class has those parameters
 * customerid, transactionBalance, transactionDate,t
 * int , int , String
 */

public class Transaction {

    private int transactionID;
    private int accountId;
    private int transactionBalance;
    private String transactionDate;

    public Transaction(int transactionID, int accountId, int transactionBalance, String transactionDate) {
        this.transactionID = transactionID;
        this.accountId = accountId;
        this.transactionBalance = transactionBalance;
        this.transactionDate = transactionDate;
    }

    public Transaction(int accountId, int transactionBalance, String transactionDate) {
        this.accountId = accountId;
        this.transactionBalance = transactionBalance;
        this.transactionDate = transactionDate;
    }



    public int getTransactionID() {
        return transactionID;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getTransactionBalance() {
        return transactionBalance;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

}
