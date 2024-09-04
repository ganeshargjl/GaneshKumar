package com.retail.reward.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.retail.reward.application.entity.Transaction;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	public List<Transaction> findAllByCustomerId(Long customerId);

	@Query("SELECT t.customerId, SUM(t.transactionAmount) FROM Transaction t GROUP BY t.customerId")
	List<Object[]> findTotalAmountGroupedByCustomerId();
}
