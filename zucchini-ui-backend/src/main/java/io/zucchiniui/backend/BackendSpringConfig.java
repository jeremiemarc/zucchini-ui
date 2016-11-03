package io.zucchiniui.backend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import io.dropwizard.setup.Environment;
import io.zucchiniui.backend.comment.domainimpl.CommentDomainEventListener;
import io.zucchiniui.backend.feature.domainimpl.FeatureDomainEventListener;
import io.zucchiniui.backend.scenario.domainimpl.ScenarioDomainEventListener;
import io.zucchiniui.backend.support.ddd.events.EventRepositoryFactory;
import io.zucchiniui.backend.support.ddd.events.eventdispatcher.AllDomainEventsListener;
import io.zucchiniui.backend.support.ddd.events.eventdispatcher.GuavaDomainEventDispatcher;
import io.zucchiniui.backend.support.morphia.MorphiaDatastoreBuilder;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackageClasses = BackendSpringConfig.class)
public class BackendSpringConfig {

    @Autowired
    private BackendConfiguration configuration;

    @Autowired
    private Environment dropwizardEnvironment;

    @Autowired
    private ListableBeanFactory beanFactory;

    @Bean
    public Datastore datastore() {
        return new MorphiaDatastoreBuilder(dropwizardEnvironment)
            .withUri(configuration.getMongoUri())
            .build("mongo");
    }

    @Bean
    public ObjectMapper reportObjectMapper() {
        return dropwizardEnvironment.getObjectMapper()
            .copy()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus("domain-event-bus");
    }

    @Bean
    public GuavaDomainEventDispatcher domainEventBus() {
        return new GuavaDomainEventDispatcher(eventBus());
    }

    @Bean
    public EventRepositoryFactory eventRepositoryFactory() {
        return new EventRepositoryFactory(domainEventBus());
    }

    @PostConstruct
    public void initDomainEventListeners() {
        eventBus().register(new AllDomainEventsListener());

        beanFactory.getBeansOfType(FeatureDomainEventListener.class).values().forEach(listener -> eventBus().register(listener));
        beanFactory.getBeansOfType(ScenarioDomainEventListener.class).values().forEach(listener -> eventBus().register(listener));
        beanFactory.getBeansOfType(CommentDomainEventListener.class).values().forEach(listener -> eventBus().register(listener));
    }

}
