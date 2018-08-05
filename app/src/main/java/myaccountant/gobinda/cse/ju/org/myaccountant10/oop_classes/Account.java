package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;


public class Account {

    private int accountId;
    private String accountName;
    private String accountMobileNumber;
    private byte[] accountImage;

    public Account(int accountId, String accountName, String accountMobileNumber, byte[] accountImage) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountMobileNumber = accountMobileNumber;
        this.accountImage = accountImage;
    }

    public Account( String accountName, String accountMobileNumber, byte[] accountImage) {
        this.accountName = accountName;
        this.accountMobileNumber = accountMobileNumber;
        this.accountImage = accountImage;
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

    public byte[] getAccountImage(){ return accountImage; }

}
