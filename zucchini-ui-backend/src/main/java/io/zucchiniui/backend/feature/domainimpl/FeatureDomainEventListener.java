package io.zucchiniui.backend.feature.domainimpl;

import com.google.common.eventbus.Subscribe;
import io.zucchiniui.backend.feature.domain.FeatureService;
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
    public void onTestRunDeleted(TestRunDeletedEvent event) {
        LOGGER.info("Deleting features by test run id: {}", event.getEntityId());
        featureService.deleteByTestRunId(event.getEntityId());
    }

}
