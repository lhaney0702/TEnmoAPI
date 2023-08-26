package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

public class TransferController
{
    private TransferDao transferDao;

    public TransferController(TransferDao transferDao)
    {
        this.transferDao = transferDao;
    }

    public Transfer createTransferBySenderRecipient(@RequestBody int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType)
    {
        return transferDao.createTransfer(senderAccountId, recipientAccountId, transferAmount, transferType);
    }


}
