package com.example.tradeofthehouse;

import com.example.tradeofthehouse.scraper.TradeScraper;
//import com.example.tradeofthehouse.service.TradeService;
import com.example.tradeofthehouse.service.TradeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.tradeofthehouse.repository")
@EntityScan(basePackages = "com.example.tradeofthehouse.model")
public class TradeOfTheHouseApplication implements CommandLineRunner {

    private final TradeService tradeService;

    public TradeOfTheHouseApplication(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TradeOfTheHouseApplication.class, args);
    }

    public void run(String... args) throws Exception {
        tradeService.updateTradesFromPdfs();
        //tradeService.scheduledUpdateTrades();
    }
}