package com.bankSim.repos;

import com.bankSim.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    Optional<Analytics> findByDate(LocalDate date);
}
