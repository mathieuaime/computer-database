package com.excilys.computerdatabase.config.spring;

import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.excilys.computerdatabase.mappers")
public class BindingConfig {
    private static final Logger LOG = LoggerFactory.getLogger(BindingConfig.class);
    
    @Bean
    public DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /**
     * Application custom initialization code.
     * <p/>
     * Spring profiles can be configured with a system property
     * -Dspring.profiles.active=javaee
     * <p/>
     */
    @PostConstruct
    public void initApp() {
        LOG.info("Binding configuring...");
    }

}