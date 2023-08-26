package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;

public class AccountController
{
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    // create account
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Account createAccountByInputID(@RequestBody int userId)
    {
        return accountDao.createAccount(userId);
    }

    // get account by account ID
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Account findAccountByAccountId(@RequestBody int accountId)
    {
        return accountDao.getAccountById(accountId);
    }

    // get accounts by user ID
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> findAccountsByUserId(@RequestBody int userId)
    {
        return accountDao.getAccountsForUser(userId);
    }

    // get current bal
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public BigDecimal getCurrentBalanceByAccountId(int accountId)
    {
        return accountDao.getCurrentBalance(accountId);
    }

    // update bal
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public BigDecimal updateAccountBalanceByAccountIdAndMoneyInput(int accountId, BigDecimal amountToChange)
    {
        return accountDao.updateAccountBalance(accountId, amountToChange);
    }


}
