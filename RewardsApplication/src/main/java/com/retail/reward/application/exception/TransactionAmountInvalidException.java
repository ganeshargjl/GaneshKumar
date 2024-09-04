package com.retail.reward.application.exception;

public class TransactionAmountInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L; // Declare serialVersionUID

	public TransactionAmountInvalidException(Double transactionAmount) {
		super("Transaction Amount must be greater than 0. Provided amount :" + transactionAmount);
	}

}
