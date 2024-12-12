package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.status = :status, t.description = :description WHERE t.id = :id")
    void updateStatusAndDescriptionById(
            @Param("id") UUID id,
            @Param("status") String status,
            @Param("description") String description);

    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.startDate <= :endDate AND t.endDate >= :startDate AND status=:status AND dataCategory=:dataCategory")
    boolean checkRecordExisitsForCurrentStartEndDateWithStatusAndCategory(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") String status, @Param("dataCategory") String dataCategory);

    List<Transaction> findByStatusAndDataCategory(String status, String dataCategory);
}
