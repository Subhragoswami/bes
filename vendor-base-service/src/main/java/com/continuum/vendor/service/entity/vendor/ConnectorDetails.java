package com.continuum.vendor.service.entity.vendor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "connector_details")
public class ConnectorDetails {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(name = "evse_id")
    private String evseId;

    @Column(name = "connector_id")
    private String connectorId;

    @Column(name = "name")
    private String name;

    @Column(name = "standard_name")
    private String standardName;

    @Column(name = "format_name")
    private String formatName;

    @Column(name = "power_type")
    private String powerType;

    @Column(name = "cms_id")
    private String cmsId;

    @Column(name = "max_voltage")
    private int maxVoltage;

    @Column(name = "max_amperage")
    private int maxAmperage;

    @Column(name = "max_electric_power")
    private int maxElectricPower;

    @Column(name = "terms_condition_url")
    private String termsConditionUrl;

    @Column(name = "connector_image")
    private String connectorImage;

    @ManyToOne
    @JoinColumn(name = "evse_id", referencedColumnName = "evse_id", insertable = false, updatable = false)
    private EVSEDetails evseDetails;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}