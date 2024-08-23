package org.example.bikers.domain.weather.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class WeatherItemDto {

    @SerializedName("baseDate")
    private String baseDate;

    @SerializedName("baseTime")
    private String baseTime;

    @SerializedName("category")
    private String category;

    @SerializedName("fcstDate")
    private String fcstDate;

    @SerializedName("fcstTime")
    private String fcstTime;

    @SerializedName("fcstValue")
    private String fcstValue;

    @SerializedName("nx")
    private int nx;

    @SerializedName("ny")
    private int ny;
}
