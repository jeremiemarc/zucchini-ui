package io.zucchiniui.backend.feature.domainimpl;

import io.zucchiniui.backend.feature.dao.FeatureDAO;
import io.zucchiniui.backend.feature.domain.Feature;
import io.zucchiniui.backend.feature.domain.FeatureRepository;
import io.zucchiniui.backend.feature.domain.FeatureService;
import io.zucchiniui.backend.scenario.views.ScenarioStats;
import io.zucchiniui.backend.scenario.views.ScenarioViewAccess;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class FeatureServiceImpl implements FeatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    private final FeatureDAO featureDAO;

    private final ScenarioViewAccess scenarioViewAccess;

    public FeatureServiceImpl(
        final FeatureRepository featureRepository,
        final FeatureDAO featureDAO,
        final ScenarioViewAccess scenarioViewAccess
    ) {
        this.featureRepository = featureRepository;
        this.featureDAO = featureDAO;
        this.scenarioViewAccess = scenarioViewAccess;
    }

    @Override
    public void updateStatusFromScenarii(final String featureId) {
        final ScenarioStats scenarioStats = scenarioViewAccess.getStats(q -> q.withFeatureId(featureId));

        final Feature feature = featureRepository.getById(featureId);
        feature.setStatus(scenarioStats.computeFeatureStatus());
        featureRepository.save(feature);
    }

    @Override
    public void deleteByTestRunId(final String testRunId) {
        final Query<Feature> query = featureDAO.prepareTypedQuery(q -> q.withTestRunId(testRunId));
        featureDAO.deleteByQuery(query);
    }

    @Override
    public void deleteById(final String featureId) {
        final Feature feature = featureRepository.getById(featureId);
        featureRepository.delete(feature);
    }

    @Override
    public Feature tryToMergeWithExistingFeature(final Feature newFeature) {
        return featureRepository.query(q -> q.withTestRunId(newFeature.getTestRunId()).withFeatureKey(newFeature.getFeatureKey()))
            .tryToFindOne()
            .map(existingFeature -> {
                LOGGER.info("Merged new feature with key {} with existing feature {}", newFeature.getFeatureKey(), existingFeature);
                existingFeature.mergeWith(newFeature);
                return existingFeature;
            })
            .orElse(newFeature);
    }

}
