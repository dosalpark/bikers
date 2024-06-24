package org.example.bikers.domain.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorageGetFileUrlResponseDto {

    private String fileUrl;

}
