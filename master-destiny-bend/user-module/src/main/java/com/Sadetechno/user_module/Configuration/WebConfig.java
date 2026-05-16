package com.Sadetechno.user_module.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure this matches your file path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:static/uploads/");

    }
}

