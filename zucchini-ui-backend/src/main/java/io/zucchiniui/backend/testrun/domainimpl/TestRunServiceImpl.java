package io.zucchiniui.backend.testrun.domainimpl;

import io.zucchiniui.backend.comment.domain.CommentReferenceType;
import io.zucchiniui.backend.comment.domain.CommentRepository;
import io.zucchiniui.backend.feature.domain.FeatureService;
import io.zucchiniui.backend.testrun.domain.TestRun;
import io.zucchiniui.backend.testrun.domain.TestRunRepository;
import io.zucchiniui.backend.testrun.domain.TestRunService;
import org.springframework.stereotype.Component;

@Component
class TestRunServiceImpl implements TestRunService {

    private final TestRunRepository testRunRepository;

    private final FeatureService featureService;

    private final CommentRepository commentRepository;

    public TestRunServiceImpl(
        final TestRunRepository testRunRepository,
        final FeatureService featureService,
        final CommentRepository commentRepository
    ) {
        this.testRunRepository = testRunRepository;
        this.featureService = featureService;
        this.commentRepository = commentRepository;
    }

    @Override
    public void deleteById(final String testRunId) {
        featureService.deleteByTestRunId(testRunId);

        final TestRun testRun = testRunRepository.getById(testRunId);
        testRunRepository.delete(testRun);

        commentRepository.deleteByReference(CommentReferenceType.TEST_RUN_ID.createReference(testRunId));
    }

}
