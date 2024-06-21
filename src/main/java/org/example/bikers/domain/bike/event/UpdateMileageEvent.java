package org.example.bikers.domain.bike.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMileageEvent {

    private Long bikeId;
    private int preMileage;
    private int nowMileage;
    private String startPoint;
    private String goalPoint;

}
