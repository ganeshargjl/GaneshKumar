package com.retail.reward.application.controllertest;


import com.retail.reward.application.controller.RewardsController;
import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.repository.CustomerRepository;
import com.retail.reward.application.service.RewardsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsServiceImpl rewardsService;
    
    @MockBean
    private CustomerRepository customerRepository;  // Mocking the repository
    
    @InjectMocks
    private RewardsController rewardsController;

    @Test
    public void testGetRewardsForCustomers() throws Exception {
        // Given
        List<RewardPointsResponse> responses = List.of(
            new RewardPointsResponse(1001L, List.of(new RewardPointsResponse.MonthRewardPoints("August", 150L)), 150L),
            new RewardPointsResponse(1002L, List.of(new RewardPointsResponse.MonthRewardPoints("August", 200L)), 200L)
        );
        
        when(rewardsService.calculateRewardsForListCustomers(List.of(1001L, 1002L))).thenReturn(responses);

        // When
        mockMvc.perform(post("/api/rewards/listTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1001,1002]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1001))
                .andExpect(jsonPath("$[0].monthlyRewards[0].monthName").value("August"))
                .andExpect(jsonPath("$[0].monthlyRewards[0].rewardPoints").value(150))
                .andExpect(jsonPath("$[0].totalRewards").value(150))
                .andExpect(jsonPath("$[1].customerId").value(1002))
                .andExpect(jsonPath("$[1].monthlyRewards[0].monthName").value("August"))
                .andExpect(jsonPath("$[1].monthlyRewards[0].rewardPoints").value(200))
                .andExpect(jsonPath("$[1].totalRewards").value(200));
    }
}
