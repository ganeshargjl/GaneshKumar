package com.retail.reward.application.service;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Transaction;
import com.retail.reward.application.repository.TransactionRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardsServiceImpl implements RewardsService{

	@Autowired
    TransactionRepository transactionRepository;

	public RewardPointsResponse calculateLastThreeMonthsRewardsForCustomer(Long customerId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(3).withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(1).minusDays(1); // End of last month

        // Fetch transactions for the last three months
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(
                customerId, startDate, endDate
        );

        // Group transactions by month and calculate rewards
        Map<String, Long> monthlyRewards = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> {
                            LocalDate date = transaction.getTransactionDate();
                            return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear();
                        },
                        Collectors.summingLong(this::calculateRewardPoints)
                ));

        // Create a list of MonthRewardPoints
        List<RewardPointsResponse.MonthRewardPoints> monthRewardPointsList = Arrays.asList(
                new RewardPointsResponse.MonthRewardPoints(
                        now.minusMonths(1).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(1).getYear(),
                        monthlyRewards.getOrDefault(now.minusMonths(1).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(1).getYear(), 0L)
                ),
                new RewardPointsResponse.MonthRewardPoints(
                        now.minusMonths(2).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(2).getYear(),
                        monthlyRewards.getOrDefault(now.minusMonths(2).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(2).getYear(), 0L)
                ),
                new RewardPointsResponse.MonthRewardPoints(
                        now.minusMonths(3).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(3).getYear(),
                        monthlyRewards.getOrDefault(now.minusMonths(3).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.minusMonths(3).getYear(), 0L)
                )
        );

        // Calculate total rewards
        Long totalRewards = monthRewardPointsList.stream()
                .mapToLong(RewardPointsResponse.MonthRewardPoints::getRewardPoints)
                .sum();

        // Return the response object
        return new RewardPointsResponse(
                customerId,
                monthRewardPointsList,
                totalRewards
        );
    }

    
    private long calculateRewardPoints(Transaction transaction) {
        long points = 0;
        double amount = transaction.getTransactionAmount();

        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100L;
        }

        if (amount > 50) {
            points += (amount - 50);
        }

        return points;
    }
    
}

