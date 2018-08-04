package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;

/**
 * Transaction class has those parameters
 * customerid, transactionBalance, transactionDate,t
 * int , int , String
 */

public class Transaction {

    private int customerId;
    private int transactionBalance;
    private String transactionDate;
    private String transactiondetails;

    public Transaction(int customerId, int transactionBalance, String transactionDate, String transactiondetails) {
        this.customerId = customerId;
        this.transactionBalance = transactionBalance;
        this.transactionDate = transactionDate;
        this.transactiondetails = transactiondetails;
    }

    public String getTransactiondetails() {
        return transactiondetails;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getTransactionBalance() {
        return transactionBalance;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}
