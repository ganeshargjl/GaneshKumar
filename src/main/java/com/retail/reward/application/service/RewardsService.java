package com.retail.reward.application.service;

import com.retail.reward.application.dto.RewardPointsResponse;

public interface RewardsService {
	public RewardPointsResponse calculateLastThreeMonthsRewardsForCustomer(Long customerId);
}
