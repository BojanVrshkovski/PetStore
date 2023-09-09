package com.petstore.repository;

import com.petstore.entity.BuyLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyLogEntryRepository extends JpaRepository<BuyLogEntry, Long> {

}
