package com.continuum.vendor.service.repository.vendor;

import com.continuum.vendor.service.entity.vendor.TransactionDump;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionDumpRepository extends CrudRepository<TransactionDump, UUID> {
}
