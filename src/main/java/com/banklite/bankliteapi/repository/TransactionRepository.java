package com.banklite.bankliteapi.repository;

import com.banklite.bankliteapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
