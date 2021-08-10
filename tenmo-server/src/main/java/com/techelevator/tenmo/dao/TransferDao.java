package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    public Transfer createTransfer(Transfer newTransfer, Principal principal);

    public List<Transfer> getAllTransfers(Principal principal);

    public Transfer getTransferByTransferId(int transferId);

    public Transfer createRequestTransfer(Transfer transfer, Principal principal);
}
