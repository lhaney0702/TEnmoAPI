package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
            return getAccountById(newAccountId);
        }
        catch (DataAccessException e)
        {
            return null;
        }

    }

    public Account getAccountById(int accountId)
    {
        String sql = "SELECT account_id, user_id, balance\n" +
                "FROM account\n" +
                "WHERE account_id = ?;";

        try
        {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next())
            {
                return mapRowToAccount(rowSet);
            }
            throw new DataRetrievalFailureException("Account not found.");
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    public List<Account> getAccountsForUser(int userId)
    {
        List<Account> userAccounts = new ArrayList<>();

        String sql = "SELECT account_id, user_id, balance\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next())
        {
            Account account = mapRowToAccount(results);
            userAccounts.add(account);
        }

        return userAccounts;
    }

    public BigDecimal getCurrentBalance(int accountId)
    {
        String sql = "SELECT balance\n" +
                "FROM account\n" +
                "WHERE account_id = ?;";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    public BigDecimal updateAccountBalance(int accountId, BigDecimal amountToChange)
    {
        String sql = "UPDATE account\n" +
                "SET balance = balance + ?\n" +
                "WHERE account_id = ?;";

        int numberOfRowsUpdated = jdbcTemplate.update(sql, amountToChange, accountId);
        if (numberOfRowsUpdated == 0)
        {
            throw new DataRetrievalFailureException("Zero rows affected, expected at least one.");
        }

        return getCurrentBalance(accountId);
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
