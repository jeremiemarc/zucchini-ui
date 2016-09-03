package io.zucchiniui.backend.testrun.domainimpl;

import io.zucchiniui.backend.feature.domain.FeatureService;
import io.zucchiniui.backend.testrun.domain.TestRun;
import io.zucchiniui.backend.testrun.domain.TestRunRepository;
import io.zucchiniui.backend.testrun.domain.TestRunService;
import org.springframework.stereotype.Component;

@Component
class TestRunServiceImpl implements TestRunService {

    private final TestRunRepository testRunRepository;

    private final FeatureService featureService;

    public TestRunServiceImpl(final TestRunRepository testRunRepository, final FeatureService featureService) {
        this.testRunRepository = testRunRepository;
        this.featureService = featureService;
    }

    @Override
    public void deleteById(final String testRunId) {
        final TestRun testRun = testRunRepository.getById(testRunId);
        testRun.ensureUnlocked();

        featureService.deleteByTestRunId(testRunId);
        testRunRepository.delete(testRun);
    }

    @Override
    public void setLocked(TestRun testRun, boolean locked) {
        testRun.setLocked(locked);
    }

}
