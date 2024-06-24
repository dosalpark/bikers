package org.example.bikers.global.storage.service;

import static org.example.bikers.global.exception.ErrorCode.FILE_NOT_SELECTED;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.example.bikers.global.storage.dto.StorageGetFileUrlRequestDto;
import org.example.bikers.global.storage.dto.StorageGetFileUrlResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class LocalStorageService implements StorageService {

    @Value("${file.upload-dir}")
    private String fileStorageLocation;

    @Override
    public StorageGetFileUrlResponseDto uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new NotFoundException(FILE_NOT_SELECTED);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("fileName = " + fileName);

        Path fileLocation = Paths.get(fileStorageLocation).resolve(fileName);

        Files.copy(file.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloads/")
            .path(fileName)
            .toUriString();
        return StorageGetFileUrlResponseDto.builder().fileUrl(fileDownloadUri).build();
    }

    @Override
    public ByteArrayResource downloadFile(StorageGetFileUrlRequestDto requestDto)
        throws IOException {
        Path path = Paths.get(fileStorageLocation + requestDto.getFileUrl()).normalize();
        byte[] file = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(file);
        return resource;
    }

}
