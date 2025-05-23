package com.aibasedcrosswordcreator.configmanagementservice.config;

import com.aibasedcrosswordcreator.configmanagementservice.interceptor.ConfigPropertyInterceptor;
import com.aibasedcrosswordcreator.configmanagementservice.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final WebClient.Builder builder;
    private final RequestLoggingInterceptor requestLoggingInterceptor;
    private final ConfigPropertyInterceptor configPropertyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ConfigPropertyInterceptor(builder))
                .addPathPatterns("/api/config-management/property");
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(configPropertyInterceptor)
                .addPathPatterns("/**");
    }
}
