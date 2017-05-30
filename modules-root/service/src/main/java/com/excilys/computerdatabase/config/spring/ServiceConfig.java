package com.excilys.computerdatabase.config.spring;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@ComponentScan(basePackages = "com.excilys.computerdatabase.services")
@EnableTransactionManagement
public class ServiceConfig implements TransactionManagementConfigurer {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ServiceConfig.class);

    @Autowired
    private DataSource dataSource;

    /**
     * Password Encoder.
     * @return Password Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Transaction Manager.
     * @return Transaction Manager
     */
    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

    @PostConstruct
    public void initApp() {
        LOG.info("Spring Service configuring...");
    }
}
