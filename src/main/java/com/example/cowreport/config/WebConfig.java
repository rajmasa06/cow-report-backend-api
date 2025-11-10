package com.example.cowreport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/uploads/**' (URL Path) ko disk par './uploads/' folder se map karen
        // Ye wohi path hai jo FileStorageService return karta hai: "/uploads/"
        registry.addResourceHandler("/uploads/**")
                // file:/ is required for file system path
                // .toAbsolutePath().normalize() FileStorageService se aa raha hai
                .addResourceLocations("file:./uploads/"); 
    }
}

