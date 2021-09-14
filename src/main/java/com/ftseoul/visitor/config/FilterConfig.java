package com.ftseoul.visitor.config;

import com.ftseoul.visitor.filter.LogbackMDCFilter;
import com.ftseoul.visitor.filter.MultiReadableHttpServletRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean multireadableFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MultiReadableHttpServletRequestFilter multiReadableHttpServletRequestFilter = new MultiReadableHttpServletRequestFilter();
        registrationBean.setFilter(multiReadableHttpServletRequestFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean logbackMDCFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LogbackMDCFilter logbackMDCFilter = new LogbackMDCFilter();
        registrationBean.setFilter(logbackMDCFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }

}