package com.retail.reward.application.service;

import java.util.List;

import com.retail.reward.application.dto.AddTransactionRequest;
import com.retail.reward.application.dto.RewardPointsResponse;

public interface RewardsService {
	public RewardPointsResponse calculateRewardPoints(Long customerId);

	public List<RewardPointsResponse> calculateRewardsForAllCustomers();

	public List<RewardPointsResponse> calculateRewardsForListCustomers(List<Long> customerIds);

	public List<Long> addTransactions(List<AddTransactionRequest> requests);

}
