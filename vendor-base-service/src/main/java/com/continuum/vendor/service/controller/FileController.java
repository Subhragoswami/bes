package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.response.FileDataResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<FileDataResponse> saveFile(@RequestPart MultipartFile file, @RequestPart String identifier, @RequestPart String fileType) {
        log.info("Getting the file data for the file:{}", file.getName());
        return fileStorageService.saveFile(file, identifier, fileType);

    }
    @GetMapping("/{id}")
    public ResponseDto<FileDataResponse> getFileData(@PathVariable UUID id){
        log.info("Getting the file data for the file:{}", id);
        return fileStorageService.getFileData(id);
    }
}
