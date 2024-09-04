package com.retail.reward.application.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.reward.application.dto.AddTransactionRequest;
import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Transaction;
import com.retail.reward.application.exception.CustomerNotFoundException;
import com.retail.reward.application.exception.TransactionAmountInvalidException;
import com.retail.reward.application.exception.TransactionDateInvalidException;
import com.retail.reward.application.repository.TransactionRepository;
import com.retail.reward.application.util.DateProvider;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardsServiceImpl implements RewardsService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	DateProvider dateProvider;

	// Check if a customer exists
	public boolean customerExists(Long customerId) {
		return !transactionRepository.findAllByCustomerId(customerId).isEmpty();
	}

	// Calculate rewards for a single customer
	public RewardPointsResponse calculateRewardPoints(Long customerId) {
		if (!customerExists(customerId)) {
			throw new CustomerNotFoundException(customerId);
		}
		List<Transaction> transactions = transactionRepository.findAllByCustomerId(customerId);
		if (transactions.isEmpty()) {
			throw new CustomerNotFoundException(customerId); // Ensure this exception is handled properly in tests
		}
		List<RewardPointsResponse.MonthRewardPoints> monthlyRewardPoints = calculateMonthlyRewardPoints(transactions);
		Long totalRewards = monthlyRewardPoints.stream()
				.mapToLong(RewardPointsResponse.MonthRewardPoints::getRewardPoints).sum();

		return new RewardPointsResponse(customerId, monthlyRewardPoints, totalRewards);
	}

	// Calculate rewards for all customers
	public List<RewardPointsResponse> calculateRewardsForAllCustomers() {

		List<RewardPointsResponse> rewardPointsResponses = new ArrayList<>();
		List<Object[]> customerTotalAmounts = transactionRepository.findTotalAmountGroupedByCustomerId();

		for (Object[] customerData : customerTotalAmounts) {
			Long customerId = (Long) customerData[0];
			// No need to check existence here because it's being aggregated
			RewardPointsResponse rewardPointsResponse = calculateRewardPoints(customerId);
			rewardPointsResponses.add(rewardPointsResponse);
		}

		return rewardPointsResponses;
	}

	// Calculate rewards for specific customers
	public List<RewardPointsResponse> calculateRewardsForListCustomers(List<Long> customerIds) {
		List<RewardPointsResponse> rewardPointsResponses = new ArrayList<>();
		for (Long customerId : customerIds) {
			if (!customerExists(customerId)) {
				throw new CustomerNotFoundException(customerId);
			}
			RewardPointsResponse rewardPointsResponse = calculateRewardPoints(customerId);
			rewardPointsResponses.add(rewardPointsResponse);
		}
		return rewardPointsResponses;
	}

	// Helper method to calculate rewards by month
	private List<RewardPointsResponse.MonthRewardPoints> calculateMonthlyRewardPoints(List<Transaction> transactions) {
		Map<String, Long> rewardsByMonth = new HashMap<>();

		for (Transaction transaction : transactions) {
			String monthName = transaction.getTransactionDate().getMonth().getDisplayName(TextStyle.FULL,
					Locale.ENGLISH) + " " + transaction.getTransactionDate().getYear();
			Long points = calculatePoints(transaction.getTransactionAmount());
//			rewardsByMonth.put(monthName, rewardsByMonth.getOrDefault(monthName, 0L) + points);
			rewardsByMonth.merge(monthName, points, Long::sum);
		}

		return rewardsByMonth.entrySet().stream()
				.map(entry -> new RewardPointsResponse.MonthRewardPoints(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	// adds extra transaction for the existing customer with customer id
	public List<Long> addTransactions(List<AddTransactionRequest> requests) {
		List<Long> transactionIds = new ArrayList<>();
		LocalDate today = LocalDate.now();
		for (AddTransactionRequest request : requests) {

			if (!customerExists(request.getCustomerId()) || request.getCustomerId() == null) {
				throw new CustomerNotFoundException(request.getCustomerId());
			}
			if (request.getTransactionAmount() <= 0) {

				throw new TransactionAmountInvalidException(request.getTransactionAmount());
			}
			if (request.getTransactionDate().isAfter(today) || request.getTransactionDate() == null) {
				throw new TransactionDateInvalidException(request.getTransactionDate());
			}
			System.out.println("Saving transaction for customerId: " + request.getCustomerId());
			Transaction transaction = new Transaction();
			transaction.setCustomerId(request.getCustomerId());
			transaction.setTransactionAmount(request.getTransactionAmount());
			transaction.setTransactionDate(request.getTransactionDate());

			transactionRepository.save(transaction);
			transactionIds.add(transaction.getTransactionId());
		}
		return transactionIds;
	}

	// Helper method to calculate points based on the transaction amount
	private Long calculatePoints(Double amount) {
		Long points = 0L;
		if (amount > 100) {
			points += (amount.intValue() - 100) * 2;
			points += 50; // for the 50-100 range
		} else if (amount > 50) {
			points += (amount.intValue() - 50);
		}
		return points;
	}
}
