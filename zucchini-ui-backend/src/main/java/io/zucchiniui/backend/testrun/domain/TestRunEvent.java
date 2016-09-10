package io.zucchiniui.backend.testrun.domain;

import io.zucchiniui.backend.support.ddd.events.AbstractDomainEvent;

public abstract class TestRunEvent extends AbstractDomainEvent<TestRun, String> {

    public TestRunEvent(String entityId) {
        super(TestRun.class, entityId);
    }

}
