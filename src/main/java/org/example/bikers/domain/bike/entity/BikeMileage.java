package org.example.bikers.domain.bike.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bikeMileages")
@NoArgsConstructor
public class BikeMileage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bikeId;

    @Column(nullable = false)
    private int preMileage;

    @Column(nullable = false)
    private int nowMileage;

    @Column
    private String startPoint;

    @Column
    private String goalPoint;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    public BikeMileage(Long bikeId, int preMileage, int nowMileage, String startPoint,
        String goalPoint) {
        this.bikeId = bikeId;
        this.preMileage = preMileage;
        this.nowMileage = nowMileage;
        this.startPoint = startPoint;
        this.goalPoint = goalPoint;
        this.createdAt = LocalDateTime.now();
    }

}
