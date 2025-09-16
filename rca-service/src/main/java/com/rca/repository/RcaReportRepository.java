package com.rca.repository;

import com.rca.entity.RcaReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RcaReportRepository extends JpaRepository<RcaReport, Long> {
    java.util.List<RcaReport> findTop50ByOrderByCreatedAtDesc();
}
