package com.retail.reward.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "TRANSACTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
	@SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
	@Column(name = "transaction_id")
	private Long transactionId;

	@Column(name = "CUSTOMER_ID")
	private Long customerId;

	@Column(name = "AMOUNT")
	private double transactionAmount;

	@Column(name = "TRANSACTION_DATE")
	private LocalDate transactionDate;

}
