package com.rca.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rca_reports")
public class RcaReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String service;
    private String env;
    private String rootCause;
    private double confidence;
    private Instant createdAt = Instant.now();
    @Column(length = 4000) private String suggestionsJson;
    @Column(length = 8000) private String signalsJson;

    public Long getId() { return id; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getSuggestionsJson() { return suggestionsJson; }
    public void setSuggestionsJson(String suggestionsJson) { this.suggestionsJson = suggestionsJson; }
    public String getSignalsJson() { return signalsJson; }
    public void setSignalsJson(String signalsJson) { this.signalsJson = signalsJson; }
}
