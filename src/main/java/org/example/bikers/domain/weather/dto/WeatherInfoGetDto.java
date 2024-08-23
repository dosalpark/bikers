package org.example.bikers.domain.weather.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;

@Getter
public class WeatherInfoGetDto {

    @SerializedName("response")
    private Response response;

    @Getter
    public static class Response {

        @SerializedName("body")
        private Body body;

        @Getter
        public static class Body {

            @SerializedName("items")
            private Items items;

            @Getter
            public static class Items {

                @SerializedName("item")
                private List<WeatherItemDto> itemList;
            }
        }
    }

}
