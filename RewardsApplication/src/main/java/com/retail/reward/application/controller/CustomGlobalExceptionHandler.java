package com.retail.reward.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.retail.reward.application.exception.CustomerNotFoundException;
import com.retail.reward.application.exception.TransactionAmountInvalidException;
import com.retail.reward.application.exception.TransactionDateInvalidException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler

{

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TransactionAmountInvalidException.class)
	public ResponseEntity<String> handleTransactionAmountInvalidException(TransactionAmountInvalidException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionDateInvalidException.class)
	public ResponseEntity<String> handleTransactionDateInvalidException(TransactionDateInvalidException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
