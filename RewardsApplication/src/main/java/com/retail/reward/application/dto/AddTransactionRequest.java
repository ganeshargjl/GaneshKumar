package com.retail.reward.application.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTransactionRequest {
	private Long customerId;
	private Double transactionAmount;
	private LocalDate transactionDate;

}
