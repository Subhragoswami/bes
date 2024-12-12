package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.TokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokenRepository  extends JpaRepository<TokenDetails, UUID> {
}
