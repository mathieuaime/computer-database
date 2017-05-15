package com.excilys.computerdatabase.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class InfrastructureConfig {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfrastructureConfig.class);

    @Autowired
    private DataSource dataSource;
    
    public DataSource getDataSource() {
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
        LOG.debug("Spring Infrastructure configuring...");
        LOG.debug(dataSource.toString());
    }
    
}
