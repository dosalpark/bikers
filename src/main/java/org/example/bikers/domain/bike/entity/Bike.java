package org.example.bikers.domain.bike.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bikes")
@NoArgsConstructor
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long bikeModelId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String bikeSerialNumber;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column
    private LocalDate saleDate;

    @Column
    private String image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BikeStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;


    public Bike(Long memberId, Long bikeModelId, String nickName, String bikeSerialNumber,
        int mileage, LocalDate purchaseDate) {
        this.memberId = memberId;
        this.bikeModelId = bikeModelId;
        this.nickName = nickName;
        this.bikeSerialNumber = bikeSerialNumber;
        this.mileage = mileage;
        this.purchaseDate = purchaseDate;
        this.status = BikeStatus.HOLD;
        this.createdAt = LocalDateTime.now();
    }

    public void updateMileage(int mileage) {
        this.mileage = mileage;
        this.modifiedAt = LocalDateTime.now();
    }
}
