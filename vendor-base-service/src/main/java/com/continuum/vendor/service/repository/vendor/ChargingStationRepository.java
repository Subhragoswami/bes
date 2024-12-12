package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.ChargingStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation,UUID> {
    Page<ChargingStation> findAll(Specification<ChargingStation> specification, Pageable pageable);
    @Query("select distinct(city) from ChargingStation where city is not null and city <> ''")
    List<String> getUniqueCities();
    Optional<ChargingStation> findByLocationId(long id);

    @Query("SELECT c.locationId FROM ChargingStation c WHERE c.locationId IN :locationIds")
    Set<Long> findAllExistingChargingStationData(@Param("locationIds") Set<Long> locationIds);
}