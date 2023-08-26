package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType)
    {
        if (senderAccountId == recipientAccountId)
        {
            throw new DaoException("User can't send or receive to own account.");
        }
        if (transferAmount.doubleValue() <= 0)
        {
            throw new DaoException("Transfer amount can't be less than or equal to zero.");
        }
        if (transferType.equals("Sent"))
        {
            JdbcAccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
            BigDecimal currentBalance = accountDao.getCurrentBalance(senderAccountId);
            if (currentBalance.doubleValue() < transferAmount.doubleValue())
            {
                throw new DaoException("Declined. Insufficient funds.");
            }
        }

        String sql = "INSERT INTO transfer (sender_account_id, recipient_account_id, transfer_amount, transfer_date, transfer_status, transfer_type)\n" +
                "VALUES (?, ?, ?, NOW(), ?, ?)\n" +
                "RETURNING transfer_id;";

        Integer newTransferId;
        String transferStatus;
        if (transferType.equals("Requested"))
        {
            transferStatus = "Pending";
        }
        else
        {
            transferStatus = "Approved";
        }
        try
        {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountId, recipientAccountId, transferAmount, transferStatus, transferType);
            return getTransferById(newTransferId);
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    public void updateTransferStatus(int transferId, boolean isApproved)
    {
        String transferStatus = isApproved ? "Approved" : "Rejected";
        String sql = "UPDATE transfer\n" +
                "SET transfer_status = ?\n" +
                "WHERE transfer_id = ?;";

        int numberOfRowsUpdated = jdbcTemplate.update(sql, transferStatus, transferId);
        if (numberOfRowsUpdated == 0)
        {
            throw new DataRetrievalFailureException("Zero rows affected, expected at least one.");
        }
    }

    @Override
    public Transfer getTransferById(int transferId)
    {
        String sql = "SELECT transfer_id, sender_account_id, recipient_account_id, transfer_amount, transfer_date, transfer_status, transfer_type\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = ?;";

        try
        {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if (rowSet.next())
            {
                return mapRowToTransfer(rowSet);
            }
            throw new DataRetrievalFailureException("Transfer not found.");
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    @Override
    public List<Transfer> getTransfersForAccount(int accountId)
    {
        List<Transfer> accountTransfers = new ArrayList<>();

        String sql = "SELECT transfer_id, sender_account_id, recipient_account_id, transfer_amount, transfer_date, transfer_status, transfer_type\n" +
                "FROM transfer\n" +
                "WHERE account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next())
        {
            Transfer transfer = mapRowToTransfer(results);
            accountTransfers.add(transfer);
        }

        return accountTransfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet)
    {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setSenderAccountId(rowSet.getInt("sender_account_id"));
        transfer.setRecipientAccountId(rowSet.getInt("recipient_account_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("transfer_amount"));
        transfer.setTransferDate(rowSet.getDate("transfer_date"));
        transfer.setTransferStatus(rowSet.getString("transfer_status"));
        transfer.setTransferType(rowSet.getString("transfer_type"));
        return transfer;
    }
}
