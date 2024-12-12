package com.continuum.vendor.service.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HashUtil {

    private final ObjectMapper objectMapper;

    public String sha256HashObject(Object object) {

        try {
            // Convert the object to a JSON string
            String jsonString = objectMapper.writeValueAsString(object);

            // Convert the JSON string to a byte array
            byte[] byteArray = jsonString.getBytes();

            // Hash the byte array using SHA-256
            return DigestUtils.sha256Hex(byteArray);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }
}
