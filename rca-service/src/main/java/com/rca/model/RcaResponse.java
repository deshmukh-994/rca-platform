package com.rca.model;

import java.util.List;
import java.util.Map;

public class RcaResponse {
    private String root_cause;
    private double confidence;
    private java.util.List<String> suggestions;
    private java.util.Map<String, Object> signals;

    public String getRoot_cause() { return root_cause; }
    public void setRoot_cause(String root_cause) { this.root_cause = root_cause; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public java.util.List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(java.util.List<String> suggestions) { this.suggestions = suggestions; }
    public java.util.Map<String, Object> getSignals() { return signals; }
    public void setSignals(java.util.Map<String, Object> signals) { this.signals = signals; }
}
