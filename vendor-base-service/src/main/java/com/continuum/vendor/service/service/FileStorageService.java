package com.continuum.vendor.service.service;

import com.continuum.vendor.service.entity.vendor.FileData;
import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.model.response.FileDataResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.repository.vendor.FilesRepository;
import com.continuum.vendor.service.util.BinaryToBase64;
import com.continuum.vendor.service.util.ErrorConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;
import static com.continuum.vendor.service.util.ErrorConstants.INVALID_ERROR_CODE;
import static com.continuum.vendor.service.util.ErrorConstants.INVALID_ERROR_CODE_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    private final FilesRepository filesRepository;
    private final ObjectMapper mapper;

    public ResponseDto<FileDataResponse> saveFile(MultipartFile file, String identifier, String fileType) {
        log.info("Starting file save operation for identifier: {}, fileType: {}", identifier, fileType);
        try {
            Optional<FileData> fileDataFromDB = filesRepository.findByTypeAndIdentifier(fileType, identifier);
            FileData fileData;
            if (fileDataFromDB.isPresent()) {
                log.info("File data found in database for identifier: {}, fileType: {}", identifier, fileType);
                fileData = fileDataFromDB.get();
                fileData.setData(file.getBytes());
                fileData.setName(file.getOriginalFilename());
                fileData.setContentType(file.getContentType());
            } else {
                log.info("No existing file data found, creating new file data for identifier: {}, fileType: {}", identifier, fileType);
                fileData = FileData.builder().type(fileType).identifier(identifier).contentType(file.getContentType())
                        .data(file.getBytes()).name(file.getOriginalFilename())
                        .build();
            }

            FileData savedFileData = filesRepository.save(fileData);
            log.info("File data saved successfully for identifier: {}, fileType: {}", identifier, fileType);
            FileDataResponse fileDataResponse = mapper.convertValue(savedFileData, FileDataResponse.class);
            log.info("Mapped saved file data to response DTO");
            return ResponseDto.<FileDataResponse>builder()
                    .status(RESPONSE_SUCCESS)
                    .data(List.of(fileDataResponse))
                    .build();
        } catch (IOException e) {
            log.error("Error saving file data for identifier: {}, fileType: {}", identifier, fileType, e.getMessage());
            throw new VendorException(INVALID_ERROR_CODE, INVALID_ERROR_CODE_MESSAGE);
        }
    }


    public ResponseDto<FileDataResponse> getFileData(UUID id){
        log.info("Fetching file data for ID: {}", id);
        FileData fileData = filesRepository.findById(id).orElseThrow(
                () -> new VendorException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_CODE_MESSAGE, "File Id")));
        log.info("Converting fileData to Base64");
        String base64Encoded = BinaryToBase64.convertToBase64(fileData.getData());
        FileDataResponse fileDataResponse = mapper.convertValue(fileData, FileDataResponse.class);
        fileDataResponse.setData(base64Encoded);
        log.info("Mapped file data to response DTO for ID: {}", id);
        return ResponseDto.<FileDataResponse>builder().status(RESPONSE_SUCCESS).data(List.of(fileDataResponse)).build();
    }
}
