package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.entity.vendor.VendorCustomer;
import com.continuum.vendor.service.mapper.ChargingSessionMapper;
import com.continuum.vendor.service.mapper.VendorCustomerMapper;
import com.continuum.vendor.service.model.request.ChargingSessionFilter;
import com.continuum.vendor.service.repository.cms.ChargingSessionRepositoryCMS;
import com.continuum.vendor.service.repository.cms.VendorCustomerRepositoryCMS;
import com.continuum.vendor.service.repository.vendor.ChargingSessionRepository;
import com.continuum.vendor.service.repository.vendor.VendorCustomerRepository;
import com.continuum.vendor.service.specifications.ChargingSessionSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
@Slf4j
public class ChargingSessionDao {
    private final ChargingSessionRepository chargingSessionRepository;
    private final VendorCustomerRepository vendorCustomerRepository;

    private final ChargingSessionRepositoryCMS chargingSessionRepositoryCMS;
    private final VendorCustomerRepositoryCMS vendorCustomerRepositoryCMS;

    private final ChargingSessionMapper chargingSessionMapper;
    private final VendorCustomerMapper vendorCustomerMapper;

    public void saveChargingSessionDetailsList(String vendorCIN, List<ChargingSession> chargingSessionList, List<VendorCustomer> vendorCustomerList) {
        chargingSessionRepository.saveAll(chargingSessionList);
        vendorCustomerRepository.saveAll(vendorCustomerList);

        //Saving to CMS
        chargingSessionRepositoryCMS.saveAll(chargingSessionMapper.mapToChargingSessionListCMS(chargingSessionList, vendorCIN));
        vendorCustomerRepositoryCMS.saveAll(vendorCustomerMapper.mapToVendorCustomerListCMS(vendorCustomerList, vendorCIN));
    }


    public Map<String, ChargingSession> getExistingTransactionIdsMap(Set<String> transactionIds) {
        List<ChargingSession> chargingSessions = chargingSessionRepository.findByTransactionIds(transactionIds);
        return chargingSessions.stream()
                .collect(Collectors.toMap(ChargingSession::getTransactionId, cs -> cs));
    }

    public Page<ChargingSession> getAllChargingSession(ChargingSessionFilter chargingSessionFilter, String search, Pageable pageable) {
        if(StringUtils.isEmpty(search) && ObjectUtils.isEmpty(chargingSessionFilter)){
            log.info("Fetching all the data from the DB: {}",pageable);
            return chargingSessionRepository.findAll(pageable);
        }
        if(ObjectUtils.isEmpty(chargingSessionFilter)){
            log.info("fetching users based on the search criteria:{}", search);
            Specification<ChargingSession> searchSpecification = new ChargingSessionSpecification.SearchSpecification(search);
            return  chargingSessionRepository.findAll(searchSpecification, pageable);
        }

        Specification<ChargingSession> specification = buildSpecification(search , chargingSessionFilter);
        log.info("fetching users based on the filter criteria:{}", chargingSessionFilter);
        return chargingSessionRepository.findAll(specification,pageable);
    }
private Specification<ChargingSession> buildSpecification(String search, ChargingSessionFilter chargingSessionFilter) {
    Specification<ChargingSession> specification = Specification.where(null);

    if (StringUtils.isNotEmpty(search)) {
        log.info("Search Specification : {}", search);
        Specification<ChargingSession> searchSpec = new ChargingSessionSpecification.SearchSpecification(search);
        specification = specification.and(searchSpec);
    }
    if (ObjectUtils.isNotEmpty(chargingSessionFilter)){
        log.info("Charging Session Filter Specification : {}", chargingSessionFilter);
        Specification<ChargingSession> stationSpec = new ChargingSessionSpecification.FilterSpecification(chargingSessionFilter.getCities(), chargingSessionFilter.getStartDate(), chargingSessionFilter.getEndDate(), chargingSessionFilter.getStationIds());
        specification = specification.and(stationSpec);
    }
    log.info("Specification for get charging session details : {}", specification);
    return specification;
    }
}
