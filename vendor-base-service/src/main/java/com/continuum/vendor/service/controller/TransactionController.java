package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.TransactionResponse;
import com.continuum.vendor.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseDto<TransactionResponse> getAllTransactions(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Getting All Transaction Details");
        return transactionService.getAllTransactions(pageable);
    }

    @GetMapping("/{transactionId}")
    public ResponseDto<TransactionResponse> getTransaction(@PathVariable(value = "transactionId") UUID transactionId) {
        log.info("Getting Transaction Details for Transaction ID:{}", transactionId);
        return transactionService.getTransactionDetails(transactionId);
    }

}
