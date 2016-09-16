package io.zucchiniui.backend.testrun.domainimpl;

import io.zucchiniui.backend.support.ddd.events.EventRepositoryFactory;
import io.zucchiniui.backend.testrun.dao.TestRunDAO;
import io.zucchiniui.backend.testrun.domain.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRunSpringConfig {

    @Autowired
    private EventRepositoryFactory eventRepositoryFactory;

    @Autowired
    private TestRunDAO testRunDAO;

    @Bean
    public TestRunRepository testRunRepository() {
        return eventRepositoryFactory.createRepository(TestRunRepositoryImpl.class);
    }

}
