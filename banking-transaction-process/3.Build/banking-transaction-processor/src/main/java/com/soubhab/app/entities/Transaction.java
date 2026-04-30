package com.soubhab.app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long accountId;

	private String type; // DEPOSIT, WITHDRAW, TRANSFER

	private BigDecimal amount;

	private long referenceAccountId;

	private LocalDateTime createdAt;
}