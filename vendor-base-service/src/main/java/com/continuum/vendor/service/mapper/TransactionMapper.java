package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.vendor.Transaction;
import com.continuum.vendor.service.model.response.TransactionResponse;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    private final MapperFacade mapperFacade;

    public TransactionMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Transaction.class, TransactionResponse.class)
                .byDefault()
                .register();
        this.mapperFacade = mapperFactory.getMapperFacade();
    }

    public TransactionResponse mapTransactionResponse(Transaction transaction) {
        return mapperFacade.map(transaction, TransactionResponse.class);
    }
    public List<TransactionResponse> mapTransactionResponses(List<Transaction> transaction) {
        return mapperFacade.mapAsList(transaction, TransactionResponse.class);
    }
}
