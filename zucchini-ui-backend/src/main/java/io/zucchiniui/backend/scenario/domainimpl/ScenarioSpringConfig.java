package io.zucchiniui.backend.scenario.domainimpl;

import io.zucchiniui.backend.scenario.dao.ScenarioDAO;
import io.zucchiniui.backend.scenario.domain.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScenarioSpringConfig {

    @Autowired
    private ScenarioDAO scenarioDAO;

    @Bean
    public ScenarioRepository scenarioRepository() {
        return new ScenarioRepositoryImpl(scenarioDAO);
    }

}
