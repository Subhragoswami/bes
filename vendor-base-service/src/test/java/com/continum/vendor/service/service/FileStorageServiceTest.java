package com.continum.vendor.service.service;

import com.continuum.vendor.service.entity.vendor.FileData;
import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.model.response.FileDataResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.repository.vendor.FilesRepository;
import com.continuum.vendor.service.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileStorageServiceTest {

    @Mock
    private FilesRepository filesRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveFileNewFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("Test content".getBytes());
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getContentType()).thenReturn("text/plain");
        when(filesRepository.findByTypeAndIdentifier(any(), any())).thenReturn(Optional.empty());
        when(filesRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        FileDataResponse fileDataResponse = new FileDataResponse();
        when(mapper.convertValue(any(), eq(FileDataResponse.class))).thenReturn(fileDataResponse);

        ResponseDto<FileDataResponse> response = fileStorageService.saveFile(file, "test-id", "test-type");

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(fileDataResponse, response.getData().get(0));
    }

    @Test
    void testSaveFileExistingFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("Test content".getBytes());
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getContentType()).thenReturn("text/plain");

        FileData existingFileData = new FileData();
        existingFileData.setId(UUID.randomUUID());
        existingFileData.setIdentifier("test-id");
        existingFileData.setType("test-type");

        when(filesRepository.findByTypeAndIdentifier(any(), any())).thenReturn(Optional.of(existingFileData));
        when(filesRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        FileDataResponse fileDataResponse = new FileDataResponse();
        when(mapper.convertValue(any(), eq(FileDataResponse.class))).thenReturn(fileDataResponse);

        ResponseDto<FileDataResponse> response = fileStorageService.saveFile(file, "test-id", "test-type");

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(fileDataResponse, response.getData().get(0));
    }

    @Test
    void testSaveFileException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException());

        VendorException exception = assertThrows(VendorException.class, () -> {
            fileStorageService.saveFile(file, "test-id", "test-type");
        });

        assertEquals("{0} is Invalid.", exception.getErrorMessage());
    }

    @Test
    void testGetFileDataSuccess() {
        UUID id = UUID.randomUUID();
        FileData fileData = new FileData();
        fileData.setData("Test content".getBytes());

        when(filesRepository.findById(any())).thenReturn(Optional.of(fileData));

        FileDataResponse fileDataResponse = new FileDataResponse();
        when(mapper.convertValue(any(), eq(FileDataResponse.class))).thenReturn(fileDataResponse);

        ResponseDto<FileDataResponse> response = fileStorageService.getFileData(id);

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(fileDataResponse, response.getData().get(0));
    }

    @Test
    void testGetFileDataNotFound() {
        UUID id = UUID.randomUUID();
        when(filesRepository.findById(any())).thenReturn(Optional.empty());

        VendorException exception = assertThrows(VendorException.class, () -> {
            fileStorageService.getFileData(id);
        });

        assertEquals("File Id is Invalid.", exception.getErrorMessage());
    }
}
