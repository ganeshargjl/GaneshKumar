package com.retail.reward.application.util;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class SystemDateProvider implements DateProvider {
	@Override
	public LocalDate today() {
		return LocalDate.now();
	}

}
