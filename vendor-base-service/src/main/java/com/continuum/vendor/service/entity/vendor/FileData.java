package com.continuum.vendor.service.entity.vendor;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "file_data")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FileData {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private String name;

    private String type;

    @Column(name = "content_type")
    private String contentType;

    private String identifier;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column(name = "data")
    private byte[] data;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
}
