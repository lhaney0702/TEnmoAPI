package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController
{
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao)
    {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/create", method = RequestMethod.POST)
    public TransferDTO createTransfer(@Valid @RequestBody int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType)
    {
        Transfer newTransfer = transferDao.createTransfer(senderAccountId, recipientAccountId, transferAmount, transferType);
        return new TransferDTO(newTransfer.getTransferId(), transferAmount, newTransfer.getSenderUsername(), newTransfer.getRecipientUsername());
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public TransferDTO getTransferByTransferId(@PathVariable int transferId)
    {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        }
        else
        {
            return new TransferDTO(transferId, transfer.getTransferAmount(), transfer.getSenderUsername(), transfer.getRecipientUsername());
        }
    }

    @RequestMapping(path = "/user/transfers", method = RequestMethod.GET)
    public List<TransferDTO> getTransfersForUser(Principal principal)
    {
        List<Transfer> transfers = transferDao.getTransfersForUser(principal.getName());
        if (transfers == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        else
        {
            List<TransferDTO> transferDTOs = new ArrayList<>();
            for (Transfer transfer : transfers)
            {
                transferDTOs.add(new TransferDTO(transfer.getTransferId(), transfer.getTransferAmount(), transfer.getSenderUsername(), transfer.getRecipientUsername()));
            }

            return transferDTOs;
        }
    }

    @RequestMapping(path = "/transfers/{id}/approve", method = RequestMethod.PUT)
    public void approveTransfer(@PathVariable("id") int transferId)
    {
        transferDao.updateTransferStatus(transferId, true);
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer.getTransferType().equals("Requested"))
        {
            BigDecimal senderAccountBalance = accountDao.getCurrentBalance(transfer.getSenderAccountId());
            if (senderAccountBalance.doubleValue() < transfer.getTransferAmount().doubleValue())
            {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Declined. Insufficient funds.");
            }
            accountDao.updateAccountBalance(transfer.getSenderAccountId(), transfer.getTransferAmount().multiply(BigDecimal.valueOf(-1)));
            accountDao.updateAccountBalance(transfer.getRecipientAccountId(), transfer.getTransferAmount());
        }
    }

    @RequestMapping(path = "/transfers/{id}/reject", method = RequestMethod.PUT)
    public void rejectTransfer(@PathVariable("id") int transferId)
    {
        transferDao.updateTransferStatus(transferId, false);
    }

    @RequestMapping(path = "/transfers/users", method = RequestMethod.GET)
    public List<UserDTO> getUsersForTransfer()
    {
        List<User> users = userDao.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users)
        {
            userDTOs.add(new UserDTO(user.getUsername()));
        }

        return userDTOs;
    }
}
