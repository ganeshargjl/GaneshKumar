package com.retail.reward.application.exception;

public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;  // Declare serialVersionUID

    public CustomerNotFoundException(Long customerId) {
        super("Customer with ID " + customerId + " not found.");
    }
}

