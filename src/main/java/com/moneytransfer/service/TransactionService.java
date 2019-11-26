package com.moneytransfer.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.moneytransfer.dao.MasterDAO;
import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.MoneyUtil;
import com.moneytransfer.model.UserTransaction;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

	private final MasterDAO masterDAO = MasterDAO.getDAOFactory(MasterDAO.H2);
	
	/**
	 * Transfer fund between two accounts.
	 * @param transaction
	 * @return Response
	 * @throws RevolutException
	 */
	@POST
	public Response transferFund(UserTransaction transaction) throws RevolutException {

		String currency = transaction.getCurrencyCode();
		if (MoneyUtil.INSTANCE.validateCcyCode(currency)) {
			int updateCount = masterDAO.getAccountDAO().transferAccountBalance(transaction);
			if (updateCount == 2) {
				return Response.status(Response.Status.OK).build();
			} else {
				// transaction failed
				throw new WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST);
			}
		} else {
			throw new WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST);
		}

	}

}
