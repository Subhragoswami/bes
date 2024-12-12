package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.ConnectorDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectorDetailsRepository extends JpaRepository<ConnectorDetails, UUID> {
    @Query("SELECT cd FROM ConnectorDetails cd WHERE cd.evseId = :evseId")
    List<ConnectorDetails> findByEvseId(@Param("evseId") String evseId);
}
