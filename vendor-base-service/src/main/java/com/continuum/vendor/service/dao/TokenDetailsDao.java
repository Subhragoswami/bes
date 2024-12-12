package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.TokenDetails;
import com.continuum.vendor.service.repository.vendor.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TokenDetailsDao {
    private final TokenRepository tokenRepository;

    public TokenDetails saveTokenDetails(TokenDetails tokenDetails){
        return tokenRepository.save(tokenDetails);
    }

    public Optional<TokenDetails> getTokenDetails(){
        return tokenRepository.findAll().stream().findFirst();
    }

}
