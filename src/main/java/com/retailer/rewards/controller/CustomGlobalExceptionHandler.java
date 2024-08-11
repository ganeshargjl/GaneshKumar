package com.retailer.rewards.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.retailer.rewards.exceptions.CustomErrorResponse;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler  
 
	{

	    @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex) {
	        CustomErrorResponse errors = new CustomErrorResponse();
	        errors.setError("Custom Internal Server Error Message");
	        errors.setMessage(ex.getMessage());

	        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

