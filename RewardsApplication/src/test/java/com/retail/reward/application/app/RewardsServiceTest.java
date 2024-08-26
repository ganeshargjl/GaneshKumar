package com.retail.reward.application.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Transaction;
import com.retail.reward.application.repository.TransactionRepository;
import com.retail.reward.application.service.RewardsService;
import com.retail.reward.application.service.RewardsServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RewardsServiceTest {


	    @Mock
	    private TransactionRepository transactionRepository;

	    @InjectMocks
	    private RewardsServiceImpl rewardsService;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testCalculateLastThreeMonthsRewardsForCustomer() {
	        Long customerId = 1001L;
	        LocalDate now = LocalDate.now();

	        LocalDate lastThirdMonth = now.minusMonths(3);
	        LocalDate lastSecondMonth = now.minusMonths(2);
	        LocalDate lastMonth = now.minusMonths(1);

	        // Create mock transactions
	        Transaction t1 = new Transaction(1L, customerId, lastMonth.withDayOfMonth(15),120L);
	        Transaction t2 = new Transaction(2L, customerId, lastSecondMonth.withDayOfMonth(10),200L);
	        Transaction t3 = new Transaction(3L, customerId,  lastThirdMonth.withDayOfMonth(5),80L);

	        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

	        // Mock repository response
	        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
	                customerId, lastThirdMonth.withDayOfMonth(1), now.withDayOfMonth(1).minusDays(1)))
	                .thenReturn(transactions);

	        // Test service method
	        RewardPointsResponse response = rewardsService.calculateLastThreeMonthsRewardsForCustomer(customerId);

	        assertEquals(customerId, response.getCustomerId());
	        assertEquals(3, response.getMonthlyRewards().size());
	        assertEquals("August 2024", response.getMonthlyRewards().get(0).getMonthName()); // Adjust month based on current month
	        assertEquals(40, response.getMonthlyRewards().get(0).getRewardPoints());
	        assertEquals("July 2024", response.getMonthlyRewards().get(1).getMonthName()); // Adjust month based on current month
	        assertEquals(295, response.getMonthlyRewards().get(1).getRewardPoints());
	        assertEquals("June 2024", response.getMonthlyRewards().get(2).getMonthName()); // Adjust month based on current month
	        assertEquals(30, response.getMonthlyRewards().get(2).getRewardPoints());
	        assertEquals(365, response.getTotalRewards());
	    }
	}
