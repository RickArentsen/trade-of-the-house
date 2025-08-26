package com.example.tradeofthehouse.service;

import com.example.tradeofthehouse.model.Pdf;
import com.example.tradeofthehouse.model.Trade;
import com.example.tradeofthehouse.repository.PdfRepository;
import com.example.tradeofthehouse.repository.PoliticianRepository;
import com.example.tradeofthehouse.repository.TradeRepository;
import com.example.tradeofthehouse.scraper.TradeScraper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    private final TradeRepository TradeRepo;
    private final TradeScraper scraper;
    private final PoliticianRepository PoliRepo;
    private final PdfRepository PdfRepo;

    public TradeService(TradeRepository TradeRepo, TradeScraper scraper,
                        PoliticianRepository PoliRepo, PdfRepository PdfRepo) {
        this.TradeRepo = TradeRepo;
        this.scraper = scraper;
        this.PoliRepo = PoliRepo;
        this.PdfRepo = PdfRepo;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/New_York") // Runs daily at 12 AM EST
    public void scheduledUpdateTrades() throws IOException {
        updateTradesFromPdfs();
    }

    public void insertTestData() {
        Trade trade = new Trade("Pelosi", "AAPL", "BUY", "$100.000", "Description", "New", LocalDateTime.now());
        TradeRepo.save(trade);
        System.out.println("Inserted trade into DB: " + trade);
    }

    public void updateTradesFromPdfs() throws IOException {
        int currentYear = LocalDate.now().getYear();
        List<String> politicians = PoliRepo.findAllNames();

        for (String name : politicians) {
            // Search filings and extract PDF links
            String[] nameParts = name.split(" ");
            String lastName = nameParts[nameParts.length - 1];
            String html = TradeScraper.searchFilings(lastName, String.valueOf(currentYear));
            List<String> pdfLinks = TradeScraper.extractPdfLinks(html);

            for (String pdfLink : pdfLinks) {
                // check if pdf already exists in DB
                //if (!PdfRepo.existsByFileName(pdfLink)) {
                // Download PDF bytes
                byte[] pdfData = TradeScraper.downloadPdfBytes(pdfLink);

                // Save PDF to DB
                Pdf pdfEntity = new Pdf(pdfLink, LocalDateTime.now());
                PdfRepo.save(pdfEntity);

                // Extract trades from PDF bytes
                List<Trade> trades = TradeScraper.extractTradesFromPdf(pdfData, name);

                TradeRepo.saveAll(trades);

                System.out.printf("Saved %d trades for %s from PDF %s%n", trades.size(), name,
                                  pdfLink);
                //}
            }
        }
    }
}