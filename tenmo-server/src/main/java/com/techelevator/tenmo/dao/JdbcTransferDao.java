package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private List<Transfer> transfer = new ArrayList<>();
    //private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }


    public List<Transfer> getAllTransfers(Principal principal) {
        String currentUserName=principal.getName();
        int currentUserId= userDao.findIdByUsername(currentUserName);
        List<Transfer> transfers = new ArrayList<>();
        //String sql = "SELECT transfer_id, transfer_type_id, amount FROM transfers WHERE account_from = ? OR account_to = ?;";
        String sql="SELECT uf.username as username_from, ut.username as username_to, transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfers\n" +
                "INNER JOIN accounts AS a_f ON (account_from = a_f.account_id)\n " +
                "INNER JOIN accounts AS a_t ON (account_to = a_t.account_id)\n " +
                "INNER JOIN users AS uf ON a_f.user_id = uf.user_id\n " +
                "INNER JOIN users AS ut ON a_t.user_id = ut.user_id\n " +
                "WHERE a_f.user_id = ? OR a_t.user_id =?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUserId, currentUserId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;

    }

    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT uf.username AS username_from, ut.username as username_to, transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n " +
                "FROM transfers\n" +
                "INNER JOIN accounts AS a_f ON (account_from = a_f.account_id)\n " +
                "INNER JOIN accounts AS a_t ON (account_to = a_t.account_id)\n " +
                "INNER JOIN users AS uf ON a_f.user_id = uf.user_id\n " +
                "INNER JOIN users AS ut ON a_t.user_id = ut.user_id\n " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;

    }

    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createRequestTransfer(Transfer newTransfer, Principal principal) {

        BigDecimal senderBalance;
        BigDecimal transferAmount;

//logged in user needs to become accountFrom instead of accountTo
        String username = principal.getName();
        String userIdSql = "SELECT user_id FROM users WHERE username = ?;";

            int userFromId = jdbcTemplate.queryForObject(userIdSql, Integer.class, username);


            String accountToIdSql = "SELECT account_id FROM accounts WHERE user_id = ?;"; //+ userId + ";";
            int accountFromId = jdbcTemplate.queryForObject(accountToIdSql, Integer.class, userFromId);

            String accountFromIdSql = "SELECT account_id FROM accounts WHERE user_id= ?;";
            int accountToId = jdbcTemplate.queryForObject(accountToIdSql, Integer.class, newTransfer.getAccountTo());

            //senderBalance = accountDao.getBalanceForUserId(userFromId);
            transferAmount = newTransfer.getAmount();

            //if (transferAmount.compareTo(senderBalance) > 0) {
            //something here which will send a msg to a client indicating transfer unsuccesful?
            //} //else {
            //String sqltransferFrom = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
            //String sqltransferTo = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";
            //jdbcTemplate.update(sqltransferFrom, transferAmount, accountFromId);
            //jdbcTemplate.update(sqltransferTo, transferAmount, accountToId);
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (1, 1, ?, ?, ?) RETURNING transfer_id;";
            int newId = jdbcTemplate.queryForObject(sql, Integer.class, accountFromId, accountToId, newTransfer.getAmount());
            //}
        return newTransfer;

    }
            @ResponseStatus(HttpStatus.CREATED)
            public Transfer createTransfer(Transfer newTransfer, Principal principal) {

                BigDecimal senderBalance;
                BigDecimal transferAmount;


                String username = principal.getName();
                String userIdSql = "SELECT user_id FROM users WHERE username = ?;";
                try {
                    int userFromId = jdbcTemplate.queryForObject(userIdSql, Integer.class, username);


                    String accountFromIdSql = "SELECT account_id FROM accounts WHERE user_id = ?;"; //+ userId + ";";
                    int accountFromId = jdbcTemplate.queryForObject(accountFromIdSql, Integer.class, userFromId);

                    String accountToIdSql = "SELECT account_id FROM accounts WHERE user_id= ?;";
                    int accountToId = jdbcTemplate.queryForObject(accountFromIdSql, Integer.class, newTransfer.getAccountTo());

                    senderBalance = accountDao.getBalanceForUserId(userFromId);
                    transferAmount = newTransfer.getAmount();

                    if (transferAmount.compareTo(senderBalance) > 0) {
                        //something here which will send a msg to a client indicating transfer unsuccesful?
                    } else {
                        String sqltransferFrom = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
                        String sqltransferTo = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";
                        jdbcTemplate.update(sqltransferFrom, transferAmount, accountFromId);
                        jdbcTemplate.update(sqltransferTo, transferAmount, accountToId);
                        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                                "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id;";
                        int newId = jdbcTemplate.queryForObject(sql, Integer.class, accountFromId, accountToId, newTransfer.getAmount());
                    }


            /*OLD CODE: String transferFrom = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
            String transferTo = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";

            if (amount.compareTo(balance) == 1 && amount.compareTo(BigDecimal.ZERO) == 1) {
                jdbcTemplate.update(transferFrom, amount, accountFromId);
                jdbcTemplate.update(transferTo, amount, accountToId);


            }*/


        } catch (Exception e) {
            System.out.println("Null pointer exception, try again " + e.getMessage());
        }

        return newTransfer;

    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferType(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatus(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setUsernameFrom(rowSet.getString("username_from"));
        transfer.setUsernameTo(rowSet.getString("username_to"));
        return transfer;
    }

}

