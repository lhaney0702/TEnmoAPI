package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public class TransferController
{
    private TransferDao transferDao;

    public TransferController(TransferDao transferDao)
    {
        this.transferDao = transferDao;
    }

    // TODO Fill in PATH here
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()") // ????? maybe???
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer createTransferBySenderAndRecipient
            (@Valid @RequestBody int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType)
    {
        return transferDao.createTransfer(senderAccountId, recipientAccountId, transferAmount, transferType);
    }




    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId)
    {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfer;
        }
    }



    @RequestMapping(path = "/accounts/{id}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersForAccountByAccountId(@PathVariable("id") int accountId)
    {
        List<Transfer> transfers = transferDao.getTransfersForAccount(accountId);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return transfers;
        }

    }

}
