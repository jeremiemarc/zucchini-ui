package io.zucchiniui.backend.support.cors;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsFilterConfiguration {

    @Bean
    public FilterRegistrationBean corsFilter() {
        // TODO Should be configurable with EndpointCorsProperties ?
        FilterRegistrationBean bean = new FilterRegistrationBean(new CrossOriginFilter());
        bean.setOrder(0);
        bean.addInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,PATCH,DELETE");
        return bean;
    }

}
