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
@Table(name = "evse_details")
public class EVSEDetails  {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(name = "charger_id")
    private String chargerId;

    @Column(name = "evse_id")
    private String evseId;

    @Column(name = "physical_reference")
    private String physicalReference;

    @Column(name = "max_output_power")
    private String maxOutputPower;

    @Column(name = "status")
    private String status;

    @Column(name = "connector_id")
    private int connectorId;

    @Column(name = "connector_status")
    private String connectorStatus;

    @Column(name = "availability")
    private String availability;

    @ManyToOne
    @JoinColumn(name = "charger_id", referencedColumnName = "charger_id", insertable = false, updatable = false)
    private ChargerDetails chargerDetails;

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