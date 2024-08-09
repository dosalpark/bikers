package org.example.bikers.domain.bike.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bike.service.BikeExaminationDateBeforeMonthResponseDto;

@Getter
@RequiredArgsConstructor
public class BikeExaminationDateBeforeMonthEventDto {

    private final List<BikeExaminationDateBeforeMonthResponseDto> examinationDateBeforeMonthList;

}
