package com.soubhab.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.soubhab.app.entities.Account;

import jakarta.persistence.LockModeType;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.account_id = :id")
	Optional<Account> findByIdForUpdate(long id);
}