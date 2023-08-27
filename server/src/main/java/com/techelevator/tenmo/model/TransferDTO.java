package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO
{
    private int transferId;
    private BigDecimal transferAmount;
    private String from;
    private String to;

    public TransferDTO(int transferId, BigDecimal transferAmount, String from, String to)
    {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.from = from;
        this.to = to;
    }

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount()
    {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount)
    {
        this.transferAmount = transferAmount;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
}
