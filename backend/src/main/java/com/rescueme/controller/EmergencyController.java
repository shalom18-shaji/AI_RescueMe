package com.rescueme.controller;

import com.rescueme.model.EmergencyRequest;
import com.rescueme.model.EmergencyResponse;
import com.rescueme.service.GeminiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {
    private final GeminiService geminiService;
    public EmergencyController(GeminiService geminiService) { this.geminiService = geminiService; }

    @PostMapping("/analyze")
    public ResponseEntity<EmergencyResponse> analyze(@Valid @RequestBody EmergencyRequest request) {
        try {
            return ResponseEntity.ok(EmergencyResponse.success(geminiService.analyze(request.getMessage())));
        } catch (GeminiService.GeminiException e) {
            return ResponseEntity.status(e.getStatus()).body(EmergencyResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(EmergencyResponse.failure("An unexpected server error occurred."));
        }
    }
}
