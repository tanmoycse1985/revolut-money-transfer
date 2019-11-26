package com.test.moneytransfer.services;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;


/**
 * Integration testing for RestAPI
 * Test data are initialized from src/test/resources/demo.sql
 */
public class TestTransactionService extends TestService {
    
    @Test
    public void testDeposit() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1/deposit/100").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterDeposit = mapper.readValue(jsonString, Account.class);
        assertTrue(afterDeposit.getBalance().equals(new BigDecimal(200).setScale(4, RoundingMode.HALF_EVEN)));

    }
    
    @Test
    public void testWithDrawSufficientFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/2/withdraw/100").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterDeposit = mapper.readValue(jsonString, Account.class);
        assertTrue(afterDeposit.getBalance().equals(new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN)));

    }
   
    @Test
    public void testWithDrawNonSufficientFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/2/withdraw/2000.25").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        assertTrue(statusCode == 500);
        assertTrue(responseBody.contains("Not sufficient Fund"));
    }
   
    @Test
    public void testTransactionEnoughFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
        UserTransaction transaction = new UserTransaction("EUR", amount, 3L, 4L);

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
    }
    
    @Test
    public void testTransactionNotEnoughFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(100000).setScale(4, RoundingMode.HALF_EVEN);
        UserTransaction transaction = new UserTransaction("EUR", amount, 3L, 4L);

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 500);
    }
    
}
