package com.rca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class RcaRequest {
    @NotBlank
    private String service;
    @NotBlank
    private String env;
    @NotEmpty
    private java.util.List<String> logs;

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }
    public java.util.List<String> getLogs() { return logs; }
    public void setLogs(java.util.List<String> logs) { this.logs = logs; }
}
