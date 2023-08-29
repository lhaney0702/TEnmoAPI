package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests
{
    private JdbcAccountDao dao;
    private Account testAccount;

    private final Account ACCOUNT_1 = new Account(2001, 1, BigDecimal.valueOf(1000.00));
    private final Account ACCOUNT_2 = new Account(2002, 1, BigDecimal.valueOf(1000.00));

    private final Account ACCOUNT_3 = new Account(2003, 2, BigDecimal.valueOf(1000.00));
    private final Account ACCOUNT_4 = new Account(2004, 2, BigDecimal.valueOf(1000.00));

    @Before
    public void setup()
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);

        testAccount = new Account(5, 1, BigDecimal.valueOf(1000.00));
        testAccount = new Account(6, 2, BigDecimal.valueOf(1000.00));

    }

    @Test
    public void createAccount_returns_new_account_ID()
    {
        //Arrange
        Account createdAccount = dao.createAccount(testAccount.getUserId());

        int newAccountID = createdAccount.getAccountId();
        Assert.assertTrue(newAccountID > 0);

        Account retrievedAccount = dao.getAccountById(newAccountID);
        assertAccountsMatch(createdAccount, retrievedAccount);
    }

    @Test
    public void getAccountById_returns_account_information_by_account_ID()
    {
        Account account = dao.getAccountById(1);
        assertAccountsMatch(ACCOUNT_1, account);

        Account account2 = dao.getAccountById(2);
        assertAccountsMatch(ACCOUNT_2, account2);
    }

    @Test
    public void getAccountById_returns_null_when_ID_not_found()
    {
        Account account = dao.getAccountById(99);
        Assert.assertNull(account);

        Account account2 = dao.getAccountById(7);
        Assert.assertNull(account2);
    }

    @Test
    public void getAccountsForUser_returns_list_of_accounts_by_user_ID()
    {
        List<Account> accounts = dao.getAccountsForUser(1);
        Assert.assertEquals(2, accounts.size());

        assertAccountsMatch(ACCOUNT_1, accounts.get(0));
        assertAccountsMatch(ACCOUNT_2, accounts.get(1));
    }

    @Test
    public void getCurrentBalance_returns_current_balance_by_account_ID()
    {

    }

    @Test
    public void updateAccountBalance_returns_updated_value_by_account_ID()
    {}






    private void assertAccountsMatch(Account expected, Account actual)    {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

}
