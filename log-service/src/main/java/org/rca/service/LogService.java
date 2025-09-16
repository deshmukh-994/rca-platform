package org.rca.service;

import jakarta.validation.Valid;
import org.rca.entity.LogRecord;
import org.rca.producer.LogProducer;
import org.rca.repository.LogRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final LogProducer producer;
    private final LogRecordRepository repo; // optional

    public LogService(LogProducer producer, @Autowired(required=false) LogRecordRepository repo) {
        this.producer = producer;
        this.repo = repo;
    }

    public int ingest(org.rca.model.@Valid LogIngestRequest req) {
        int n = 0;
        for (String line : req.getLogs()) {
            producer.send(req.getService(), line);
            n++;
            if (repo != null) {
                repo.save(LogRecord.of(req.getService(), req.getEnv(), line));
            }
        }
        return n;
    }
}