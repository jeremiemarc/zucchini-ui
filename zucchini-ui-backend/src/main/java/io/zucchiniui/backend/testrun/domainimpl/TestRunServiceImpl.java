package io.zucchiniui.backend.testrun.domainimpl;

import io.zucchiniui.backend.testrun.domain.TestRun;
import io.zucchiniui.backend.testrun.domain.TestRunRepository;
import io.zucchiniui.backend.testrun.domain.TestRunService;
import org.springframework.stereotype.Component;

@Component
class TestRunServiceImpl implements TestRunService {

    private final TestRunRepository testRunRepository;

    public TestRunServiceImpl(final TestRunRepository testRunRepository) {
        this.testRunRepository = testRunRepository;
    }

    @Override
    public void deleteById(final String testRunId) {
        final TestRun testRun = testRunRepository.getById(testRunId);
        testRunRepository.delete(testRun);
    }

}
