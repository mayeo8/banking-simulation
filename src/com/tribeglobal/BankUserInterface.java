package com.tribeglobal;

public interface BankUserInterface {
    //creating all the method we are going to need to run the application from beginning to the ending
    void createBankUser(bankUser user);
    Boolean login(int password, String name);
    void depositFunds(double depositAmount);
    Boolean withdrawFunds(double withdrawAmount);
    void checkBalance();
    boolean transferFunds(String accountName, int accountNumber, double transferAmount);
    Boolean deleteBankUser();
}
