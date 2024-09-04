package com.retail.reward.application.servicetest;

import com.retail.reward.application.dto.AddTransactionRequest;
import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.entity.Transaction;
import com.retail.reward.application.exception.CustomerNotFoundException;
import com.retail.reward.application.exception.TransactionAmountInvalidException;
import com.retail.reward.application.exception.TransactionDateInvalidException;
import com.retail.reward.application.repository.TransactionRepository;
import com.retail.reward.application.service.RewardsServiceImpl;
import com.retail.reward.application.util.DateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RewardsServiceImplTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private DateProvider dateProvider;

	@InjectMocks
	private RewardsServiceImpl rewardsService;

	private LocalDate fixedDate;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		fixedDate = LocalDate.of(2024, 9, 1);
		when(dateProvider.today()).thenReturn(fixedDate);
	}

	@Test
	void testCalculateRewardPointsCustomerNotFound() {
		Long customerId = 1L;
		when(transactionRepository.findAllByCustomerId(customerId)).thenReturn(Collections.emptyList());

		assertThrows(CustomerNotFoundException.class, () -> rewardsService.calculateRewardPoints(customerId));
	}

	@Test
	void testCalculateRewardPointsSuccess() {
		Long customerId = 1L;
		Transaction transaction1 = new Transaction(1L, customerId, 150.0, LocalDate.of(2024, 8, 15));
		Transaction transaction2 = new Transaction(2L, customerId, 80.0, LocalDate.of(2024, 9, 1));
		List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

		when(transactionRepository.findAllByCustomerId(customerId)).thenReturn(transactions);

		RewardPointsResponse response = rewardsService.calculateRewardPoints(customerId);

		assertNotNull(response);
		assertEquals(customerId, response.getCustomerId());
		assertEquals(180, response.getTotalRewards()); // (150-100)*2 + 50 + (80-50) = 100 + 30 = 180

		// Check that monthly reward points are calculated correctly
		assertEquals(2, response.getMonthlyRewards().size());
		assertTrue(response.getMonthlyRewards().stream()
				.anyMatch(rp -> rp.getMonthName().equals("August 2024") && rp.getRewardPoints() == 150));
		assertTrue(response.getMonthlyRewards().stream()
				.anyMatch(rp -> rp.getMonthName().equals("September 2024") && rp.getRewardPoints() == 30));
	}

	@Test
	void testCalculateRewardsForAllCustomers() {
		Long customerId1 = 1L;
		Long customerId2 = 2L;
		Transaction transaction1 = new Transaction(1L, customerId1, 150.0, LocalDate.of(2024, 8, 15));
		Transaction transaction2 = new Transaction(2L, customerId2, 80.0, LocalDate.of(2024, 9, 1));
		List<Transaction> transactions1 = Collections.singletonList(transaction1);
		List<Transaction> transactions2 = Collections.singletonList(transaction2);

		when(transactionRepository.findTotalAmountGroupedByCustomerId())
				.thenReturn(Arrays.asList(new Object[] { customerId1, 150.0 }, new Object[] { customerId2, 80.0 }));
		when(transactionRepository.findAllByCustomerId(customerId1)).thenReturn(transactions1);
		when(transactionRepository.findAllByCustomerId(customerId2)).thenReturn(transactions2);

		List<RewardPointsResponse> responses = rewardsService.calculateRewardsForAllCustomers();

		assertNotNull(responses);
		assertEquals(2, responses.size());
		assertTrue(
				responses.stream().anyMatch(r -> r.getCustomerId().equals(customerId1) && r.getTotalRewards() == 150));
		assertTrue(
				responses.stream().anyMatch(r -> r.getCustomerId().equals(customerId2) && r.getTotalRewards() == 30));
	}

	@Test
	void testCalculateRewardsForListCustomers() {
		Long customerId1 = 1L;
		Long customerId2 = 2L;
		Transaction transaction1 = new Transaction(1L, customerId1, 150.0, LocalDate.of(2024, 8, 15));
		Transaction transaction2 = new Transaction(2L, customerId2, 80.0, LocalDate.of(2024, 9, 1));
		List<Transaction> transactions1 = Collections.singletonList(transaction1);
		List<Transaction> transactions2 = Collections.singletonList(transaction2);

		when(transactionRepository.findAllByCustomerId(customerId1)).thenReturn(transactions1);
		when(transactionRepository.findAllByCustomerId(customerId2)).thenReturn(transactions2);

		List<RewardPointsResponse> responses = rewardsService
				.calculateRewardsForListCustomers(Arrays.asList(customerId1, customerId2));

		assertNotNull(responses);
		assertEquals(2, responses.size());
		assertTrue(
				responses.stream().anyMatch(r -> r.getCustomerId().equals(customerId1) && r.getTotalRewards() == 150));
		assertTrue(
				responses.stream().anyMatch(r -> r.getCustomerId().equals(customerId2) && r.getTotalRewards() == 30));
	}

	@Test
	void testAddTransactionsInvalidCustomer() {
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2024, 9, 1));
		when(transactionRepository.findAllByCustomerId(request.getCustomerId())).thenReturn(Collections.emptyList());

		assertThrows(CustomerNotFoundException.class,
				() -> rewardsService.addTransactions(Collections.singletonList(request)));
	}

	@Test
	void testAddTransactionsInvalidAmount() {
		AddTransactionRequest request = new AddTransactionRequest(1L, -10.0, LocalDate.of(2024, 9, 1));
		when(transactionRepository.findAllByCustomerId(request.getCustomerId()))
				.thenReturn(Collections.singletonList(new Transaction()));

		assertThrows(TransactionAmountInvalidException.class,
				() -> rewardsService.addTransactions(Collections.singletonList(request)));
	}

	@Test
	void testAddTransactionsInvalidDate() {
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2025, 1, 1)); // Future date
		when(transactionRepository.findAllByCustomerId(request.getCustomerId()))
				.thenReturn(Collections.singletonList(new Transaction()));

		assertThrows(TransactionDateInvalidException.class,
				() -> rewardsService.addTransactions(Collections.singletonList(request)));
	}

	@Test
	void testAddTransactionsSuccess() {
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2024, 9, 1));
		when(transactionRepository.findAllByCustomerId(request.getCustomerId()))
				.thenReturn(Collections.singletonList(new Transaction()));

		when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
			Transaction transaction = invocation.getArgument(0);
			transaction.setTransactionId(1L); // Simulate ID generation
			return transaction;
		});

		List<Long> transactionIds = rewardsService.addTransactions(Collections.singletonList(request));

		assertNotNull(transactionIds);
		assertEquals(1, transactionIds.size());
		assertEquals(1L, transactionIds.get(0));
	}
}
