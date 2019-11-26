package com.moneytransfer.dao;

public abstract class MasterDAO {

	public static final int H2 = 1;

	public abstract UserDAO getUserDAO();

	public abstract AccountDAO getAccountDAO();

	public abstract void populateTestData();

	public static MasterDAO getDAOFactory(int factoryCode) {

		switch (factoryCode) {
		case H2:
			return new H2DatabaseDAO();
		default:
			return new H2DatabaseDAO();
		}
	}
}
