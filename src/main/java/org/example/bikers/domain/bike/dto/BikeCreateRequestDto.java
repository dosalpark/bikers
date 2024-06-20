package org.example.bikers.domain.bike.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class BikeCreateRequestDto {

    @Positive
    private Long bikeModelId;

    @NotEmpty(message = "별명을 입력해주세요")
    @Length(max = 10, message = "별명은 10자 이하로 작성해주세요")
    private String nickName;

    @NotEmpty(message = "차대번호를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{17}$", message = "정확한 차대번호를 입력해주세요")
    private String bikeSerialNumber;

    @PositiveOrZero(message = "현재 키로수를 입력해주세요")
    private int mileage;

    @NotNull(message = "구매일을 입력해주세요")
    private LocalDate purchaseDate;

    @NotNull(message = "공개여부를 설정해주세요")
    private boolean isPublic;

}
