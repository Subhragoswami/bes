package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.ChargingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, UUID> {
    Page<ChargingSession> findAll(Specification<ChargingSession> specification, Pageable pageable);

    @Query("SELECT COALESCE(SUM(ch.usedEnergy), 0.0) FROM ChargingSession ch")
    BigDecimal countTotalEnergy();

    @Query("SELECT COALESCE(SUM(TIMESTAMPDIFF(HOUR, cs.startAt, cs.stopAt)), 0.0) FROM ChargingSession cs")
    BigDecimal countTotalUtilization();
    @Query("SELECT COUNT(c.id) FROM ChargingSession c")
    int countTotalUser();
    long count();

    @Query("SELECT cs FROM ChargingSession cs " +
        "LEFT JOIN FETCH cs.vendorCustomer vc " +
        "LEFT JOIN FETCH cs.chargingStation csn " +
        "LEFT JOIN FETCH cs.carbonCredits ccd " +
        "WHERE cs.startAt >= :startDate AND cs.startAt <= :endDate")
    List<Object[]> findAllSessionsWithCarbonCreditsByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT cs FROM ChargingSession cs " +
            "LEFT JOIN FETCH cs.vendorCustomer vc " +
            "LEFT JOIN FETCH cs.chargingStation csn " +
            "LEFT JOIN FETCH cs.carbonCredits ccd")
    List<Object[]> findAllSessionsWithCarbonCredits();

    @Query("SELECT cs FROM ChargingSession cs WHERE cs.transactionId IN :transactionIds")
    List<ChargingSession> findByTransactionIds(@Param("transactionIds") Set<String> transactionIds);

}
