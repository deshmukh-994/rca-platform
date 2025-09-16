package com.rca.controller;

import com.rca.model.RcaRequest;
import com.rca.model.RcaResponse;
import com.rca.service.MlClient;
import com.rca.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RcaController {

    private final MlClient mlClient;
    private final ReportService reportService;

    public RcaController(MlClient mlClient, ReportService reportService) {
        this.mlClient = mlClient;
        this.reportService = reportService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().body("{"+"status"+":"+"ok"+"}");
    }

    @PostMapping("/rca")
    public ResponseEntity<RcaResponse> rca(@Validated @RequestBody RcaRequest request) {
        RcaResponse resp = mlClient.fetchRca(request);
        reportService.saveReport(request, resp);
        return ResponseEntity.ok(resp);
    }
}
