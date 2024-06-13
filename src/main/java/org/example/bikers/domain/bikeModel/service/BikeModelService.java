package org.example.bikers.domain.bikeModel.service;

import lombok.RequiredArgsConstructor;
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
