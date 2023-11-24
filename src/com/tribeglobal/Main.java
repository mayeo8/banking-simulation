package com.tribeglobal;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //declaring global variables so i do not declare them everytime i was to get user input
        bankUser user;
        int id;
        String accountFirstName, accountLastName;
        int accountNumber, accountPassword, input, count = 0;
        Scanner sc = new Scanner(System.in);
        //creating an instance of the class implementing the interface i called it methods
        BankUserInterfaceImplementation methods = new BankUserInterfaceImplementation();
        System.out.println("Welcome to Tribe Bank");
        //the rest are self-explanatory just implementing the methods and supplying parameters to the methods
        while (true){
            System.out.println("""
                    1. create new account
                    2. login
                    3. exist
                    """);
            input = sc.nextInt();
            switch (input){
                case 1 -> {
                    user = new bankUser();
                    System.out.println("Welcome Please Fill Out This Online Form");
                    System.out.println("Enter ID:");
                    id = sc.nextInt();
                    System.out.println("Enter First Name:");
                    accountFirstName = sc.next();
                    System.out.println("Enter last Name:");
                    accountLastName = sc.next();
                    System.out.println("Enter Account Number:");
                    accountNumber = sc.nextInt();
                    System.out.println("Enter Password:");
                    accountPassword = sc.nextInt();
                    user.setAccountFirstName(accountFirstName);
                    user.setAccountLastName(accountLastName);
                    user.setId(id);
                    user.setAccountPassword(accountPassword);
                    user.setAccountNumber(accountNumber);
                    methods.createBankUser(user);

                }
                case 2 -> {
                    System.out.println("Please enter Your first name");
                    accountFirstName = sc.next();
                    System.out.println("Enter Password");
                    accountPassword = sc.nextInt();
                    if (methods.login(accountPassword, accountFirstName)){
                        System.out.println("""
                                Which Action Will You Be Performing Today
                                1. Deposit Funds
                                2. Withdraw Funds
                                3. Check Balance
                                4. Transfer Funds
                                5. Delete Your Account
                                """);
                        int choice = sc.nextInt();
                        switch (choice){
                            case 1 -> {
                                System.out.println("Enter Amount You Want To Deposit");
                                double depositAmount = sc.nextDouble();
                                methods.depositFunds(depositAmount);
                            }
                            case 2 -> {
                                System.out.println("Enter Amount You Want To Withdraw");
                                double withdrawAmount = sc.nextDouble();
                                if(methods.withdrawFunds(withdrawAmount)){
                                    System.out.println("Withdrawal successful");
                                }
                                else System.out.println("Insufficient Balance");
                            }
                            case 3 -> methods.checkBalance();
                            case 4 -> {
                                System.out.println("Enter Destination Account Name");
                                accountFirstName = sc.next();
                                System.out.println("Enter Destination Account Number");
                                accountNumber = sc.nextInt();
                                System.out.println("Enter Transfer Amount");
                                double transferAmount = sc.nextDouble();
                                if (methods.transferFunds(accountFirstName, accountNumber, transferAmount)){
                                    System.out.println("Transaction Successful");
                                }else System.out.println("Incorrect Transfer Destination");
                            }
                            case 5 -> {
                                System.out.println(accountFirstName +" Are You Sure You Want To Delete Your Account Permanently?");
                                System.out.println("""
                                        1. Yes
                                        2. No
                                        """);
                                int confirmDelete = sc.nextInt();
                                if (confirmDelete == 1){
                                            methods.deleteBankUser();
                                            System.out.println("Account Deleted Successfully");
                                }else System.out.println("Thank You For Your Time!");

                            }
                        }
                    }else {
                        count++;
                        if (count == 5){
                            System.out.println("You Have Input The Wrong Info " + count + " Times Your Account Has Been Banned");
                            System.exit(0);
                        }
                        System.out.println("Password Or Name Incorrect");
                    }

                }
                case 3 -> System.exit(0);
                default -> System.out.println("invalid Input");
            }
        }
    }
}
