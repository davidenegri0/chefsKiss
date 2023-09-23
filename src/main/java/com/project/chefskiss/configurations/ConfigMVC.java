package com.project.chefskiss.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ConfigMVC {
    @Configuration
    public class MVCConfiguration implements WebMvcConfigurer {

        private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
                "classpath:/static/",
        };

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        }

        @Bean
        public MultipartResolver multipartResolv() {
            return new StandardServletMultipartResolver();
        };
    }
}
