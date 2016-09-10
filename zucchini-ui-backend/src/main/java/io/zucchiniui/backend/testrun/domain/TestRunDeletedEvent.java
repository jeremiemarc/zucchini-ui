package io.zucchiniui.backend.testrun.domain;

public final class TestRunDeletedEvent extends TestRunEvent {

    public TestRunDeletedEvent(String entityId) {
        super(entityId);
    }

}
