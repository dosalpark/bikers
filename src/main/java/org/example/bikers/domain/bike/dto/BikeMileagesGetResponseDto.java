package org.example.bikers.domain.bike.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BikeMileagesGetResponseDto {

    private Long bikeMileageId;
    private int preMileage;
    private int nextMileage;
    private String startPoint;
    private String goalPoint;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

}
