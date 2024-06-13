package org.example.bikers.domain.bikeModel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bikeModels")
@NoArgsConstructor
public class BikeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BikeCategory bikeCategory;

    @Column(nullable = false)
    private int displacement;

    @Column(nullable = false)
    private Long addUserId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public BikeModel(String manufacturer, String name, int year, String bikeCategory,
        int displacement, Long addUserId) {
        this.manufacturer = Manufacturer.valueOf(manufacturer);
        this.name = name;
        this.year = year;
        this.bikeCategory = BikeCategory.valueOf(bikeCategory);
        this.displacement = displacement;
        this.addUserId = addUserId;
        this.createdAt = LocalDateTime.now();
    }
}
