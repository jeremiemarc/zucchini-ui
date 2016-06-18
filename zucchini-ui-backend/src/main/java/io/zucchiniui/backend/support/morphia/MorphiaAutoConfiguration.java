package io.zucchiniui.backend.support.morphia;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Morphia.class)
@EnableConfigurationProperties(MorphiaProperties.class)
public class MorphiaAutoConfiguration {

    @Autowired
    private MorphiaProperties morphiaProperties;

    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongoClient() {
        return new MongoClient(morphiaProperties.getClientURI());
    }

    @Bean
    @ConditionalOnMissingBean
    public Morphia morphia() {
        final Morphia morphia = new Morphia();
        morphia.getMapper().getConverters().addConverter(ZonedDateTimeConverter.class);
        return morphia;
    }

    @Bean
    @ConditionalOnMissingBean
    public Datastore datastore() {
        return morphia().createDatastore(mongoClient(), morphiaProperties.getClientURI().getDatabase());
    }

    @Bean
    @ConditionalOnWebApplication
    public MongoHealthIndicator mongoHealthIndicator() {
        return new MongoHealthIndicator(mongoClient(), morphiaProperties.getClientURI());
    }

}
