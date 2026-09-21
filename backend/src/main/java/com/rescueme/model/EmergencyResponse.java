package com.rescueme.model;

import java.util.List;

public class EmergencyResponse {
    private boolean success;
    private EmergencyAnalysis analysis;
    private String error;

    public EmergencyResponse() { }
    private EmergencyResponse(boolean success, EmergencyAnalysis analysis, String error) {
        this.success = success; this.analysis = analysis; this.error = error;
    }
    public static EmergencyResponse success(EmergencyAnalysis analysis) { return new EmergencyResponse(true, analysis, null); }
    public static EmergencyResponse failure(String error) { return new EmergencyResponse(false, null, error); }
    public boolean isSuccess() { return success; }
    public EmergencyAnalysis getAnalysis() { return analysis; }
    public String getError() { return error; }

    public static class EmergencyAnalysis {
        private String emergencyType;
        private String severity;
        private double confidence;
        private String summary;
        private List<String> immediateActions;
        private String additionalAdvice;

        public EmergencyAnalysis() { }
        public String getEmergencyType() { return emergencyType; }
        public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public List<String> getImmediateActions() { return immediateActions; }
        public void setImmediateActions(List<String> immediateActions) { this.immediateActions = immediateActions; }
        public String getAdditionalAdvice() { return additionalAdvice; }
        public void setAdditionalAdvice(String additionalAdvice) { this.additionalAdvice = additionalAdvice; }
    }
}
