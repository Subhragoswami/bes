package com.continuum.vendor.service.specifications;

import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.entity.vendor.VendorCustomer;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ChargingSessionSpecification {

    public static class FilterSpecification implements Specification<ChargingSession> {
        private final List<Long> stationIds;
        private final List<String> cities;
        private final Date startDate;
        private final Date endDate;

        public FilterSpecification(List<String> cities, Date startDate , Date endDate, List<Long> stationIds ) {
            this.stationIds = stationIds;
            this.cities = cities;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public Predicate toPredicate(Root<ChargingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            log.info("Query filter for stationId: {}, cities: {}, startDate: {}, endDate: {}", stationIds, cities, startDate, endDate);

            List<Predicate> predicates = new ArrayList<>();
            if(!CollectionUtils.isEmpty(stationIds)){
                predicates.add(root.get("chargingStation").get("locationId").in(stationIds));
            }

            if (!CollectionUtils.isEmpty(cities)) {
                predicates.add(root.get("chargingStation").get("city").in(cities));
            }
            if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
                predicates.add(criteriaBuilder.between(root.get("startAt"), startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.of(23, 59, 0))));
            }
            log.info("Specific query to get charging session data : {}", predicates);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
    }
    public static class SearchSpecification implements Specification<ChargingSession> {
        private final String search;

        public SearchSpecification(String search) {
            this.search = search;
        }

        @Override
        public Predicate toPredicate(Root<ChargingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            log.info("Query for search : {}", search);
            String searchString = "%" + search.toLowerCase() + "%";

            Join<ChargingSession, ChargingStation> stationJoin = root.join("chargingStation", JoinType.LEFT);
            Predicate cityInStationPredicate = criteriaBuilder.like(criteriaBuilder.lower(stationJoin.get("city")), searchString);

            Join<ChargingSession, VendorCustomer> customerJoin = root.join("vendorCustomer", JoinType.LEFT);
            Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(customerJoin.get("firstName")), searchString);
            Predicate lastPredicate = criteriaBuilder.like(criteriaBuilder.lower(customerJoin.get("lastName")), searchString);
            log.info("search query criteria : {}, : {}, {}", cityInStationPredicate, firstNamePredicate, lastPredicate);
            return criteriaBuilder.or(cityInStationPredicate, firstNamePredicate, lastPredicate);
        }
    }
}
