package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.ChargerDetails;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.entity.vendor.ConnectorDetails;
import com.continuum.vendor.service.entity.vendor.EVSEDetails;
import com.continuum.vendor.service.mapper.ChargingStationCMSMapper;
import com.continuum.vendor.service.model.request.ChargingStationFilterRequest;
import com.continuum.vendor.service.repository.cms.*;
import com.continuum.vendor.service.repository.vendor.*;
import com.continuum.vendor.service.specifications.ChargingStationSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ChargingStationDao {
    private final ChargingStationRepository chargingStationRepository;
    private final ChargerDetailsRepository chargerDetailsRepository;
    private final EVSEDetailsRepository evseDetailsRepository;
    private final ConnectorDetailsRepository connectorDetailsRepository;
    private final CarbonCreditsRepository carbonCreditsRepository;

    private final ChargingStationRepositoryCMS chargingStationRepositoryCMS;
    private final ChargerDetailsRepositoryCMS chargerDetailsRepositoryCMS;
    private final EVSEDetailsRepositoryCMS evseDetailsRepositoryCMS;
    private final ConnectorDetailsRepositoryCMS connectorDetailsRepositoryCMS;

    private final ChargingStationCMSMapper chargingStationCMSMapper;

    public Optional<ChargingStation> getChargingStationData(UUID chargingStationDetailsId) {
        log.info("Retrieving charging station data for ID: {}", chargingStationDetailsId);
        return chargingStationRepository.findById(chargingStationDetailsId);
    }

    public Set<Long> findAllExistingChargingStationData(Set<Long> locationIds){
        return chargingStationRepository.findAllExistingChargingStationData(locationIds);
    }

    public void saveChargingStationDetailsList(String vendorCIN, List<ChargingStation> chargingStationList) {
        chargingStationRepository.saveAll(chargingStationList);

        //Save to CMS
        chargingStationRepositoryCMS.saveAll(chargingStationCMSMapper.mapToChargingStationListCMS(chargingStationList, vendorCIN));
    }

    public void saveChargerDetail(String vendorCIN, List<ChargerDetails> chargerDetails) {
        chargerDetailsRepository.saveAll(chargerDetails);

        //Save to CMS
        chargerDetailsRepositoryCMS.saveAll(chargingStationCMSMapper.mapToChargerListCMS(chargerDetails, vendorCIN));
    }

    public void saveEVSEDetails(String vendorCIN, List<EVSEDetails> evseDetails) {
        evseDetailsRepository.saveAll(evseDetails);

        //Save to CMS
        evseDetailsRepositoryCMS.saveAll(chargingStationCMSMapper.mapToEVSEListCMS(evseDetails, vendorCIN));
    }
    public void saveConnectorDetails(String vendorCIN, List<ConnectorDetails> connectorDetails) {
        connectorDetailsRepository.saveAll(connectorDetails);

        //Save to CMS
        connectorDetailsRepositoryCMS.saveAll(chargingStationCMSMapper.mapToConnectorListCMS(connectorDetails, vendorCIN));
    }

    public BigDecimal getTotalCC(Long id){
        return carbonCreditsRepository.getTotalCC(id);
    }


    public List<ChargerDetails> getByLocationId(Long id) {
        return chargerDetailsRepository.findByLocationId(id);
    }

    public List<EVSEDetails> getByChargerId(String id) {
        return evseDetailsRepository.findByChargerId(id);
    }

    public List<ConnectorDetails> getByEVSEId(String id) {
        return connectorDetailsRepository.findByEvseId(id);
    }


    public Page<ChargingStation> getAllDetails(Pageable pageable, String search, ChargingStationFilterRequest chargingStationFilterRequest) {
        log.info("Retrieving all charging station details with search: {}, filter: {}", search, chargingStationFilterRequest);
        if (StringUtils.isEmpty(search) && ObjectUtils.isEmpty(chargingStationFilterRequest)) {
            return chargingStationRepository.findAll(pageable);
        }
        if (ObjectUtils.isEmpty(chargingStationFilterRequest)) {
            log.info("fetching users based on the search criteria:{}", search);
            return chargingStationRepository.findAll(ChargingStationSpecification.withSearch(search), pageable);
        }
        Specification<ChargingStation> specification = buildSpecification(search, chargingStationFilterRequest);
        log.info("fetching users based on the filter criteria:{} {}", chargingStationFilterRequest, search);
        return chargingStationRepository.findAll(specification, pageable);
    }

    public long getCount() {
        return chargingStationRepository.count();
    }
    public Optional<ChargingStation> findByLocatonId(long id){
        return chargingStationRepository.findByLocationId(id);
    }

    public List<String> getCityList() {
        log.info("Retrieving unique city list for charging stations");
        return chargingStationRepository.getUniqueCities();
    }

    private Specification<ChargingStation> buildSpecification(String search, ChargingStationFilterRequest chargingStationFilterRequest) {
        log.info("Building specification for search: {}, filter: {}", search, chargingStationFilterRequest);
        Specification<ChargingStation> specification = Specification.where(null);

        if (StringUtils.isNotEmpty(search)) {
            specification = specification.and(ChargingStationSpecification.withSearch(search));
        }
        if (!CollectionUtils.isEmpty(chargingStationFilterRequest.getCities())) {
            specification = specification.and(ChargingStationSpecification.withCities(chargingStationFilterRequest.getCities()));
        }

        return specification;
    }
}
