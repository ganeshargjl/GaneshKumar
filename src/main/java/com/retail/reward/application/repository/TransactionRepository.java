package com.retail.reward.application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.reward.application.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
		public List<Transaction> findAllByCustomerId(Long customerId);
		public List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
	}
