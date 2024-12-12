package com.continuum.vendor.service.entity.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carbon_credit_config")
public class CarbonCreditConfig {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;


    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "description", nullable = false)
    private String description;
}