package com.retail.reward.application.app;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.retail.reward.application.controller.RewardsController;
import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.service.RewardsServiceImpl;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsServiceImpl rewardsService;

    @Test
    public void testGetLastThreeMonthsRewards() throws Exception {
        Long customerId = 1001L;

        RewardPointsResponse.MonthRewardPoints firstMonth = new RewardPointsResponse.MonthRewardPoints("July 2024", 0L);
        RewardPointsResponse.MonthRewardPoints secondMonth = new RewardPointsResponse.MonthRewardPoints("June 2024", 295L);
        RewardPointsResponse.MonthRewardPoints thirdMonth = new RewardPointsResponse.MonthRewardPoints("May 2024", 130L);

        RewardPointsResponse mockResponse = new RewardPointsResponse(
                customerId,
                Arrays.asList(firstMonth, secondMonth, thirdMonth),
                425L
        );

        Mockito.when(rewardsService.calculateLastThreeMonthsRewardsForCustomer(anyLong()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/customer/{customerId}/rewards", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.monthlyRewards[0].monthName").value("July 2024"))
                .andExpect(jsonPath("$.monthlyRewards[0].rewardPoints").value(0))
                .andExpect(jsonPath("$.monthlyRewards[1].monthName").value("June 2024"))
                .andExpect(jsonPath("$.monthlyRewards[1].rewardPoints").value(295))
                .andExpect(jsonPath("$.monthlyRewards[2].monthName").value("May 2024"))
                .andExpect(jsonPath("$.monthlyRewards[2].rewardPoints").value(130))
                .andExpect(jsonPath("$.totalRewards").value(425));
    }
}
