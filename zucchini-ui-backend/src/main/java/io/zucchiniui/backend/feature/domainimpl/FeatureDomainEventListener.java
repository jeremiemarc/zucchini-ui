package io.zucchiniui.backend.feature.domainimpl;

import com.google.common.eventbus.Subscribe;
import io.zucchiniui.backend.feature.domain.FeatureService;
import io.zucchiniui.backend.scenario.domain.ScenarioCreatedEvent;
import io.zucchiniui.backend.scenario.domain.ScenarioDeletedEvent;
import io.zucchiniui.backend.scenario.domain.ScenarioStatusChangedEvent;
import io.zucchiniui.backend.testrun.domain.TestRunDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeatureDomainEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureDomainEventListener.class);

    private final FeatureService featureService;

    public FeatureDomainEventListener(FeatureService featureService) {
        this.featureService = featureService;
    }

    @Subscribe
    public void onScenarioCreated(ScenarioCreatedEvent event) {
        recomputeFeatureStatus(event.getFeatureId());
    }

    @Subscribe
    public void onScenarioStatusChanged(ScenarioStatusChangedEvent event) {
        recomputeFeatureStatus(event.getFeatureId());
    }

    @Subscribe
    public void onScenarioDeleted(ScenarioDeletedEvent event) {
        recomputeFeatureStatus(event.getFeatureId());
    }

    @Subscribe
    public void onTestRunDeleted(TestRunDeletedEvent event) {
        LOGGER.info("Deleting features by test run id: {}", event.getEntityId());
        featureService.deleteByTestRunId(event.getEntityId());
    }

    private void recomputeFeatureStatus(String featureId) {
        LOGGER.info("Updating status of feature {}", featureId);
        featureService.updateStatusFromScenarii(featureId);
    }

}
