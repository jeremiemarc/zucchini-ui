package io.zucchiniui.backend.support.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

@Configuration
public class ResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConfiguration.class);

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    @ConditionalOnWebApplication
    public ResourceConfig resourceConfig() {
        return new ResourceConfig();
    }

    @PostConstruct
    public void registerJAXRSBeans() {
        applicationContext.getBeansWithAnnotation(Path.class).forEach((name, resource) -> {
            LOGGER.info("Registring resource {}", name);
            resourceConfig().register(resource);
        });
        applicationContext.getBeansWithAnnotation(Provider.class).forEach((name, provider) -> {
            LOGGER.info("Registring provider {} of type {}", name, provider.getClass());
            resourceConfig().register(provider);
        });
    }

}
