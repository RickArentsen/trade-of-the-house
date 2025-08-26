package com.example.tradeofthehouse;

import com.example.tradeofthehouse.scraper.TradeScraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TradeScraperTests {

    private TradeScraper tradeScraper;

//    @BeforeEach
//    void setUp() {
//        tradeScraper = new TradeScraper();
//        // Reset static state before each test
//        TradeScraper.trades.clear();
//        TradeScraper.uniqueTickers.clear();
//        TradeScraper.seenTickers.clear();
//    }
//
//    @Test
//    void testSingleTradeWithCompleteAmount() {
//        String text = "Alphabet Inc. - Class A Common Stock (GOOGL) [OP]\n" +
//                "P 01/14/2025 01/14/2025 $250,001 - $500,000\n" +
//                "Filing Status: New\n" +
//                "Description: Purchased 50 call options";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(1, trades.size());
//        TradeScraper.Trade trade = trades.get(0);
//        assertEquals("TestPolitician", trade.politician);
//        assertEquals("GOOGL", trade.ticker);
//        assertEquals("Buy", trade.action);
//        assertEquals(LocalDate.of(2025, 1, 14), trade.date);
//        assertEquals("$250,001 - $500,000", trade.amount);
//        assertEquals("New", trade.filingStatus);
//        assertEquals("Purchased 50 call options", trade.description);
//    }
//
//    @Test
//    void testMultiLineAmountHandling() {
//        String text = "NVIDIA Corporation - Common Stock (NVDA) [ST]\n" +
//                "P 12/20/2024 12/20/2024 $500,001 -\n" +  // Amount continues on next line
//                "$1,000,000\n" +
//                "Filing Status: New\n" +
//                "Description: Exercised options";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(1, trades.size());
//        TradeScraper.Trade trade = trades.get(0);
//        assertEquals("NVDA", trade.ticker);
//        assertEquals("Buy", trade.action);
//        assertEquals("$500,001 - $1,000,000", trade.amount);
//    }
//
//    @Test
//    void testDescriptionWithDollarSignIgnored() {
//        String text = "Apple Inc. - Common Stock (AAPL) [ST]\n" +
//                "S 12/31/2024 12/31/2024 $5,000,001 - $25,000,000\n" +
//                "Filing Status: New\n" +
//                "Description: Sold 31,600 shares at $500 each";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(1, trades.size());
//        TradeScraper.Trade trade = trades.get(0);
//        assertEquals("AAPL", trade.ticker);
//        assertEquals("Sell", trade.action);
//        assertEquals("$5,000,001 - $25,000,000", trade.amount);
//        assertEquals("Sold 31,600 shares at $500 each", trade.description);
//    }
//
//    @Test
//    void testPartialSaleHandling() {
//        String text = "Microsoft Corporation (MSFT) [ST]\n" +
//                "S (partial) 11/15/2024 11/15/2024 $1,000,001 - $5,000,000\n" +
//                "Filing Status: Amendment\n" +
//                "Description: Partial sale of shares";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(1, trades.size());
//        TradeScraper.Trade trade = trades.get(0);
//        assertEquals("MSFT", trade.ticker);
//        assertEquals("Sell (partial)", trade.action);
//        assertEquals("Amendment", trade.filingStatus);
//    }
//
//    @Test
//    void testMultipleTradesInDocument() {
//        String text = "Alphabet Inc. (GOOGL) [OP]\n" +
//                "P 01/14/2025 01/14/2025 $250,001 - $500,000\n" +
//                "Filing Status: New\n\n" +
//                "Amazon.com, Inc. (AMZN) [OP]\n" +
//                "P 01/14/2025 01/14/2025 $250,001 - $500,000\n" +
//                "Filing Status: New";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(2, trades.size());
//
//        TradeScraper.Trade trade1 = trades.get(0);
//        assertEquals("GOOGL", trade1.ticker);
//        assertEquals("Buy", trade1.action);
//
//        TradeScraper.Trade trade2 = trades.get(1);
//        assertEquals("AMZN", trade2.ticker);
//        assertEquals("Buy", trade2.action);
//    }
//
//    @Test
//    void testAmountWithSymbolsOnly() {
//        String text = "Tesla Inc (TSLA) [ST]\n" +
//                "P 09/01/2025 09/01/2025 $1,500 -\n" +  // Incomplete amount
//                "$2,500\n" +  // Pure amount line
//                "Filing Status: New\n" +
//                "Description: This line has $ but also letters";
//
//        tradeScraper.parseTrades(text, "TestPolitician");
//        List<TradeScraper.Trade> trades = TradeScraper.trades;
//
//        assertEquals(1, trades.size());
//        TradeScraper.Trade trade = trades.get(0);
//        assertEquals("TSLA", trade.ticker);
//        assertEquals("$1,500 - $2,500", trade.amount);
//    }
}