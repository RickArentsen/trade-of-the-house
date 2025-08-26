package com.example.tradeofthehouse.repository;

import com.example.tradeofthehouse.model.Politician;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PoliticianRepository extends JpaRepository<Politician, Long> {
    // Custom query that fetches just the names
    @Query(value = "SELECT name FROM politicians", nativeQuery = true)
    List<String> findAllNames();
}