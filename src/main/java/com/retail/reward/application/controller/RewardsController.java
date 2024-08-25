package com.retail.reward.application.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Customer;
import com.retail.reward.application.repository.CustomerRepository;
import com.retail.reward.application.service.RewardsService;



@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class RewardsController {

	@Autowired
    RewardsService rewardsService;
	
	@Autowired
	CustomerRepository customerRepository;

    @GetMapping("/{customerId}/rewards")
    public ResponseEntity<RewardPointsResponse> getLastThreeMonthsRewards(@PathVariable Long customerId) {
    	
    	Customer customer = customerRepository.findByCustomerId(customerId);
		if (customer == null)
		{
			throw new RuntimeException("Invalid/Missing Customer Id ");
		}
        RewardPointsResponse monthlyRewards = rewardsService.calculateLastThreeMonthsRewardsForCustomer(customerId);
        return ResponseEntity.ok(monthlyRewards);
    }
}
