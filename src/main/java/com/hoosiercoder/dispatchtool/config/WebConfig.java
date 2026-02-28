package com.hoosiercoder.dispatchtool.config;

import com.hoosiercoder.dispatchtool.config.interceptor.TenantInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: HoosierCoder
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // This applies the tenant check to all the API endpoints
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/api/v1/**");
    }
}
