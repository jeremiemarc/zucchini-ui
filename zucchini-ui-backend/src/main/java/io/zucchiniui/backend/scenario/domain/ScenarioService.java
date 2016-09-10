package io.zucchiniui.backend.scenario.domain;

import java.util.Set;

public interface ScenarioService {

    void updateScenario(String scenarioId, UpdateScenarioParams params);

    Scenario tryToMergeWithExistingScenario(Scenario newScenario);

    void deleteByTestRunId(String testRunId);

    void deleteByFeatureId(String featureId);

    void updateTagsForScenariiWithFeatureId(String featureId, Set<String> tags);

}
