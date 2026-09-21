package com.rescueme.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmergencyRequest {
    @NotBlank(message = "Please describe the emergency.")
    @Size(max = 2000, message = "Message must be 2,000 characters or fewer.")
    private String message;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
