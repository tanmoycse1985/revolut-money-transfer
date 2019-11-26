package com.moneytransfer.exception;

public class RevolutException extends Exception {

	private static final long serialVersionUID = 1L;

	public RevolutException(String msg) {
		super(msg);
	}

	public RevolutException(String msg, Throwable cause) {
		super(msg, cause);
	}
}