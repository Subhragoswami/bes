package com.continuum.vendor.service.repository.cms;

import com.continuum.vendor.service.entity.cms.VendorCustomerCMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VendorCustomerRepositoryCMS extends CrudRepository<VendorCustomerCMS, UUID> {

}