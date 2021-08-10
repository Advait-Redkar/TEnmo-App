package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;


    }

    public List<Account> getAccountByUserId(int userId) {
        List<Account> account = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance " +
                "FROM accounts " +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            account.add(mapRowToAccount(results));
        }
        return account;

    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();

        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }


    @Override
    public BigDecimal getBalanceForUserId(int loggedInUserId) {
        String sql = "SELECT SUM(balance) FROM accounts WHERE user_id = ?;";

        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, loggedInUserId);
            return balance;
        } catch (Exception e) {
            System.out.println("Cannot find balance for user " + loggedInUserId + e.getMessage());
            return BigDecimal.ZERO;
        }

    }

    @Override
    public int getAccountIdForUserId(int loggedInUserId) {
        String sql="SELECT account_id FROM accounts WHERE user_id=?;";
        try{
            int accountId=jdbcTemplate.queryForObject(sql,Integer.class,loggedInUserId);
            return accountId;
        }catch (Exception e){
            System.out.println("Cannot find account for user " + loggedInUserId + e.getMessage());
            return 0;
        }
    }

    @Override
    public Account getAccountForUserId(int loggedInUserId) {
        return null;
    }


}
