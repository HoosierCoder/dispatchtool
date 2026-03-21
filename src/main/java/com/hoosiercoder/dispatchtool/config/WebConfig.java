package com.hoosiercoder.dispatchtool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: HoosierCoder
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // TenantInterceptor removed in favor of TenantFilter
    // No interceptors currently registered
}
