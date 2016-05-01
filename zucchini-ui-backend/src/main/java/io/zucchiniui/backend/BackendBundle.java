package io.zucchiniui.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.zucchiniui.backend.support.ddd.rest.EntityNotFoundExceptionMapper;
import io.zucchiniui.backend.support.spring.AnnotationSpringConfigBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class BackendBundle implements ConfiguredBundle<BackendConfiguration> {

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)
        ));

        // Register Spring context
        bootstrap.addBundle(new AnnotationSpringConfigBundle(BackendSpringConfig.class));

        // Configure Jackson mapper
        bootstrap.getObjectMapper()
            .registerModules(new JavaTimeModule(), new Jdk8Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void run(final BackendConfiguration configuration, final Environment environment) throws Exception {
        final FilterRegistration.Dynamic corsRegistration = environment.servlets().addFilter("cors-filter", CrossOriginFilter.class);
        corsRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        corsRegistration.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        corsRegistration.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,PATCH,POST,DELETE,HEAD");
        corsRegistration.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        corsRegistration.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");

        configuration.getMetrics().configure(environment.lifecycle(), environment.metrics());

        environment.jersey().register(new EntityNotFoundExceptionMapper());
    }

}
