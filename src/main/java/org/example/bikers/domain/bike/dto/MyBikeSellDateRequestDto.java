package org.example.bikers.domain.bike.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MyBikeSellDateRequestDto {

    @NotNull(message = "날짜를 입력해주세요")
    @PastOrPresent(message = "오늘 이후의 날짜를 입력할 수 없습니다.")
    private LocalDate sellDate;

}
