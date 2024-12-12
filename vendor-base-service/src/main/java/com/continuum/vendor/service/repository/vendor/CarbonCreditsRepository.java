package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.CarbonCredits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface CarbonCreditsRepository extends JpaRepository<CarbonCredits, UUID> {
    @Query("SELECT COALESCE(SUM(c.creditScorePoints), 0.0) FROM CarbonCredits c WHERE c.stationId = :stationId")
    BigDecimal getTotalCC(@Param("stationId") Long stationId);

    @Query("SELECT COALESCE(SUM(c.creditScorePoints), 0.0) FROM CarbonCredits c")
    BigDecimal getTotalCC();
}