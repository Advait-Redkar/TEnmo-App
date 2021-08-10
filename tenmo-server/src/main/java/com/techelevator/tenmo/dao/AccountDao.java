package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    //possibly getAccountForDao--dont get circular
    BigDecimal getBalanceForUserId(int loggedInUserId);
    int getAccountIdForUserId(int loggedInUserId);
    Account getAccountForUserId(int loggedInUserId);
}
