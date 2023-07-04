package com.cos.photogramstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**") // jsp 페이지에서 /upload/** 패턴이 나오면 발동
                .addResourceLocations("file:///".concat(uploadDir))
                .setCachePeriod(60*10*6) // 1시간 동안 캐시
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
