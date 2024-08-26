package com.retail.reward.application.servicetest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Transaction;
import com.retail.reward.application.repository.TransactionRepository;
import com.retail.reward.application.service.RewardsServiceImpl;

import java.time.LocalDate;
import java.util.List;


import static org.mockito.Mockito.when;
import com.retail.reward.application.exception.CustomerNotFoundException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RewardsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
  
    @InjectMocks
    private RewardsServiceImpl rewardsService;

    public RewardsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateRewardPointsForExistingCustomer() {
        // Given
        List<Transaction> transactions = List.of(
            new Transaction(1L, 1001L, LocalDate.of(2024, 8, 15), 150.0),
            new Transaction(2L, 1001L, LocalDate.of(2024, 8, 25), 200.0)
        );
        when(transactionRepository.findAllByCustomerId(1001L)).thenReturn(transactions);

        // When
        RewardPointsResponse response = rewardsService.calculateRewardPoints(1001L);

        // Then
        assertThat(response.getCustomerId()).isEqualTo(1001L);
        assertThat(response.getTotalRewards()).isGreaterThan(0);
    }

    @Test
    public void testCalculateRewardPointsForNonExistingCustomer() {
        // Given
        when(transactionRepository.findAllByCustomerId(1002L)).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> rewardsService.calculateRewardPoints(1002L))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("Customer with ID 1002 not found.");
    }
    @Test
    public void testCalculateRewardsForAllCustomers() {
        // Given
        List<Object[]> customerData = List.of(
            new Object[]{1001L},
            new Object[]{1002L}
        );
        when(transactionRepository.findTotalAmountGroupedByCustomerId()).thenReturn(customerData);

        // Mock findByCustomerId
        when(transactionRepository.findAllByCustomerId(1001L)).thenReturn(List.of(new Transaction(1L, 1001L, LocalDate.now(), 150.0)));
        when(transactionRepository.findAllByCustomerId(1002L)).thenReturn(List.of(new Transaction(2L, 1002L, LocalDate.now(), 200.0)));

        // When
        List<RewardPointsResponse> responses = rewardsService.calculateRewardsForAllCustomers();

        // Then
        assertThat(responses).hasSize(2);
    }
}
