package com.moneytransfer.dao;

import java.math.BigDecimal;
import java.util.List;

import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;

/**
 * 
 * @author Tanmoy
 *
 */
public interface AccountDAO {

	List<Account> getAllAccounts() throws RevolutException;
    Account getAccountById(long accountId) throws RevolutException;
    long createAccount(Account account) throws RevolutException;
    int deleteAccountById(long accountId) throws RevolutException;
    int updateAccountBalance(long accountId, BigDecimal deltaAmount) throws RevolutException;
    int transferAccountBalance(UserTransaction userTransaction) throws RevolutException;
}
