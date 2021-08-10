package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;



    public Account() {
    }

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId(int userId) {
        return accountId;
    }

    public int getAccountId() {return accountId;}

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId(int accountId) {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
