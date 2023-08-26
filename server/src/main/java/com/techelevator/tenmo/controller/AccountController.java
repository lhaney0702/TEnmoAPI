package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public class AccountController
{
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }



    // create account
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/accounts", method = RequestMethod.POST)
    public Account createAccountByInputID(@Valid @RequestBody int userId)
    {
        return accountDao.createAccount(userId);
    }




    // get account by account ID
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable int accountId)
    {
        Account account = accountDao.getAccountById(accountId);
        if (account == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return account;
        }
    }




    // get accounts by user ID
    @RequestMapping(path = "/users/{id}/accounts", method = RequestMethod.GET)
    public List<Account> getAccountsByUserId(@PathVariable("id") int userId)
    {
        List<Account> accounts = accountDao.getAccountsForUser(userId);
        if (accounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account(s) not found.");
        } else {
            return accounts;
        }
    }




    // get current bal
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getCurrentBalanceByAccountId(@Valid @PathVariable int accountId)
    {
        Account account = accountDao.getAccountById(accountId);
        if (account == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return account.getBalance();
        }
    }




    // update bal
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.PUT)
    public BigDecimal updateAccountBalanceByAccountIdAndMoneyInput
        (@Valid @RequestBody Account account,
        @PathVariable int accountId, BigDecimal amountToChange)
    {
        account.setAccountId(accountId);
        try {
            BigDecimal updatedAccount = accountDao.updateAccountBalance(accountId, amountToChange);
            return updatedAccount;
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
    }

}
