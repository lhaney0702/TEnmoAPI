package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import java.math.BigDecimal;
import java.util.List;

public interface TransferDao
{
    Transfer createTransfer(int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType);

    Transfer getTransferById(int transferId);

    List<Transfer> getTransfersForAccount(int accountId);

    List<Transfer> getTransfersForUser(String username);

    void updateTransferStatus(int transferId, boolean isApproved);
}
