package org.example.bikers.domain.weather.service;

import com.google.gson.Gson;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.weather.dto.WeatherGetResponseDto;
import org.example.bikers.domain.weather.dto.WeatherInfoGetDto;
import org.example.bikers.domain.weather.dto.WeatherItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${openapi.data-go-kr.key}")
    private String secretKey;

    private final String[] regions = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기 용인",
        "경기 양평", "충북", "충남", "전북", "전남", "경북", "경남", "강원", "제주"};
    private final int[][] regionsXY = {{60, 127}, {98, 74}, {89, 90}, {55, 124}, {58, 74},
        {67, 100}, {102, 84}, {66, 103}, {62, 121}, {69, 125}, {69, 106}, {68, 100}, {63, 89},
        {51, 67}, {87, 106}, {91, 77}, {73, 134}, {52, 38}};


    public List<WeatherGetResponseDto> getWeathers() {
        LocalDate today = LocalDate.now();
        //기상청 단기예보시간 02시부터 3시간단위로 발표
        int hour = LocalDateTime.now().getHour();
        if (hour < 2) {
            hour += 24;
            today = today.minusDays(1L);
        }
        hour = hour - ((hour + 1) % 3);
        String strToday = today.toString().replace("-", "");
        String strHour = hour + "00";

        List<WeatherGetResponseDto> weatherGetResponseDtoList = new ArrayList<>();

        for (int i = 0; i < regions.length; i++) {
            URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr")
                .path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("serviceKey", secretKey)
                .queryParam("dataType", "JSON")
                .queryParam("numOfRows", 12)
                .queryParam("base_date", strToday)
                .queryParam("base_time", strHour)
                .queryParam("nx", regionsXY[i][0])
                .queryParam("ny", regionsXY[i][1])
                .build(true)
                .toUri();

            RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, uri);
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            if (Objects.requireNonNull(response.getBody()).contains("errMsg")) {
                String errMsg = response.getBody().split("<returnAuthMsg>|</returnAuthMsg>")[1];
                String errCode = response.getBody()
                    .split("<returnReasonCode>|</returnReasonCode>")[1];
                throw new IllegalArgumentException("ErrMsg: " + errMsg + "/ ErrCode: " + errCode);
            }
            WeatherInfoGetDto weatherInfo = gson.fromJson(response.getBody(),
                WeatherInfoGetDto.class);

            WeatherGetResponseDto oneRegionWeather = getRequiredWeatherData(weatherInfo, i);
            weatherGetResponseDtoList.add(oneRegionWeather);
        }

        return weatherGetResponseDtoList;
    }

    private WeatherGetResponseDto getRequiredWeatherData(WeatherInfoGetDto weatherInfo, int i) {
        String precipitationProbability = "";
        String precipitationType = "";
        String skyCondition = "";
        String nowTemp = "";

        for (WeatherItemDto item : weatherInfo.getResponse().getBody().getItems().getItemList()) {
            switch (item.getCategory()) {
                case "POP":
                    precipitationProbability = item.getFcstValue() + "%";
                    break;
                case "PTY":
                    if (item.getFcstValue().equals("0")) {
                        precipitationType = "없음";
                    }
                    if (item.getFcstValue().equals("1")) {
                        precipitationType = "비";
                    }
                    if (item.getFcstValue().equals("2")) {
                        precipitationType = "비/눈";
                    }
                    if (item.getFcstValue().equals("3")) {
                        precipitationType = "눈";
                    }
                    if (item.getFcstValue().equals("4")) {
                        precipitationType = "소나기";
                    }
                    break;
                case "SKY":
                    if (item.getFcstValue().equals("1")) {
                        skyCondition = "맑음";
                    }
                    if (item.getFcstValue().equals("3")) {
                        skyCondition = "구름많음";
                    }
                    if (item.getFcstValue().equals("4")) {
                        skyCondition = "흐림";
                    }
                    break;
                case "TMP":
                    nowTemp = item.getFcstValue();
                    break;
                default:
                    continue;
            }
        }
        return new WeatherGetResponseDto(
            regions[i], precipitationProbability, precipitationType, skyCondition, nowTemp);
    }
}
