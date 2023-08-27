package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UserBalanceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class AccountController
{
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao)
    {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    // create account
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/accounts/create", method = RequestMethod.POST)
    public Account createAccountForUser(@Valid @RequestBody int userId)
    {
        return accountDao.createAccount(userId);
    }

    // get account by account ID
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable int accountId)
    {
        Account account = accountDao.getAccountById(accountId);
        if (account == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
        else
        {
            return account;
        }
    }

    // get accounts for user
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/user/accounts", method = RequestMethod.GET)
    public List<Account> getAccountsForUser(Principal principal)
    {
        List<Account> accounts = accountDao.getAccountsForUser(userDao.findIdByUsername(principal.getName()));
        if (accounts == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account(s) not found.");
        }
        else
        {
            return accounts;
        }
    }

    // get current balance
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/accounts/balance", method = RequestMethod.GET)
    public UserBalanceDTO getCurrentBalance(Principal principal)
    {
        BigDecimal userBalance = userDao.getBalanceForUser(principal.getName());
        return new UserBalanceDTO(principal.getName(), userBalance);
    }
}
