package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao
{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account createAccount(int userId)
    {
        String sql = "INSERT INTO account (user_id, balance)\n" +
                "VALUES (?, 1000.00)\n" +
                "RETURNING account_id;";

        Integer newAccountId;
        try
        {
            newAccountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        }
        catch (DataAccessException e)
        {
            return null;
        }

        return new Account();
    }

    public Account getAccountById(int accountId)
    {
        String sql = "";

        try
        {

        }
        catch (DataAccessException e)
        {
            return null;
        }

        return new Account();
    }

    private Account mapRowToAccount(SqlRowSet rowSet)
    {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
