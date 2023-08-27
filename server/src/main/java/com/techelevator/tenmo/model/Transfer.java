package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transfer
{
    private int transferId;
    private int senderAccountId;
    private int recipientAccountId;
    private BigDecimal transferAmount;
    private Date transferDate;
    private String transferStatus;
    private String transferType;
    private String senderUsername;
    private String recipientUsername;

    public Transfer()
    {

    }

    public Transfer(int transferId, int senderAccountId, int recipientAccountId, BigDecimal transferAmount, Date transferDate, String transferStatus, String transferType)
    {
        this.transferId = transferId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.transferAmount = transferAmount;
        this.transferDate = transferDate;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
    }

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public int getSenderAccountId()
    {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId)
    {
        this.senderAccountId = senderAccountId;
    }

    public int getRecipientAccountId()
    {
        return recipientAccountId;
    }

    public void setRecipientAccountId(int recipientAccountId)
    {
        this.recipientAccountId = recipientAccountId;
    }

    public BigDecimal getTransferAmount()
    {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount)
    {
        this.transferAmount = transferAmount;
    }

    public Date getTransferDate()
    {
        return transferDate;
    }

    public void setTransferDate(Date transferDate)
    {
        this.transferDate = transferDate;
    }

    public String getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    public String getTransferType()
    {
        return  transferType;
    }

    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public String getSenderUsername()
    {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername)
    {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername()
    {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername)
    {
        this.recipientUsername = recipientUsername;
    }
}
