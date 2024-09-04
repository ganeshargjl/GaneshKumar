package com.retail.reward.application.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retail.reward.application.controller.RewardsController;
import com.retail.reward.application.dto.AddTransactionRequest;
import com.retail.reward.application.dto.RewardPointsResponse;
import com.retail.reward.application.exception.CustomerNotFoundException;
import com.retail.reward.application.exception.TransactionAmountInvalidException;
import com.retail.reward.application.exception.TransactionDateInvalidException;
import com.retail.reward.application.repository.CustomerRepository;
import com.retail.reward.application.service.RewardsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RewardsController.class)
//Test Case for specific Customers BulkTransaction
public class RewardsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RewardsServiceImpl rewardsService;

	@MockBean
	private CustomerRepository customerRepository; // Mocking the repository

	@InjectMocks
	private RewardsController rewardsController;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate support
	}

	@Test
//	Test Case for getRewardsForCustomer
	public void testGetRewardsForCustomer() throws Exception {
		// Given
		RewardPointsResponse response = new RewardPointsResponse(1001L,
				Arrays.asList(new RewardPointsResponse.MonthRewardPoints("May 2024", 270L),
						new RewardPointsResponse.MonthRewardPoints("June 2024", 210L),
						new RewardPointsResponse.MonthRewardPoints("July 2024", 125L)),
				605L);

		when(rewardsService.calculateRewardPoints(1001L)).thenReturn(response);

		// When & Then
		mockMvc.perform(get("/api/rewards/customer/1001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.customerId").value(1001))
				.andExpect(jsonPath("$.monthlyRewards[0].monthName").value("May 2024"))
				.andExpect(jsonPath("$.monthlyRewards[0].rewardPoints").value(270))
				.andExpect(jsonPath("$.monthlyRewards[1].monthName").value("June 2024"))
				.andExpect(jsonPath("$.monthlyRewards[1].rewardPoints").value(210))
				.andExpect(jsonPath("$.monthlyRewards[2].monthName").value("July 2024"))
				.andExpect(jsonPath("$.monthlyRewards[2].rewardPoints").value(125))
				.andExpect(jsonPath("$.totalRewards").value(605));
	}

	@Test
//	Test Case for getRewardsForAllCustomer
	public void testGetRewardsForAllCustomers() throws Exception {
		// Given
		List<RewardPointsResponse> responses = Arrays.asList(
				new RewardPointsResponse(1001L,
						Arrays.asList(new RewardPointsResponse.MonthRewardPoints("May 2024", 270L),
								new RewardPointsResponse.MonthRewardPoints("June 2024", 210L),
								new RewardPointsResponse.MonthRewardPoints("July 2024", 125L)),
						605L),
				new RewardPointsResponse(1002L,
						Arrays.asList(new RewardPointsResponse.MonthRewardPoints("August 2024", 106L),
								new RewardPointsResponse.MonthRewardPoints("June 2024", 148L),
								new RewardPointsResponse.MonthRewardPoints("July 2024", 324L)),
						578L),
				new RewardPointsResponse(1003L,
						Arrays.asList(new RewardPointsResponse.MonthRewardPoints("May 2024", 1924L),
								new RewardPointsResponse.MonthRewardPoints("June 2024", 306L),
								new RewardPointsResponse.MonthRewardPoints("July 2024", 88L)),
						2318L));

		when(rewardsService.calculateRewardsForAllCustomers()).thenReturn(responses);

		// When & Then
		mockMvc.perform(get("/api/rewards/allcustomers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].customerId").value(1001))
				.andExpect(jsonPath("$[0].monthlyRewards[0].monthName").value("May 2024"))
				.andExpect(jsonPath("$[0].monthlyRewards[0].rewardPoints").value(270))
				.andExpect(jsonPath("$[0].monthlyRewards[1].monthName").value("June 2024"))
				.andExpect(jsonPath("$[0].monthlyRewards[1].rewardPoints").value(210))
				.andExpect(jsonPath("$[0].monthlyRewards[2].monthName").value("July 2024"))
				.andExpect(jsonPath("$[0].monthlyRewards[2].rewardPoints").value(125))
				.andExpect(jsonPath("$[0].totalRewards").value(605)).andExpect(jsonPath("$[1].customerId").value(1002))
				.andExpect(jsonPath("$[1].monthlyRewards[0].monthName").value("August 2024"))
				.andExpect(jsonPath("$[1].monthlyRewards[0].rewardPoints").value(106))
				.andExpect(jsonPath("$[1].monthlyRewards[1].monthName").value("June 2024"))
				.andExpect(jsonPath("$[1].monthlyRewards[1].rewardPoints").value(148))
				.andExpect(jsonPath("$[1].monthlyRewards[2].monthName").value("July 2024"))
				.andExpect(jsonPath("$[1].monthlyRewards[2].rewardPoints").value(324))
				.andExpect(jsonPath("$[1].totalRewards").value(578)).andExpect(jsonPath("$[2].customerId").value(1003))
				.andExpect(jsonPath("$[2].monthlyRewards[0].monthName").value("May 2024"))
				.andExpect(jsonPath("$[2].monthlyRewards[0].rewardPoints").value(1924))
				.andExpect(jsonPath("$[2].monthlyRewards[1].monthName").value("June 2024"))
				.andExpect(jsonPath("$[2].monthlyRewards[1].rewardPoints").value(306))
				.andExpect(jsonPath("$[2].monthlyRewards[2].monthName").value("July 2024"))
				.andExpect(jsonPath("$[2].monthlyRewards[2].rewardPoints").value(88))
				.andExpect(jsonPath("$[2].totalRewards").value(2318));
	}

	@Test
