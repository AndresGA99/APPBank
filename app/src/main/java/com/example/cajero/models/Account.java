package com.example.cajero.models;

public class Account {
    private String accountNumber;
    private double mount;
    private static Account Instance;
    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.mount = balance;
        Instance = Account.this;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public double getMount() {
        return mount;
    }
    public void setMount(double balance) {
        this.mount = balance;
    }
    public static Account getInstance() {
        return Instance;
    }
}
