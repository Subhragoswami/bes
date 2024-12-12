package com.continuum.vendor.service.service;

import com.continuum.vendor.service.dao.TransactionDao;
import com.continuum.vendor.service.entity.vendor.Transaction;
import com.continuum.vendor.service.mapper.TransactionMapper;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionDao transactionDao;
    private final TransactionMapper transactionMapper;

    public ResponseDto<TransactionResponse> getTransactionDetails(UUID transactionId) {
        Transaction transaction = transactionDao.getTransactionById(transactionId);
        return ResponseDto.<TransactionResponse>builder()
                .data(List.of(transactionMapper.mapTransactionResponse(transaction)))
                .status(RESPONSE_SUCCESS)
                .build();
    }

    public ResponseDto<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactionList = transactionDao.getAllTransaction(pageable);
        return ResponseDto.<TransactionResponse>builder()
                .data(transactionMapper.mapTransactionResponses(transactionList.getContent()))
                .count((long) transactionList.getContent().size())
                .total(transactionList.getTotalElements())
                .status(RESPONSE_SUCCESS)
                .build();
    }
}
