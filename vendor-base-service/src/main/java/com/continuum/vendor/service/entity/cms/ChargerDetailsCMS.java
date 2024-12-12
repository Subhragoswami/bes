package com.continuum.vendor.service.entity.cms;


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
@Table(name = "charger_details")
public class ChargerDetailsCMS {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "identity")
    private String identity;

    @Column(name = "charger_name")
    private String chargerName;

    @Column(name = "charge_point_oem")
    private String chargePointOem;

    @Column(name = "charge_point_device")
    private String chargePointDevice;

    @Column(name = "charge_point_connection_protocol")
    private String chargePointConnectionProtocol;

    @Column(name = "floor_level")
    private String floorLevel;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "charger_id")
    private String chargerId;

    @Column(name = "station_type")
    private String stationType;

    @Column(name = "location_id")
    private long locationId;


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