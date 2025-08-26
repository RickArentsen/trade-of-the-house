package com.example.tradeofthehouse.controller;

import com.example.tradeofthehouse.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TriggerController {

    @Autowired
    private TradeService tradeService;

    // Secret token for authentication (you'll set this as an environment variable)
    private static final String EXPECTED_TOKEN = System.getenv("UPDATE_TOKEN");

    @PostMapping("/trigger-update")
    public ResponseEntity<String> triggerUpdate(
            @RequestHeader(value = "Authorization", required = false) String authToken) {

        // Check if the request is authorized
        if (EXPECTED_TOKEN == null || !EXPECTED_TOKEN.equals(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or missing token");
        }

        try {
            tradeService.updateTradesFromPdfs();
            return ResponseEntity.ok("Trade update completed successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("Error triggering update: " + e.getMessage());
        }
    }
}