package com.soubhab.app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

	@Id
	private long account_id;

	private BigDecimal balance;

	private LocalDateTime createdAt;
}
