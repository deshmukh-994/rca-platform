package com.rca.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rca.entity.RcaReport;
import com.rca.model.RcaRequest;
import com.rca.model.RcaResponse;
import com.rca.repository.RcaReportRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final RcaReportRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public ReportService(RcaReportRepository repo) { this.repo = repo; }

    public RcaResponse saveReport(RcaRequest request, RcaResponse resp) {
        try {
            RcaReport r = new RcaReport();
            r.setService(request.getService());
            r.setEnv(request.getEnv());
            r.setRootCause(resp.getRoot_cause());
            r.setConfidence(resp.getConfidence());
            r.setSuggestionsJson(mapper.writeValueAsString(resp.getSuggestions()));
            r.setSignalsJson(mapper.writeValueAsString(resp.getSignals()));
            repo.save(r);
        } catch (Exception ignored) {}
        return resp;
    }
}
