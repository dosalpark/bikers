package org.example.bikers.global.storage.service;

import java.io.IOException;
import org.example.bikers.global.storage.dto.StorageGetFileUrlRequestDto;
import org.example.bikers.global.storage.dto.StorageGetFileUrlResponseDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    StorageGetFileUrlResponseDto uploadFile(MultipartFile file) throws IOException;

    ByteArrayResource downloadFile(StorageGetFileUrlRequestDto requestDto) throws IOException;

}
