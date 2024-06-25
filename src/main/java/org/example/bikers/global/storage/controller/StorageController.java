package org.example.bikers.global.storage.controller;

import java.io.IOException;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.storage.dto.StorageDeleteFileUrlRequestDto;
import org.example.bikers.global.storage.dto.StorageGetFileUrlRequestDto;
import org.example.bikers.global.storage.dto.StorageGetFileUrlResponseDto;
import org.example.bikers.global.storage.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<StorageGetFileUrlResponseDto>> uploadFile(
        @RequestPart(value = "file") MultipartFile file) throws IOException {
        StorageGetFileUrlResponseDto fileUrl = storageService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.success(fileUrl));
    }

    @GetMapping
    public ResponseEntity<ByteArrayResource> downloadFile(
        @RequestBody StorageGetFileUrlRequestDto requestDto) throws IOException {
        ByteArrayResource resource = storageService.downloadFile(requestDto);
        return ResponseEntity
            .ok()
            .contentLength(resource.contentLength())
            .header("Content-type", "application/octet-stream")
            .header("Content-disposition",
                "attachment; filename=\"" + URLEncoder.encode(requestDto.getFileUrl(), "utf-8")
                    + "\"").body(resource);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestBody StorageDeleteFileUrlRequestDto requestDto)
        throws IOException {
        storageService.deleteFile(requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
