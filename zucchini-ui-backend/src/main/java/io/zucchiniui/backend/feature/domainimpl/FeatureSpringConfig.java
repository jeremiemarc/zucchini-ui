package io.zucchiniui.backend.feature.domainimpl;

import io.zucchiniui.backend.feature.dao.FeatureDAO;
import io.zucchiniui.backend.feature.domain.FeatureRepository;
import io.zucchiniui.backend.support.ddd.events.EventRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureSpringConfig {

    @Autowired
    private EventRepositoryFactory eventRepositoryFactory;

    @Autowired
    private FeatureDAO featureDAO;

    @Bean
    public FeatureRepository featureRepository() {
        return eventRepositoryFactory.createRepository(FeatureRepositoryImpl.class, featureDAO);
    }

}
