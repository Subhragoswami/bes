package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.TransactionDump;
import com.continuum.vendor.service.repository.vendor.TransactionDumpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionDumpDao {
    private final TransactionDumpRepository transactionDumpRepository;
    public void saveTransactionDataDump(TransactionDump transactionDump){
        transactionDumpRepository.save(transactionDump);
    }
}
