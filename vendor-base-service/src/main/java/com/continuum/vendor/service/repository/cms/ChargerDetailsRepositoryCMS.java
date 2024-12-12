package com.continuum.vendor.service.repository.cms;

import com.continuum.vendor.service.entity.cms.ChargerDetailsCMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChargerDetailsRepositoryCMS extends JpaRepository<ChargerDetailsCMS, UUID> {
}
