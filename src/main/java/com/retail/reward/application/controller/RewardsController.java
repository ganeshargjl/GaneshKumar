package com.retail.reward.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.repository.CustomerRepository;
import com.retail.reward.application.service.RewardsService;


@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardPointsService;
    
	@Autowired
	CustomerRepository customerRepository;

    // Get rewards for a single customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<RewardPointsResponse> getRewardsForCustomer(@PathVariable Long customerId) {
        RewardPointsResponse rewards = rewardPointsService.calculateRewardPoints(customerId);
        return ResponseEntity.ok(rewards);
    }

    // Get rewards for all customers
    @GetMapping("/allcustomers")
    public ResponseEntity<List<RewardPointsResponse>> getRewardsForAllCustomers() {
        
          List<RewardPointsResponse> rewards = rewardPointsService.calculateRewardsForAllCustomers();
        return ResponseEntity.ok(rewards);
    }

    // Get rewards for specific customers
    @PostMapping("/listTransaction")
    public ResponseEntity<List<RewardPointsResponse>> getRewardsForCustomers(@RequestBody List<Long> customerIds) {
         List<RewardPointsResponse> rewards = rewardPointsService.calculateRewardsForListCustomers(customerIds);
        return ResponseEntity.ok(rewards);
    }
}
