package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.repository.vendor.CarbonCreditsRepository;
import com.continuum.vendor.service.repository.vendor.ChargerDetailsRepository;
import com.continuum.vendor.service.repository.vendor.ChargingSessionRepository;
import com.continuum.vendor.service.repository.vendor.VendorCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
@Slf4j
public class VendorDashboardDao {
    private final ChargingSessionRepository chargingSessionRepository;
    private final VendorCustomerRepository vendorCustomerRepository;
    private final CarbonCreditsRepository carbonCreditsRepository;
    private final ChargerDetailsRepository chargerDetailsRepository;

    public int getCountOfTotalCharger() {
        return chargerDetailsRepository.countTotalCharger();
    }

    public BigDecimal getTotalEnergy() {
        return chargingSessionRepository.countTotalEnergy();
    }

    public int getTotalSession() {
        return chargingSessionRepository.countTotalUser();
    }

    public BigDecimal getTotalUtilization() {
        return chargingSessionRepository.countTotalUtilization();
    }

    public BigDecimal getTotalCarbonCredit(){
        return carbonCreditsRepository.getTotalCC();
    }
}
