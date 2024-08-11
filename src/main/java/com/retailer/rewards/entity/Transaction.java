package com.retailer.rewards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table (name="TRANSACTION")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column (name="TRANSACTION_ID")
	private Long transactionId;
	
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	
	@Column (name="TRANSACTION_DATE")
	private Timestamp transactionDate;
	
	@Column(name="AMOUNT")
	private double transactionAmount;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	
	

}
