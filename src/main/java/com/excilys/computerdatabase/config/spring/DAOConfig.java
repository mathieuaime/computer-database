package com.excilys.computerdatabase.config.spring;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Depending active spring profile.
 */
@Configuration
@ComponentScan(basePackages = "com.excilys.computerdatabase.daos")
@PropertySource({ "classpath:spring/datasource.properties" })
public class DAOConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DAOConfig.class);

    @Autowired
    private Environment environment;

    /**
     * Datasource.
     * @return Datasource
     */
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        LOG.debug("Create datasource");
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));

        return dataSource;
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
        LOG.debug("Spring DataSource configuring...");
        LOG.debug("URL : " + environment.getProperty("spring.datasource.url"));
    }

}