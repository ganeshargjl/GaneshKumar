package com.retail.reward.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.reward.application.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

		public Customer findByCustomerId(Long customerId);
	}

