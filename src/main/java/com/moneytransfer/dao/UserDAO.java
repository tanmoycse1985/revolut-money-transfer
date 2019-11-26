package com.moneytransfer.dao;

import java.util.List;

import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.User;

public interface UserDAO {
	
	long insertUser(User user) throws RevolutException;

	int updateUser(Long userId, User user) throws RevolutException;

	int deleteUser(long userId) throws RevolutException;

	List<User> getAllUsers() throws RevolutException;

	User getUserById(long userId) throws RevolutException;

	User getUserByName(String userName) throws RevolutException;

	
}
