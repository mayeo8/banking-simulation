package com.tribeglobal;
//a simple POJO
public class bankUser {
    //member variable of a typical bank user
    public int id;
    public String accountFirstName;
    public String accountLastName;
    public int accountNumber;
    public int accountPassword;
    public Double accountBalance = 0.0;
    //creating an empty constructor but this can be created by the compiler at runtime constructor
//    public  bankUser(){}


    //getters an setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountFirstName() {
        return accountFirstName;
    }

    public void setAccountFirstName(String accountFirstName) {
        this.accountFirstName = accountFirstName;
    }

    public String getAccountLastName() {
        return accountLastName;
    }

    public void setAccountLastName(String accountLastName) {
        this.accountLastName = accountLastName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(int accountPassword) {
        this.accountPassword = accountPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

}

