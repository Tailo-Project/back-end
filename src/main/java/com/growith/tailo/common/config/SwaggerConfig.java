package com.growith.tailo.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
// 운영 환경에서 사용 불가능
@Profile("!prod")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("Tailo API")
                .description("Tailo - 반려동물 SNS 프로젝트 API 명세서 입니다.");

        return new OpenAPI()
                .info(info);
    }
}