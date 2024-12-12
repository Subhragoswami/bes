package com.continuum.vendor.service.repository.vendor;


import com.continuum.vendor.service.entity.vendor.CarbonCreditConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarbonCreditConfigRepository extends JpaRepository<CarbonCreditConfig, UUID> {
    Optional<CarbonCreditConfig> findByKey(String key);
}