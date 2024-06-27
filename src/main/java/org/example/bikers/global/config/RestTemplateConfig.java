package org.example.bikers.global.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        // Builder를 사용하는게 Factory에 비해서 코드가 간결하고 명확해서 사용
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(5)) //연결 최대 대기시간 설정
            .setReadTimeout(Duration.ofSeconds(5)) // 값을 반환받는 최대 대기시간 설정
            .build();
    }

}
