package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }


    @RequestMapping(path = "/account/transfer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createTransfer(@RequestBody Transfer transfer, Principal principal) {
        return transferDao.createTransfer(transfer, principal);
    }
    @RequestMapping(path = "/account/requesttransfer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createRequestTransfer(@RequestBody Transfer transfer, Principal principal) {
        return transferDao.createRequestTransfer(transfer, principal);
    }

    @RequestMapping(path = "/account/transfer", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        String loggedInUserName = principal.getName();
        int loggedInUserId = userDao.findIdByUsername(loggedInUserName);
        return transferDao.getAllTransfers(principal);
    }


    @RequestMapping(path = "/account/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) throws NullPointerException {
        Transfer transfer = transferDao.getTransferByTransferId(transferId);


        return transfer;
    }


}
