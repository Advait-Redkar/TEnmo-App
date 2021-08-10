package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int transferType;
    private int transferStatus;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private String usernameFrom;
    private String usernameTo;

    public Transfer() {
    }

    public Transfer(int transferId, int transferType, int transferStatus, int accountFrom, int accountTo, BigDecimal amount, String usernameFrom, String usernameTo) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
    }

    public Transfer(int transferId, int transferType, int transferStatus, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;

    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String returnStatus() {
        String returnStatusStr;
        if (getTransferStatus() == 1) {
            returnStatusStr = "Pending";
        } else if (getTransferStatus() == 2) {
            returnStatusStr = "Approved";
        } else if (getTransferStatus() == 3) {
            returnStatusStr = "Rejected";
        } else returnStatusStr = "Invalid response";
        return returnStatusStr;
    }
    public String transferTypeStr() {
        String transferTypeStr;
        if (getTransferType() == 1) {
            transferTypeStr = "From: ";
        } else if (getTransferType() == 2) {
            transferTypeStr = "To: ";
        } return transferTypeStr();
    }


}

