package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao
{
    Account createAccount(int userId);

    Account getAccountById(int accountId);

    List<Account> getAccountsForUser(int userId);

    BigDecimal getCurrentBalance(int accountId);

    BigDecimal updateAccountBalance(int accountId, BigDecimal amountToChange);
}
