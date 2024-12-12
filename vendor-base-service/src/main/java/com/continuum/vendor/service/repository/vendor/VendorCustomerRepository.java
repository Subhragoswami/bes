package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.VendorCustomer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VendorCustomerRepository extends CrudRepository<VendorCustomer, UUID> {
    @Query("SELECT COUNT(v.id) FROM VendorCustomer v")
    int countTotalUser();
}