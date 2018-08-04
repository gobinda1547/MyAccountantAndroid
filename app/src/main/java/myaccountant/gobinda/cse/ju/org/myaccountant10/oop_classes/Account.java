package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;

/**
 * Created by gobinda22 on 7/20/2018.
 */

public class Account {

    private int accountId;
    private String accountName;
    private String accountMobileNumber;
    private long accountBalance;
    private String accountAddress;
    private byte[] accountImage;

    public int getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountMobileNumber() {
        return accountMobileNumber;
    }

    public long getAccountBalance() {
        return accountBalance;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public byte[] getAccountImage(){ return accountImage; }


    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountMobileNumber(String accountMobileNumber) {
        this.accountMobileNumber = accountMobileNumber;
    }

    public void setAccountBalance(long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public void setAccountImage(byte[] accountImage) {
        this.accountImage = accountImage;
    }
}
