package com.soubhab.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soubhab.app.data.AccountAmt;
import com.soubhab.app.data.MoneyTransfer;
import com.soubhab.app.entities.Account;
import com.soubhab.app.entities.Transaction;
import com.soubhab.app.service.BankingService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private BankingService service;

	@GetMapping("/test")
	public String testController() {
		return "Account Controller is working fine !!!!";
	}

	@GetMapping("/{id}/createAccount")
	public Account createAccount(@PathVariable Long id) {
		return service.createAccount(id);
	}

	@PostMapping("/{id}/deposit")
	public Transaction deposit(@PathVariable Long id, @RequestBody AccountAmt acctAmt) {
		return service.deposit(id, acctAmt.getAmount());
	}

	@PostMapping("/{id}/withdraw")
	public Transaction withdraw(@PathVariable Long id, @RequestBody AccountAmt acctAmt) {
		return service.withdraw(id, acctAmt.getAmount());
	}

	@GetMapping("/allAccounts")
	public List<Account> getAllEmployees() {
		return service.findAllAccounts();
	}

	@GetMapping("/{id}/acctDetails")
	public Account singleAccountDetails(@PathVariable Long id) {
		return service.getAcctDetails(id);
	}

	@PostMapping("/transfer")
	public List<Transaction> transfer(@RequestBody MoneyTransfer moneyTransfer) {
		return service.transfer(moneyTransfer.getFrom(), moneyTransfer.getTo(), moneyTransfer.getAmount());
	}

	@GetMapping("/{id}/transactions")
	public List<Transaction> transactions(@PathVariable Long acct_id) {
		return service.getTransactions(acct_id);
	}

	@GetMapping("/allTransactions")
	public List<Transaction> getAllTransactions() {
		return service.findAllTransactions();
	}
}