package com.moneytransfer.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import com.moneytransfer.dao.impl.AccountDAOImpl;
import com.moneytransfer.dao.impl.UserDAOImpl;
import com.moneytransfer.utils.Utility;

public class H2DatabaseDAO extends MasterDAO {

	private static Logger log = Logger.getLogger(H2DatabaseDAO.class);
	
	private static final String h2_driver = Utility.getStringProperty("h2_driver");
	private static final String h2_connection_url = Utility.getStringProperty("h2_connection_url");
	private static final String h2_user = Utility.getStringProperty("h2_user");
	private static final String h2_password = Utility.getStringProperty("h2_password");
	

	private final UserDAOImpl userDAO = new UserDAOImpl();
	private final AccountDAOImpl accountDAO = new AccountDAOImpl();

	H2DatabaseDAO() {
		// init: load driver
		DbUtils.loadDriver(h2_driver);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);

	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	@Override
	public void populateTestData() {
		log.info("Populating Test User Table and data ..... ");
		Connection conn = null;
		try {
			conn = H2DatabaseDAO.getConnection();
			RunScript.execute(conn, new FileReader("src/test/resources/demo.sql"));
		} catch (SQLException e) {
			log.error("populateTestData(): Error populating user data: ", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			log.error("populateTestData(): Error finding test script file ", e);
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	
}
