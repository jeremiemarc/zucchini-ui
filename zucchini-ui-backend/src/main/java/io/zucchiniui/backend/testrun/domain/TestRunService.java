package io.zucchiniui.backend.testrun.domain;

public interface TestRunService {

    void deleteById(String testRunId);

    void setLocked(TestRun testRun, boolean locked);

}
