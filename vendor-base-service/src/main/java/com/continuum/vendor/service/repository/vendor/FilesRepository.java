package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FilesRepository extends JpaRepository<FileData, UUID> {

    Optional<FileData> findByTypeAndIdentifier(String type, String identifier);
}
