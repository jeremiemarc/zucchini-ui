package io.zucchiniui.backend.scenario.domainimpl;

import io.zucchiniui.backend.scenario.dao.ScenarioDAO;
import io.zucchiniui.backend.scenario.domain.Scenario;
import io.zucchiniui.backend.scenario.domain.ScenarioRepository;
import io.zucchiniui.backend.scenario.domain.ScenarioService;
import io.zucchiniui.backend.scenario.domain.UpdateScenarioParams;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class ScenarioServiceImpl implements ScenarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioServiceImpl.class);

    private final ScenarioRepository scenarioRepository;

    private final ScenarioDAO scenarioDAO;

    public ScenarioServiceImpl(
        final ScenarioRepository scenarioRepository,
        final ScenarioDAO scenarioDAO
    ) {
        this.scenarioRepository = scenarioRepository;
        this.scenarioDAO = scenarioDAO;
    }

    @Override
    public void updateScenario(final String scenarioId, final UpdateScenarioParams params) {
        final Scenario scenario = scenarioRepository.getById(scenarioId);
        params.getStatus().ifPresent(scenario::setStatus);
        params.isReviewed().ifPresent(scenario::setReviewed);
        scenarioRepository.save(scenario);
    }

    @Override
    public Scenario tryToMergeWithExistingScenario(final Scenario newScenario) {
        return scenarioRepository.query(q -> q.withFeatureId(newScenario.getFeatureId()).withScenarioKey(newScenario.getScenarioKey()))
            .tryToFindOne()
            .map(existingScenario -> {
                LOGGER.info("Merging new scenario {} with existing feature {}", newScenario, existingScenario);
                existingScenario.mergeWith(newScenario);
                return existingScenario;
            })
            .orElse(newScenario);
    }

    @Override
    public void deleteByTestRunId(String testRunId) {
        final Query<Scenario> query = scenarioDAO.prepareTypedQuery(q -> q.withTestRunId(testRunId));
        scenarioDAO.deleteByQuery(query);
    }

    @Override
    public void deleteByFeatureId(String featureId) {
        final Query<Scenario> query = scenarioDAO.prepareTypedQuery(q -> q.withFeatureId(featureId));
        scenarioDAO.deleteByQuery(query);
    }

    @Override
    public void updateTagsForScenariiWithFeatureId(String featureId, Set<String> tags) {
        scenarioRepository.query(q -> q.withFeatureId(featureId)).forEach(scenario -> {
            scenario.updateWithExtraTags(tags);
            scenarioRepository.save(scenario);
        });
    }

}
