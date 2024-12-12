package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.EVSEDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EVSEDetailsRepository extends JpaRepository<EVSEDetails, UUID> {
    public List<EVSEDetails> findByChargerId(String id);
}
