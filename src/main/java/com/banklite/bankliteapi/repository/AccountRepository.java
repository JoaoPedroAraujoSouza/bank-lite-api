package com.banklite.bankliteapi.repository;

import com.banklite.bankliteapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
