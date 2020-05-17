package com.gateway.config;

import com.gateway.filter.AccessFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Zuul配置
 *
 * @author Administrator
 */
@Configuration
public class ZuulConfig {

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
}


