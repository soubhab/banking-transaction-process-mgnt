package com.soubhab.app.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MoneyTransfer {
	private Long from;
	private Long to;
	private BigDecimal amount;
}
