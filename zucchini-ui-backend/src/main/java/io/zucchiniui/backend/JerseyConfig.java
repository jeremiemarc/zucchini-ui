package io.zucchiniui.backend;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

@Component
public class JerseyConfig extends ResourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeansWithAnnotation(Path.class).forEach((name, resource) -> {
            LOGGER.info("Registring resource {}", name);
            register(resource);
        });
        applicationContext.getBeansWithAnnotation(Provider.class).forEach((name, provider) -> {
            LOGGER.info("Registring provider {} of type {}", name, provider.getClass());
            register(provider);
        });
    }

}
