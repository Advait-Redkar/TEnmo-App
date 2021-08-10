package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")


public class AccountController {

    private UserDao userDao;
    private AccountDao accountDao;

    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;

    }

    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);

        return accountDao.getBalanceForUserId(loggedInUserId);
    }

    @RequestMapping(path = "/account/users", method = RequestMethod.GET)
    public List<User> getAllUsers(Principal principal) {
        return userDao.findAll(principal);

    }

    @RequestMapping(path="/account/{id}",method = RequestMethod.GET)
    public int getAccountId(@RequestBody @PathVariable int id){
        return accountDao.getAccountIdForUserId(id);
    }

}
