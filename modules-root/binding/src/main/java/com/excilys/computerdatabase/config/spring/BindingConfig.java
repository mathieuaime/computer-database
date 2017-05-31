package com.excilys.computerdatabase.config.spring;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.excilys.computerdatabase.mappers")
public class BindingConfig {
    private static final Logger LOG = LoggerFactory.getLogger(BindingConfig.class);

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