//package com.mj.tcs.config;
//
//import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
//import org.springframework.web.WebApplicationInitializer;
//
//import javax.servlet.FilterRegistration;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//
///**
// * @author Wang Zhen
// */
//public class ApplicationInitializer implements WebApplicationInitializer {
//
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        FilterRegistration.Dynamic openEntityManagerInViewFilter = servletContext.addFilter("openEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
//        openEntityManagerInViewFilter.setInitParameter("entityManagerFactoryBeanName","entityManagerFactory");
//        openEntityManagerInViewFilter.addMappingForUrlPatterns(null, false, "/*");
//    }
//}