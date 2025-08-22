package com.banklite.bankliteapi.repository;

import com.banklite.bankliteapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
