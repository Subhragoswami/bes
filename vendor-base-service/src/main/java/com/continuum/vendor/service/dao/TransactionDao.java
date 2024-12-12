package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.Transaction;
import com.continuum.vendor.service.repository.vendor.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionDao {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByStatusAndCategory(String status, String category){
        return transactionRepository.findByStatusAndDataCategory(status, category);
    }
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.getReferenceById(transactionId);
    }

    public Page<Transaction> getAllTransaction(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public void updateStatusAndDescriptionByTransactionId(UUID transactionId, String status, String description) {
        transactionRepository.updateStatusAndDescriptionById(transactionId, status, description);
    }

    public boolean checkRecordExisitsForCurrentStartEndDateWithStatusAndCategory(LocalDateTime startDateTime, LocalDateTime endDateTime, String status, String dataCategory) {
        return transactionRepository.checkRecordExisitsForCurrentStartEndDateWithStatusAndCategory(startDateTime, endDateTime, status, dataCategory);
    }
}
