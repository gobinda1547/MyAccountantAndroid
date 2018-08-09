package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;


public class Account {

    private int accountId;
    private String accountName;
    private String accountMobileNumber;
    private String accountImageLocation;

    public Account(int accountId, String accountName, String accountMobileNumber, String accountImageLocation) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountMobileNumber = accountMobileNumber;
        this.accountImageLocation = accountImageLocation;
    }

    public Account( String accountName, String accountMobileNumber, String accountImageLocation) {
        this.accountName = accountName;
        this.accountMobileNumber = accountMobileNumber;
        this.accountImageLocation = accountImageLocation;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountMobileNumber() {
        return accountMobileNumber;
    }

    public String getAccountImageLocation(){ return accountImageLocation; }

}
