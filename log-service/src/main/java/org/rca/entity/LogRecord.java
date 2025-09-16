package org.rca.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="logs")
public class LogRecord {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String service;
    private String env;

    @Column(length=2000)
    private String line;

    private Instant createdAt = Instant.now();

    public static LogRecord of(String service, String env, String line){
        LogRecord r = new LogRecord();
        r.service = service; r.env = env; r.line = line; return r;
    }

    public Long getId(){ return id; }
    public String getService(){ return service; }
    public void setService(String v){ service=v; }
    public String getEnv(){ return env; }
    public void setEnv(String v){ env=v; }
    public String getLine(){ return line; }
    public void setLine(String v){ line=v; }
    public Instant getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Instant v){ createdAt=v; }
}
