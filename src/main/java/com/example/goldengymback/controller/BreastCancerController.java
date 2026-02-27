package com.example.goldengymback.controller;

import com.example.goldengymback.serviceImpl.BreastCancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BreastCancerController {

    @Autowired
    private BreastCancerService breastCancerService;

    // Endpoint to get ACR score and update mammary scan
    @GetMapping("/acr/{scanId}")
    public ResponseEntity<String> getAcrScore(@PathVariable Long scanId) {
        try {
            // Call the service method to get ACR score and recommended action
            String score = breastCancerService.getAcrScore(scanId);
            return ResponseEntity.ok(score);  // Return the AI response
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
