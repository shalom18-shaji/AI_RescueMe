package com.rescueme.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescueme.model.EmergencyResponse.EmergencyAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class GeminiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private static final String SYSTEM_PROMPT = """
        You are RescueMe, an emergency-analysis assistant for a college prototype. Analyze the user's report calmly and conservatively. This is not dispatch and must never claim that you contacted emergency services. Return only valid JSON with exactly these keys: emergencyType, severity, confidence, summary, immediateActions, additionalAdvice. emergencyType must be one of FIRE, FLOOD, EARTHQUAKE, ACCIDENT, MEDICAL, STORM, LANDSLIDE, CRIME, MENTAL_HEALTH, OTHER, UNKNOWN. severity must be LOW, MEDIUM, HIGH, CRITICAL, or UNKNOWN. confidence is a number from 0 to 1. Give 3 to 5 short, practical immediateActions. If information is insufficient use UNKNOWN and lower confidence. Where there may be immediate danger, advise contacting local emergency services. Do not diagnose medical conditions. Keep advice general, safety-first, and concise.
        """;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String apiKey;
    private final String model;
    private final Duration timeout;

    public GeminiService(ObjectMapper objectMapper,
                         @Value("${gemini.api.key}") String apiKey,
                         @Value("${gemini.api.model}") String model,
                         @Value("${gemini.api.timeout-seconds}") int timeoutSeconds) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        logger.info("Gemini service configured to use model: {}", model);
    }

    public EmergencyAnalysis analyze(String message) throws GeminiException {
        if (apiKey == null || apiKey.isBlank()) throw new GeminiException(HttpStatus.SERVICE_UNAVAILABLE, "AI analysis is not configured on this server.");
        try {
            String body = objectMapper.writeValueAsString(java.util.Map.of(
                    "systemInstruction", java.util.Map.of("parts", List.of(java.util.Map.of("text", SYSTEM_PROMPT))),
                    "contents", List.of(java.util.Map.of("role", "user", "parts", List.of(java.util.Map.of("text", message.trim())))),
                    "generationConfig", java.util.Map.of("responseMimeType", "application/json", "temperature", 0.2)
            ));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent"))
                    .timeout(timeout).header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                logger.warn("Gemini API returned HTTP status {}: {}", response.statusCode(), googleErrorMessage(response.body()));
                throw new GeminiException(HttpStatus.BAD_GATEWAY, "The AI analysis service is temporarily unavailable.");
            }
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode text = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (text.isMissingNode() || text.asText().isBlank()) throw new GeminiException(HttpStatus.BAD_GATEWAY, "The AI service returned an unusable analysis.");
            EmergencyAnalysis analysis = objectMapper.readValue(text.asText(), EmergencyAnalysis.class);
            validateAnalysis(analysis);
            return analysis;
        } catch (java.net.http.HttpTimeoutException e) {
            throw new GeminiException(HttpStatus.GATEWAY_TIMEOUT, "The AI analysis timed out. Please try again.");
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new GeminiException(HttpStatus.BAD_GATEWAY, "Unable to reach the AI analysis service.");
        } catch (IllegalArgumentException e) {
            throw new GeminiException(HttpStatus.BAD_GATEWAY, "The AI service returned invalid data.");
        }
    }

    private void validateAnalysis(EmergencyAnalysis a) throws GeminiException {
        if (a == null || a.getEmergencyType() == null || a.getSeverity() == null || a.getSummary() == null ||
                a.getImmediateActions() == null || a.getImmediateActions().isEmpty() || a.getAdditionalAdvice() == null ||
                a.getConfidence() < 0 || a.getConfidence() > 1) {
            throw new GeminiException(HttpStatus.BAD_GATEWAY, "The AI service returned an incomplete analysis.");
        }
    }

    private String googleErrorMessage(String responseBody) {
        try {
            String message = objectMapper.readTree(responseBody).path("error").path("message").asText();
            return message.isBlank() ? "No error message supplied." : message;
        } catch (Exception ignored) {
            return "No readable error message supplied.";
        }
    }

    public static class GeminiException extends Exception {
        private final HttpStatus status;
        public GeminiException(HttpStatus status, String message) { super(message); this.status = status; }
        public HttpStatus getStatus() { return status; }
    }
}
