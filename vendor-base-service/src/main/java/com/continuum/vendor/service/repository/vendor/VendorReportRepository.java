package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.VendorReport;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface VendorReportRepository extends JpaRepository<VendorReport, UUID> {
    @Query("FROM VendorReport v WHERE v.dateCreated >= :startDate AND v.dateCreated <= :endDate")
    Page<VendorReport> findReportByDate(Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Modifying
    @Transactional
    @Query("UPDATE VendorReport r SET r.status = :status, r.fileId = :fileId WHERE r.id = :id")
    void updateReportStatusById(@Param("id") UUID id, @Param("status") String status, @Param("fileId") UUID fileId);
}
