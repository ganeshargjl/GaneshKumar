package com.retail.reward.application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table (name="TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column (name="TRANSACTION_ID")
	private Long transactionId;
	
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	
	@Column (name="TRANSACTION_DATE")
	@Temporal(TemporalType.DATE)
	private LocalDate transactionDate;
	
	@Column(name="AMOUNT")
	private double transactionAmount;
	

}
