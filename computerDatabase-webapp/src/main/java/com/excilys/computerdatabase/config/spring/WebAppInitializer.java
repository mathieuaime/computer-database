package com.excilys.computerdatabase.config.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(DAOConfig.class, ServiceConfig.class, SecurityConfig.class, BindingConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register(MvcConfig.class, RESTConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet",
                new DispatcherServlet(dispatcherServlet));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.setInitParameter("throwExceptionIfNoHandlerFound", "true");

        rootContext.setServletContext(servletContext);
        rootContext.refresh();
    }
}