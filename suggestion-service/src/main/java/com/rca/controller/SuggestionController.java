package com.rca.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SuggestionController {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("{"+"status"+":"+"ok"+"}");
    }

    @GetMapping("/suggest/{label}")
    public ResponseEntity<?> suggest(@PathVariable String label) {
        Map<String, List<String>> map = Map.of(
            "Null reference in code path", List.of("Add null checks", "Harden DTO mapping", "Add tests"),
            "Database connectivity timeout", List.of("Fix firewall rules", "Tune pool size", "Add retry/backoff"),
            "Memory leak or insufficient heap", List.of("Increase heap", "Profile allocations", "Bound caches")
        );
        return ResponseEntity.ok(map.getOrDefault(label, List.of("No suggestions found")));
    }
}
