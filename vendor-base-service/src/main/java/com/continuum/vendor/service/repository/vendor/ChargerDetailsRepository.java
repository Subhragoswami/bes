package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.ChargerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChargerDetailsRepository extends JpaRepository<ChargerDetails, UUID> {

   List<ChargerDetails> findByLocationId(Long locationId);
   @Query("SELECT COUNT(c.id) FROM ChargerDetails c")
   int countTotalCharger();
}
