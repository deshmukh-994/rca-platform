package org.rca.controller;

import jakarta.validation.Valid;
import org.rca.model.LogIngestRequest;
import org.rca.model.LogIngestResponse;
import org.rca.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService service;

    public LogController(LogService service){ this.service = service; }

    @GetMapping("/health")
    public ResponseEntity<?> health(){ return ResponseEntity.ok("{\"status\":\"ok\"}"); }

    @PostMapping("/ingest")
    public ResponseEntity<LogIngestResponse> ingest(@Valid @RequestBody LogIngestRequest req){
        int count = service.ingest(req);
        return ResponseEntity.accepted().body(new LogIngestResponse(true, count, System.getenv().getOrDefault("LOG_TOPIC","logs")));
    }
}