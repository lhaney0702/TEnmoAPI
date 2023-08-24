package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcTransferDao
{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transfer createTransfer(int senderAccountId, int recipientAccountId, BigDecimal transferAmount, String transferType)
    {
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
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountId, recipientAccountId, transferAmount, transferType);
        }
        catch (DataAccessException e)
        {
            return null;
        }

        return new Transfer();
    }

    public Transfer getTransferById(int transferId)
    {
        String sql = "";

        try
        {

        }
        catch (DataAccessException e)
        {
            return null;
        }

        return new Transfer();
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
