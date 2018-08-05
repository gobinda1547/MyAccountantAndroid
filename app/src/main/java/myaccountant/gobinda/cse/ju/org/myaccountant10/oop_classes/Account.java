package myaccountant.gobinda.cse.ju.org.myaccountant10.oop_classes;


public class Account {

    private int accountId;
    private String accountName;
    private String accountMobileNumber;
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

    public void setAccountImage(byte[] accountImage) {
        this.accountImage = accountImage;
    }
}
