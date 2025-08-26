package com.example.tradeofthehouse.repository;

import com.example.tradeofthehouse.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    // add query methods if needed
}
