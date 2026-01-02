package com.til.csweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 MVC 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 이미지 서빙은 ImageController에서 처리
}
