package com.retail.reward.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name="CUSTOMER")
@Getter
@Setter
public class Customer {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	@Column (name="CUSTOMER_NAME")
	private String customerName;
	
}



