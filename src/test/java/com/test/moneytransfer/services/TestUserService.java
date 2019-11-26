package com.test.moneytransfer.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.moneytransfer.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;


/**
 * Integration testing for RestAPI
 * Test data are initialized from src/test/resources/demo.sql
 */
public class TestUserService extends TestService {

    @Test
    public void testGetUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/test1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        User user = mapper.readValue(jsonString, User.class);
        assertTrue(user.getUserName().equals("test1"));
        assertTrue(user.getEmailAddress().equals("test001@gmail.com"));

    }

    @Test
    public void testGetAllUsers() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        User[] users = mapper.readValue(jsonString, User[].class);
        assertTrue(users.length > 0);
    }

    @Test
    public void testCreateUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/create").build();
        User user = new User("tester", "tester@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        User uAfterCreation = mapper.readValue(jsonString, User.class);
        assertTrue(uAfterCreation.getUserName().equals("tester"));
        assertTrue(uAfterCreation.getEmailAddress().equals("tester@gmail.com"));
    }

    @Test
    public void testCreateExistingUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/create").build();
        User user = new User("test1", "test1@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 400);

    }

    @Test
    public void testUpdateUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/2").build();
        User user = new User(2L, "test1", "test1123@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
    }


    @Test
    public void testUpdateNonExistingUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/100").build();
        User user = new User(2L, "test1", "test1123@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 404);
    }

    @Test
    public void testDeleteUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/3").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
    }


    @Test
    public void testDeleteNonExistingUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/300").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 404);
    }


}
