package com.retail.reward.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RewardPointsResponse {
 

	private Long customerId;
    private List<MonthRewardPoints> monthlyRewards;
    private Long totalRewards;
    
    @Data
    @AllArgsConstructor
    public static class MonthRewardPoints {
        private String monthName;
        private Long rewardPoints;
    }
}