//	Test Case for getRewardsForSpecificCustomer
	public void testGetRewardsForSpecificCustomers() throws Exception {
		// Given
		List<RewardPointsResponse> responses = Arrays.asList(
				new RewardPointsResponse(1001L,
						Arrays.asList(new RewardPointsResponse.MonthRewardPoints("August", 150L)), 150L),
				new RewardPointsResponse(1002L,
						Arrays.asList(new RewardPointsResponse.MonthRewardPoints("August", 200L)), 200L));

		when(rewardsService.calculateRewardsForListCustomers(anyList())).thenReturn(responses);

		// When
		mockMvc.perform(
				post("/api/rewards/bulkTransaction").contentType(MediaType.APPLICATION_JSON).content("[1001,1002]"))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].customerId").value(1001))
				.andExpect(jsonPath("$[0].monthlyRewards[0].monthName").value("August"))
				.andExpect(jsonPath("$[0].monthlyRewards[0].rewardPoints").value(150))
				.andExpect(jsonPath("$[0].totalRewards").value(150)).andExpect(jsonPath("$[1].customerId").value(1002))
				.andExpect(jsonPath("$[1].monthlyRewards[0].monthName").value("August"))
				.andExpect(jsonPath("$[1].monthlyRewards[0].rewardPoints").value(200))
				.andExpect(jsonPath("$[1].totalRewards").value(200));
	}

	@Test
//	 Test Case for addTransactions
	void testAddTransactionSuccess() throws Exception {
		Long transactionId = 1L;
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2024, 9, 1));
		List<AddTransactionRequest> requests = Collections.singletonList(request);
		when(rewardsService.addTransactions(requests)).thenReturn(Collections.singletonList(transactionId));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requests))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.string("Transaction added successfully with Transaction ID:[1]"));

		verify(rewardsService, times(1)).addTransactions(requests);
	}

	@Test
	void testAddTransactionInvalidCustomer() throws Exception {
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2024, 9, 1));
		List<AddTransactionRequest> requests = Collections.singletonList(request);
		when(rewardsService.addTransactions(requests)).thenThrow(new CustomerNotFoundException(1L));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requests)))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().string("Customer with ID 1 not found."));

		verify(rewardsService, times(1)).addTransactions(requests);
	}

	@Test
	void testAddTransactionInvalidAmount() throws Exception {
		AddTransactionRequest request = new AddTransactionRequest(1L, -100.0, LocalDate.of(2024, 9, 1));
		List<AddTransactionRequest> requests = Collections.singletonList(request);
		when(rewardsService.addTransactions(requests)).thenThrow(new TransactionAmountInvalidException(-100.0));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requests)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content()
						.string("Transaction Amount must be greater than 0. Provided amount :-100.0"));

		verify(rewardsService, times(1)).addTransactions(requests);
	}

	@Test
	void testAddTransactionInvalidDate() throws Exception {
		AddTransactionRequest request = new AddTransactionRequest(1L, 100.0, LocalDate.of(2025, 1, 1)); // Future date
		List<AddTransactionRequest> requests = Collections.singletonList(request);
		when(rewardsService.addTransactions(requests))
				.thenThrow(new TransactionDateInvalidException(LocalDate.of(2025, 1, 1)));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requests)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.content()
						.string("Transaction date cannot be greater than today's date. Provided date: 2025-01-01"));

		verify(rewardsService, times(1)).addTransactions(requests);
	}
}
