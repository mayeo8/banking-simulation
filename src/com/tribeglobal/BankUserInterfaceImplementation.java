package com.tribeglobal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//implementing the BankUserInterface interface
public class BankUserInterfaceImplementation implements  BankUserInterface{
    //creating global variables that will be used and shared by all the methods
    Connection connect;
    Boolean check = false, withdrawalCheck = false, transferCheck = false, deleteCheck = false;
    int password;
    String name;
    static double accountbalance, transfer, transferAccountBalance;


    @Override
    public void createBankUser(bankUser user) {
        //first creating a connection
        connect = DBConnection.createDBConnection();
        //assigning values using query to the database
        String query = "insert into bankuser values(?,?,?,?,?,?)";
        //when using this query all fields in the column must be filled
        try {
            //using PreparedStatement to prepare the query and pass in the values dynamically from the BankUser object
            //from the main.java method
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setInt(1,user.getId());
            statement.setString(2, user.getAccountFirstName());
            statement.setString(3, user.getAccountLastName());
            statement.setInt(4, user.getAccountNumber());
            statement.setInt(5, user.getAccountPassword());
            statement.setDouble(6, user.getAccountBalance());
            //checking to see if the insertion was successful
            int rows = statement.executeUpdate();
            if (rows>0){
                System.out.println("Bank User created Successfully");
                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Boolean login(int password, String name) {
        //assigning this method parameters to the global password and name created so all the other methods can use it
        //before every other method is run the user must log in so the variable are never null when being used by other method
        this.name = name;
        this.password = password;
        connect = DBConnection.createDBConnection();
        //database query to return firstname, password from the table bankuser where the first name is the name gotten from the method parameter
        String query = "select firstname, password from bankuser where firstname=?" ;
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            //looping through all the result from the database
            while (result.next()){
                //validation
                //checking if the name exist in the database if it does
                if (name.equals(result.getString("firstname"))){
                    //checking if it matches the password
                    if (password == result.getInt("password")){
                        System.out.println("Welcome " + name);
                        //returns a boolean because for some reasons else statement don't work out here
                        //the boolean value is to be used in the main method to check if it was successful or not
                        check = true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    @Override
    public void depositFunds(double depositAmount) {
        //creating connection
        connect = DBConnection.createDBConnection();
        //this method implements two query one to get the current account balance the other to update it after deposit
        String query = "select accountbalance from bankuser where firstname=?" ;
        String updateFunds = "update bankuser set accountbalance=? where firstname=?";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            //always using the login name, so we know which user is making changes to their account
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                //getting the bank account and adding the deposited funds and storing it in a static variable
                accountbalance = depositAmount + result.getDouble("accountbalance");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            //executing the second query to update the account balance setting the static variable created to the new account balance
            PreparedStatement state = connect.prepareStatement(updateFunds);
            state.setDouble(1, accountbalance);
            //always using the login name, so we know which user is making changes to their account
            state.setString(2, name);
            int row = state.executeUpdate();
            if(row > 0){
                System.out.println("Transaction Successful");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Boolean withdrawFunds(double withdrawAmount) {
        //this method also execute two query one to get the current account balance the other to update it with the withdrew funds
        String query = "select accountbalance from bankuser where firstname=?" ;
        String updateFunds = "update bankuser set accountbalance=? where firstname=?";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            //using login name
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                //getting results and checking if the withdray amount is greater than account balance
                if (result.getDouble("accountbalance") > withdrawAmount){
                    //if it is greater deduct it from account balance
                    accountbalance -= withdrawAmount;
                    //this method returns a boolean that will be used to display messages in the main.java file
                    withdrawalCheck = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            //executing the second query to update the new account balance
            PreparedStatement state = connect.prepareStatement(updateFunds);
            state.setDouble(1, accountbalance);
            state.setString(2, name);
            state.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
        return withdrawalCheck;
    }

    @Override
    public void checkBalance() {
        //this method is simple returns all account balance with firstname the login name
        String query = "select accountbalance from bankuser where firstname=?" ;
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                //loop through the result and print out the balance
                System.out.println("Your Account Balance Is " + result.getDouble("accountbalance"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean transferFunds(String accountName, int accountNumber, double transferAmount) {
        connect = DBConnection.createDBConnection();
        //this method is the most complex method it execute three query
        //query 1 gets information about the destination account
        //query 2 selects the account balance from the destination account
        //query 3 updates both account balances
        String query = "select firstname, accountnumber, accountbalance from bankuser where firstname=?" ;
        String senderQuery = "select accountbalance from bankuser where firstname=?" ;
        String updateFunds = "update bankuser set accountbalance=? where firstname=?";

        try {
            //this statement prepares the senderQuery which returns the account balance of the one sending money
            PreparedStatement senderStatement = connect.prepareStatement(senderQuery);
            senderStatement.setString(1, name);
            ResultSet res = senderStatement.executeQuery();
            while (res.next()){
                //checking the result if the sender has enough money to send if he does
                if (res.getDouble("accountbalance") > transferAmount){
                    //deduct the money from his account
                    transferAccountBalance = res.getDouble("accountbalance") - transferAmount;
                    PreparedStatement senderState = connect.prepareStatement(updateFunds);
                    //preparing the update his account with the new account balance
                    senderState.setDouble(1, transferAccountBalance);
                    senderState.setString(2, name);
                    //execute the update
                    senderState.executeUpdate();
                }
                //if he does not have enough money print out a message
                if (res.getDouble("accountbalance") < transferAmount){
                    System.out.println("insufficient balance");
                    //this method returns a boolean in this case it return false that is then use din the main.java to send an error message
                    return transferCheck;
                }
            }
            //preparing the recipient account details from the database
            PreparedStatement statement = connect.prepareStatement(query);
            //passing the parameter name that is specifying the recipient account
            statement.setString(1, accountName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                //checking the result if the entered name matches any account name in the database
                if (accountName.equals(result.getString("firstname"))){
                    //if the name exist check the account number
                    if (accountNumber == result.getInt("accountnumber")){
                        //if both information are true add the transfer amount to the current account balance
                        System.out.println("You Are Going To Be Transferring "+ transferAmount + " To "+  accountName);
                        //adding the transferAmount to the recipient account balance
                            transfer = result.getDouble("accountbalance") + transferAmount;
                            //preparing the update statement to update the new balance
                            PreparedStatement state = connect.prepareStatement(updateFunds);
                            //updating the account with the static variable transfer and pass in the recipient name
                            state.setDouble(1, transfer);
                            state.setString(2, accountName);
                            state.executeUpdate();
                        //this method returns a boolean in this case it return false that is then used in the main.java to send a success message
                            transferCheck = true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //throughout the program the method is returning states of the boolean transferCheck
        return transferCheck;
    }

    @Override
    public Boolean deleteBankUser() {
        connect = DBConnection.createDBConnection();
        //simple method that deletes the login user account from the database
        String query = "delete from bankuser where firstname=?";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, name);
            int row = statement.executeUpdate();
            //check if the action was successful
            if (row > 0){
                System.out.println("your Account Has Been Permanently Deleted");
                //also returns a boolean that will be used in the main.java
                deleteCheck = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deleteCheck;
    }
}
