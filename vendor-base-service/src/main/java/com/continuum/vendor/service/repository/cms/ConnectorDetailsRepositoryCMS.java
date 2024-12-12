package com.continuum.vendor.service.repository.cms;

import com.continuum.vendor.service.entity.cms.ConnectorDetailsCMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConnectorDetailsRepositoryCMS extends JpaRepository<ConnectorDetailsCMS, UUID> {

}
