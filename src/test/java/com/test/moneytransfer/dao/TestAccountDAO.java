package com.test.moneytransfer.dao;

import com.moneytransfer.dao.MasterDAO;
import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.Account;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TestAccountDAO {

	private static final MasterDAO h2DaoFactory = MasterDAO.getDAOFactory(MasterDAO.H2);

	@BeforeClass
	public static void setup() {
		// prepare test database and test data. Test data are initialised from
		// src/test/resources/demo.sql
		h2DaoFactory.populateTestData();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGetAllAccounts() throws RevolutException {
		List<Account> allAccounts = h2DaoFactory.getAccountDAO().getAllAccounts();
		assertTrue(allAccounts.size() > 1);
	}

	@Test
	public void testGetAccountById() throws RevolutException {
		Account account = h2DaoFactory.getAccountDAO().getAccountById(1L);
		assertTrue(account.getUserName().equals("test1"));
	}

	@Test
	public void testGetNonExistingAccById() throws RevolutException {
		Account account = h2DaoFactory.getAccountDAO().getAccountById(100L);
		assertTrue(account == null);
	}

	@Test
	public void testCreateAccount() throws RevolutException {
		BigDecimal balance = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		Account a = new Account("test2", balance, "CNY");
		long aid = h2DaoFactory.getAccountDAO().createAccount(a);
		Account afterCreation = h2DaoFactory.getAccountDAO().getAccountById(aid);
		assertTrue(afterCreation.getUserName().equals("test2"));
		assertTrue(afterCreation.getCurrencyCode().equals("CNY"));
		assertTrue(afterCreation.getBalance().equals(balance));
	}

	@Test
	public void testDeleteAccount() throws RevolutException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(2L);
		assertTrue(rowCount == 1);
		assertTrue(h2DaoFactory.getAccountDAO().getAccountById(2L) == null);
	}

	@Test
	public void testDeleteNonExistingAccount() throws RevolutException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(500L);
		assertTrue(rowCount == 0);

	}

	@Test
	public void testUpdateAccountBalanceSufficientFund() throws RevolutException {

		BigDecimal deltaDeposit = new BigDecimal(50).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal afterDeposit = new BigDecimal(150).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdated = h2DaoFactory.getAccountDAO().updateAccountBalance(1L, deltaDeposit);
		assertTrue(rowsUpdated == 1);
		assertTrue(h2DaoFactory.getAccountDAO().getAccountById(1L).getBalance().equals(afterDeposit));
		BigDecimal deltaWithDraw = new BigDecimal(-50).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal afterWithDraw = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdatedW = h2DaoFactory.getAccountDAO().updateAccountBalance(1L, deltaWithDraw);
		assertTrue(rowsUpdatedW == 1);
		assertTrue(h2DaoFactory.getAccountDAO().getAccountById(1L).getBalance().equals(afterWithDraw));

	}

	@Test(expected = RevolutException.class)
	public void testUpdateAccountBalanceNotEnoughFund() throws RevolutException {
		BigDecimal deltaWithDraw = new BigDecimal(-50000).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdatedW = h2DaoFactory.getAccountDAO().updateAccountBalance(1L, deltaWithDraw);
		assertTrue(rowsUpdatedW == 0);

	}

}