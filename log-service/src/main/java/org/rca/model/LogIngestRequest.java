package org.rca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class LogIngestRequest {
    @NotBlank
    private String service;
    @NotBlank
    private String env;
    @NotEmpty
    private List<String> logs;

    public String getService(){ return service; }
    public void setService(String v){ service = v; }
    public String getEnv(){ return env; }
    public void setEnv(String v){ env = v; }
    public List<String> getLogs(){ return logs; }
    public void setLogs(List<String> v){ logs = v; }
}