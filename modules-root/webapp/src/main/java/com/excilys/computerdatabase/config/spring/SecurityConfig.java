package com.excilys.computerdatabase.config.spring;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String REALM_NAME = "Contacts Realm via Digest Authentication";

    @Autowired
    UserDetailsService userDetailsService;

    /*@Bean
    public UserDetailsService userDetailsService() {
        Properties users = new Properties();
        users.setProperty("user", "pwd" + ",ROLE_USER");
        users.setProperty("admin", "admin" + ",ROLE_USER,ROLE_ADMIN");
        return new InMemoryUserDetailsManager(users);
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        LOG.info("Spring Security configuration 1 ...");
        builder.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOG.info("Spring Security configuration 2 ...");
        DigestAuthenticationEntryPoint authenticationEntryPoint = new DigestAuthenticationEntryPoint();
        authenticationEntryPoint.setKey("acegi");
        authenticationEntryPoint.setRealmName(REALM_NAME);

        DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setUserDetailsService(userDetailsService());

        http.authorizeRequests().antMatchers("/resources/**", "/signup", "/about").permitAll().and().formLogin()
                .loginPage("/login").usernameParameter("username").passwordParameter("password").and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    @PostConstruct
    public void initApp() {
        LOG.info("Spring Security configuration ...");
    }
}