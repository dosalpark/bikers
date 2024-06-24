package org.example.bikers.domain.storage.service;

import java.io.IOException;
import org.example.bikers.domain.storage.dto.StorageGetFileUrlResponseDto;
import org.example.bikers.domain.storage.dto.StorageGetFileUrlRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    StorageGetFileUrlResponseDto uploadFile(MultipartFile file) throws IOException;

    void deleteImage(StorageGetFileUrlRequestDto requestDto);

}
