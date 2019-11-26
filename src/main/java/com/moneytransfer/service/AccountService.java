package com.moneytransfer.service;

import com.moneytransfer.dao.MasterDAO;
import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.MoneyUtil;

import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Account Service 
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {
	
    private final MasterDAO daoFactory = MasterDAO.getDAOFactory(MasterDAO.H2);
    
    private static Logger log = Logger.getLogger(AccountService.class);

    
    /**
     * Find all accounts
     * @return
     * @throws RevolutException
     */
    @GET
    @Path("/all")
    public List<Account> getAllAccounts() throws RevolutException {
        return daoFactory.getAccountDAO().getAllAccounts();
    }

    /**
     * Find by account id
     * @param accountId
     * @return
     * @throws RevolutException
     */
    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") long accountId) throws RevolutException {
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }
    
    /**
     * Find balance by account Id
     * @param accountId
     * @return
     * @throws RevolutException
     */
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") long accountId) throws RevolutException {
        final Account account = daoFactory.getAccountDAO().getAccountById(accountId);

        if(account == null){
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
        return account.getBalance();
    }
    
    /**
     * Create Account
     * @param account
     * @return
     * @throws RevolutException
     */
    @PUT
    @Path("/create")
    public Account createAccount(Account account) throws RevolutException {
        final long accountId = daoFactory.getAccountDAO().createAccount(account);
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }

    /**
     * Deposit amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws RevolutException
     */
    @PUT
    @Path("/{accountId}/deposit/{amount}")
    public Account deposit(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws RevolutException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }

        daoFactory.getAccountDAO().updateAccountBalance(accountId,amount.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }

    /**
     * Withdraw amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws RevolutException
     */
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    public Account withdraw(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws RevolutException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        BigDecimal delta = amount.negate();
        if (log.isDebugEnabled())
            log.debug("Withdraw service: delta change to account  " + delta + " Account ID = " +accountId);
        daoFactory.getAccountDAO().updateAccountBalance(accountId,delta.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }


    /**
     * Delete amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws RevolutException
     */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) throws RevolutException {
        int deleteCount = daoFactory.getAccountDAO().deleteAccountById(accountId);
        if (deleteCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
