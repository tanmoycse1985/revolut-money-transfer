package com.test.moneytransfer.dao;

import com.moneytransfer.dao.MasterDAO;
import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.User;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TestUserDAO {
	
	private static Logger log = Logger.getLogger(TestUserDAO.class);
	
	private static final MasterDAO h2DaoFactory = MasterDAO.getDAOFactory(MasterDAO.H2);

	@BeforeClass
	public static void setup() {
		// prepare test database and test data by executing sql script demo.sql
		log.debug("setting up test database and sample data....");
		h2DaoFactory.populateTestData();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGetAllUsers() throws RevolutException {
		List<User> allUsers = h2DaoFactory.getUserDAO().getAllUsers();
		assertTrue(allUsers.size() > 1);
	}

	@Test
	public void testGetUserById() throws RevolutException {
		User u = h2DaoFactory.getUserDAO().getUserById(2L);
		assertTrue(u.getUserName().equals("test2"));
	}

	@Test
	public void testGetNonExistingUserById() throws RevolutException {
		User u = h2DaoFactory.getUserDAO().getUserById(500L);
		assertTrue(u == null);
	}

	@Test
	public void testGetNonExistingUserByName() throws RevolutException {
		User u = h2DaoFactory.getUserDAO().getUserByName("TESTER");
		assertTrue(u == null);
	}

	@Test
	public void testCreateUser() throws RevolutException {
		User u = new User("test007", "test007@gmail.com");
		long id = h2DaoFactory.getUserDAO().insertUser(u);
		User uAfterInsert = h2DaoFactory.getUserDAO().getUserById(id);
		assertTrue(uAfterInsert.getUserName().equals("test007"));
		assertTrue(u.getEmailAddress().equals("test007@gmail.com"));
	}

	@Test
	public void testUpdateUser() throws RevolutException {
		User u = new User(1L, "test2", "test2@gmail.com");
		int rowCount = h2DaoFactory.getUserDAO().updateUser(1L, u);
		assertTrue(rowCount == 1);
		assertTrue(h2DaoFactory.getUserDAO().getUserById(1L).getEmailAddress().equals("test2@gmail.com"));
	}

	@Test
	public void testUpdateNonExistingUser() throws RevolutException {
		User u = new User(500L, "test2", "test2@gmail.com");
		int rowCount = h2DaoFactory.getUserDAO().updateUser(500L, u);
		assertTrue(rowCount == 0);
	}

	@Test
	public void testDeleteUser() throws RevolutException {
		int rowCount = h2DaoFactory.getUserDAO().deleteUser(1L);
		assertTrue(rowCount == 1);
		assertTrue(h2DaoFactory.getUserDAO().getUserById(1L) == null);
	}

	@Test
	public void testDeleteNonExistingUser() throws RevolutException {
		int rowCount = h2DaoFactory.getUserDAO().deleteUser(500L);
		assertTrue(rowCount == 0);

	}

}
