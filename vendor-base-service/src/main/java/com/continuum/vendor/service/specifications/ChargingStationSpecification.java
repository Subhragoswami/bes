package com.continuum.vendor.service.specifications;


import com.continuum.vendor.service.entity.vendor.ChargingStation;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.util.List;

public class ChargingStationSpecification {
    public static Specification<ChargingStation> withSearch(String search) {
        String searchString = MessageFormat.format("%{0}%", search.toLowerCase());
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("name")), searchString.toLowerCase())
        );
    }
    public static Specification<ChargingStation> withCities(List<String> cities) {
        return (root, query, builder) -> {
            CriteriaBuilder.In<String> cityIn = builder.in(builder.lower(root.get("city")));
            cities.stream()
                    .map(String::toLowerCase)
                    .forEach(cityIn::value);
            return cityIn;
        };
    }
}
