package com.rca.service;

import com.rca.model.RcaRequest;
import com.rca.model.RcaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MlClient {

    private final RestClient restClient;

    public MlClient(@Value("${ML_SERVICE_URL:http://localhost:8000}") String mlUrl) {
        this.restClient = RestClient.builder().baseUrl(mlUrl).build();
    }

    public RcaResponse fetchRca(RcaRequest request) {
        try {
            return restClient.post()
                    .uri("/rca")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(RcaResponse.class);
        } catch (Exception e) {
            RcaResponse fallback = new RcaResponse();
            fallback.setRoot_cause("ML service unavailable");
            fallback.setConfidence(0.0);
            fallback.setSuggestions(java.util.List.of("Retry", "Check ml-service logs"));
            fallback.setSignals(java.util.Map.of("error", e.getMessage()));
            return fallback;
        }
    }
}
