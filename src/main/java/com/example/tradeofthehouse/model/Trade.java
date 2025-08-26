package com.example.tradeofthehouse.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Access(AccessType.FIELD)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trade_id;

    @Column(name = "politician")
    public String politician;

    @Column(name = "ticker_symbol")
    public String ticker;

    @Column(name = "transaction_type")
    public String action; // "BUY" or "SELL"

    @Column(name = "amount")
    public String amount;

    @Column(name = "description")
    public String description;

    @Column(name = "filing_status")
    public String filingStatus;

    @Column(name = "trade_date")
    public LocalDateTime date;

    @Column(length = 400, name = "raw_text")
    private String rawText; // optional raw scraped block

    public Trade() {
    }

    public Trade(String politician, String ticker, String action, String amount, String description, String filingStatus, LocalDateTime date) {
        this.politician = politician;
        this.ticker = ticker;
        this.action = action;
        this.amount = amount;
        this.description = description;
        this.filingStatus = filingStatus;
        this.date = date;
    }

    public Long getId() { return trade_id; }
    public void setId(Long id) { this.trade_id = id; }

    public String getPolitician() { return politician; }
    public void setPolitician(String politician) { this.politician = politician; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFilingStatus() { return filingStatus; }
    public void setFilingStatus(String filingStatus) { this.filingStatus = filingStatus; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    @Override
    public String toString() {
        return String.format(
                "Trade{politician='%s', ticker='%s', action='%s', date=%s, amount='%s', filingStatus='%s', description='%s'}",
                politician, ticker, action, date, amount, filingStatus, description
        );
    }
}