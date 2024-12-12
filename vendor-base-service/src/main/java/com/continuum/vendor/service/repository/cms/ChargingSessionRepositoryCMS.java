package com.continuum.vendor.service.repository.cms;

import com.continuum.vendor.service.entity.cms.ChargingSessionCMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;



public interface ChargingSessionRepositoryCMS extends JpaRepository<ChargingSessionCMS, UUID> {

}
