package org.example.bikers.domain.bikeModel.service;

import static org.example.bikers.global.exception.ErrorCode.NO_BIKE_MODEL_FOUND;
import static org.example.bikers.global.exception.ErrorCode.NO_MATCHING_CATEGORY;
import static org.example.bikers.global.exception.ErrorCode.NO_MATCHING_MANUFACTURER;
import static org.example.bikers.global.exception.ErrorCode.NO_SUCH_BIKE_MODEL;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.bikeModel.dto.BikeModelGetResponseDto;
import org.example.bikers.domain.bikeModel.entity.BikeCategory;
import org.example.bikers.domain.bikeModel.entity.BikeModel;
import org.example.bikers.domain.bikeModel.entity.Manufacturer;
import org.example.bikers.domain.bikeModel.repository.BikeModelRepository;
import org.example.bikers.global.exception.customException.NotFoundException;
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
            throw new NotFoundException(NO_MATCHING_MANUFACTURER);
        }
        if (!isValidBikeCategory(bikeCategory)) {
            throw new NotFoundException(NO_MATCHING_CATEGORY);
        }
        if (bikeModelRepository.existsBikeModelByNameEqualsAndYearEquals(name, year)) {
            throw new IllegalArgumentException("BM000003");
        }

        BikeModel newModel = new BikeModel(manufacturer, name, year, bikeCategory, displacement,
            userId);
        bikeModelRepository.save(newModel);
    }

    @Transactional(readOnly = true)
    public BikeModelGetResponseDto getBikeModelById(Long bikeModelId) {
        BikeModel getModel = bikeModelRepository.findById(bikeModelId).orElseThrow(
            () -> new NotFoundException(NO_SUCH_BIKE_MODEL)
        );
        return converterToDto(getModel);
    }

    @Transactional(readOnly = true)
    public List<BikeModelGetResponseDto> getBikeModels() {
        List<BikeModel> getModels = bikeModelRepository.findAll();
        if (getModels.isEmpty()) {
            throw new NotFoundException(NO_BIKE_MODEL_FOUND);
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
