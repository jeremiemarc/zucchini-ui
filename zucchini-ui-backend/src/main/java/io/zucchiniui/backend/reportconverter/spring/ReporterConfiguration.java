package io.zucchiniui.backend.reportconverter.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ReporterConfiguration {

    @Autowired
    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    @Bean
    @Qualifier("reportObjectMapper")
    public ObjectMapper reportObjectMapper() {
        return objectMapperBuilder.build();
    }

}
