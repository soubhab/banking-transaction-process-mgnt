package com.soubhab.app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soubhab.app.entities.Account;
import com.soubhab.app.entities.Transaction;
import com.soubhab.app.repo.AccountRepository;
import com.soubhab.app.repo.TransactionRepository;

@Service
public class BankingService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private TransactionRepository txnRepo;

	@Transactional
	public Transaction deposit(Long accountId, BigDecimal amount) {
		validateAmount(amount);

		Account account = accountRepo.findByIdForUpdate(accountId)
				.orElseThrow(() -> new RuntimeException("This " + accountId + " account not found"));

		account.setBalance(account.getBalance().add(amount));

		Transaction tx = txnRepo.save(buildTxn(accountId, "DEPOSIT", amount, accountId));
		return tx;
	}

	@Transactional
	public Transaction withdraw(Long accountId, BigDecimal amount) {
		validateAmount(amount);

		Account account = accountRepo.findByIdForUpdate(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		if (account.getBalance().compareTo(amount) < 0) {
			throw new RuntimeException("Insufficient balance");
		}

		account.setBalance(account.getBalance().subtract(amount));

		return txnRepo.save(buildTxn(accountId, "WITHDRAW", amount, accountId));
	}

	@Transactional
	public List<Transaction> transfer(Long fromId, Long toId, BigDecimal amount) {
		validateAmount(amount);

		// Prevent deadlock: always lock in sorted order
		Long first = fromId.compareTo(toId) < 0 ? fromId : toId;
		Long second = fromId.compareTo(toId) < 0 ? toId : fromId;

		Account acc1 = accountRepo.findByIdForUpdate(first)
				.orElseThrow(() -> new RuntimeException("First Account not found"));

		Account acc2 = accountRepo.findByIdForUpdate(second)
				.orElseThrow(() -> new RuntimeException("Second Account not found"));

		Account from = fromId.equals(first) ? acc1 : acc2;
		Account to = toId.equals(first) ? acc1 : acc2;

		if (from.getBalance().compareTo(amount) < 0) {
			throw new RuntimeException("Insufficient balance");
		}

		from.setBalance(from.getBalance().subtract(amount));
		to.setBalance(to.getBalance().add(amount));
		
		List<Transaction> txList = new ArrayList<Transaction>();

		Transaction tx1 = txnRepo.save(buildTxn(fromId, "TRANSFER_OUT", amount, toId));
		Transaction tx2 = txnRepo.save(buildTxn(toId, "TRANSFER_IN", amount, fromId));
		
		txList.add(tx1);
		txList.add(tx2);
		
		return txList;
	}

	public Account getAcctDetails(Long accountId) {
		return accountRepo.findById(accountId).orElseThrow();
	}

	public List<Transaction> getTransactions(Long accountId) {
		return txnRepo.findByAccountIdOrderByCreatedAtDesc(accountId);
	}

	private void validateAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Invalid amount");
		}
	}

	private Transaction buildTxn(Long accountId, String type, BigDecimal amount, Long ref) {
		Transaction txn = new Transaction();
		// txn.setId(UUID.randomUUID());
		txn.setAccountId(accountId);
		txn.setType(type);
		txn.setAmount(amount);
		txn.setReferenceAccountId(ref);
		txn.setCreatedAt(LocalDateTime.now());
		return txn;
	}

	public Account createAccount(Long id) {

		Account acct = new Account();
		acct.setAccount_id(id);
		acct.setBalance(BigDecimal.valueOf(0));
		acct.setCreatedAt(LocalDateTime.now());

		return accountRepo.save(acct);
	}

	public List<Account> findAllAccounts() {
		return accountRepo.findAll();
	}

	public List<Transaction> findAllTransactions() {
		return txnRepo.findAll();
	}
}