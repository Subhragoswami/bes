package com.continuum.vendor.service.repository.cms;

import com.continuum.vendor.service.entity.cms.CarbonCreditsCMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarbonCreditsRepositoryCMS extends CrudRepository<CarbonCreditsCMS, UUID> {

}