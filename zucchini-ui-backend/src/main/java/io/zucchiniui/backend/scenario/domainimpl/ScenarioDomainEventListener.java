package io.zucchiniui.backend.scenario.domainimpl;

import com.google.common.eventbus.Subscribe;
import io.zucchiniui.backend.feature.domain.FeatureDeletedEvent;
import io.zucchiniui.backend.feature.domain.FeatureTagsUpdatedEvent;
import io.zucchiniui.backend.scenario.domain.ScenarioService;
import io.zucchiniui.backend.testrun.domain.TestRunDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScenarioDomainEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioDomainEventListener.class);

    private final ScenarioService scenarioService;

    public ScenarioDomainEventListener(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @Subscribe
    public void onTestRunDeleted(TestRunDeletedEvent event) {
        LOGGER.info("Deleting scenarii by test run id: {}", event.getEntityId());
        scenarioService.deleteByTestRunId(event.getEntityId());
    }

    @Subscribe
    public void onFeatureDeleted(FeatureDeletedEvent event) {
        LOGGER.info("Deleting scenarii by feature id: {}", event.getEntityId());
        scenarioService.deleteByFeatureId(event.getEntityId());
    }

    @Subscribe
    public void onFeatureTagsUpdated(FeatureTagsUpdatedEvent event) {
        LOGGER.info("Updating tags for scenarii with feature id {}: {}", event.getEntityId(), event.getTags());
        scenarioService.updateTagsForScenariiWithFeatureId(event.getEntityId(), event.getTags());
    }

}
