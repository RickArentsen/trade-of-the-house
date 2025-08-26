//package com.example.tradeofthehouse.scheduler;
//
//import com.example.tradeofthehouse.scraper.TradeScraper;
//import com.example.tradeofthehouse.service.TradeService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//@
//Component
//public class TradeScheduler {
//
//    private final TradeScraper scraper;
//    private final TradeService tradeService;
//
//    public TradeScheduler(TradeScraper scraper, TradeService tradeService) {
//        this.scraper = scraper;
//        this.tradeService = tradeService;
//    }
//
//    @Scheduled(cron = "0 0 0 * * *", zone = "America/New_York")
//    public void fetchTrades() {
//        var trades = scraper.getTrades();
//        //tradeService.insertTestData();
//        //tradeService.saveAll(trades);
//        System.out.println("Saved " + trades.size() + " trades to DB");
//    }
//}