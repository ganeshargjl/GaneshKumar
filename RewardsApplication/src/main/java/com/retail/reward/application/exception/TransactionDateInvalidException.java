package com.retail.reward.application.exception;

import java.time.LocalDate;

public class TransactionDateInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L; // Declare serialVersionUID

	public TransactionDateInvalidException(LocalDate transactionDate) {
		super("Transaction date cannot be greater than today's date. Provided date: " + transactionDate);
	}
}
