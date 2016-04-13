package io.zucchiniui.backend.support.spring;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

class SpringContextManaged implements Managed {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextManaged.class);

    private final ConfigurableApplicationContext applicationContext;

    public SpringContextManaged(final ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting Spring context");
        applicationContext.start();
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Spring context");
        applicationContext.stop();
        applicationContext.close();
    }

}
