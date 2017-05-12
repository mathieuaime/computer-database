package com.excilys.computerdatabase.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration of the business, persistence and security layers.  
 */
@Configuration
@ComponentScan(basePackages = "com.excilys.computerdatabase")
@Import(value = { 
        DataSourceConfig.class,
        InfrastructureConfig.class,
        RepositoryConfig.class,
        ServiceConfig.class,
        SecurityConfig.class
} )
public class MainConfig {
    
    private static final Logger LOG = LoggerFactory.getLogger(MainConfig.class);
    
    @Bean
    public Company company() {
        return new Company();
    }
    
    @Bean
    public Computer computer() {
        return new Computer("test");
    }

    @Autowired
    private Environment env;

    /**
     * Application custom initialization code.
     * <p/>
     * Spring profiles can be configured with a system property
     * -Dspring.profiles.active=javaee
     * <p/>
     */
    @PostConstruct
    public void initApp() {
        LOG.debug("Looking for Spring profiles...");
        if (env.getActiveProfiles().length == 0) {
            LOG.info("No Spring profile configured, running with default configuration.");
        } else {
            for (String profile : env.getActiveProfiles()) {
                LOG.info("Detected Spring profile: {}", profile);
            }
        }
    }
}
