package org.example.bikers.domain.bikeModel.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.dto.BikeModelGetResponseDto;
import org.example.bikers.domain.bikeModel.entity.BikeCategory;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;
import org.example.bikers.domain.bikeModel.repository.BikeModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BikeModelService {

    private final BikeModelRepository bikeModelRepository;

    @Transactional
    public void createBikeModel(
        Long userId,
        String manufacturer,
        String name,
        int year,
        String bikeCategory,
        int displacement) {
        if (!isValidManufacturer(manufacturer)) {
            throw new IllegalArgumentException("일치하는 제조사가 없습니다.");
        }
        if (!isValidBikeCategory(bikeCategory)) {
            throw new IllegalArgumentException("일치하는 카테고리가 없습니다.");
        }
        if (bikeModelRepository.existsBikeModelByNameEqualsAndYearEquals(name, year)) {
            throw new IllegalArgumentException("이미 등록된 모델입니다.");
        }

        BikeModel newModel = new BikeModel(manufacturer, name, year, bikeCategory, displacement,
            userId);
        bikeModelRepository.save(newModel);
    }

    @Transactional(readOnly = true)
    public BikeModelGetResponseDto getBikeModelById(Long bikeModelId) {
        BikeModel getModel = bikeModelRepository.findById(bikeModelId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 바이크모델이 없습니다.")
        );
        return converterToDto(getModel);
    }

    @Transactional(readOnly = true)
    public List<BikeModelGetResponseDto> getBikeModels() {
        List<BikeModel> getModels = bikeModelRepository.findAll();
        if(getModels.isEmpty()){
            throw new IllegalArgumentException("조회 할 바이크모델이 없습니다.");
        }
        return converterToDtoList(getModels);
    }

    private BikeModelGetResponseDto converterToDto(BikeModel getModel) {
        return BikeModelGetResponseDto.builder()
            .bikeModelId(getModel.getId())
            .manufacturer(String.valueOf(getModel.getManufacturer()))
            .name(getModel.getName())
            .year(getModel.getYear())
            .bikeCategory(String.valueOf(getModel.getBikeCategory()))
            .displacement(getModel.getDisplacement())
            .build();
    }

    private List<BikeModelGetResponseDto> converterToDtoList(List<BikeModel> getModels) {
        List<BikeModelGetResponseDto> responseDtoList = new ArrayList<>();

        for (BikeModel getModel : getModels) {
            BikeModelGetResponseDto responseDto = BikeModelGetResponseDto.builder()
                .bikeModelId(getModel.getId())
                .manufacturer(String.valueOf(getModel.getManufacturer()))
                .name(getModel.getName())
                .year(getModel.getYear())
                .bikeCategory(String.valueOf(getModel.getBikeCategory()))
                .displacement(getModel.getDisplacement())
                .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    private boolean isValidBikeCategory(String bikeCategory) {
        for (BikeCategory one : BikeCategory.values()) {
            if (bikeCategory.equals(String.valueOf(one))) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidManufacturer(String manufacturer) {
        for (Manufacturer one : Manufacturer.values()) {
            if (manufacturer.equals(String.valueOf(one))) {
                return true;
            }
        }
        return false;
    }
}
