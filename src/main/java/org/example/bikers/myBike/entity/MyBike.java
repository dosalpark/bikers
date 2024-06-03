package org.example.bikers.myBike.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "myBikes")
public class MyBike {

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
    private LocalDate purchaseDate;

    @Column
    private LocalDate saleDate;

    @Column(nullable = false)
    private int mileage;

    @Column
    private String image;

    @Column
    private boolean isOwnership;

}
