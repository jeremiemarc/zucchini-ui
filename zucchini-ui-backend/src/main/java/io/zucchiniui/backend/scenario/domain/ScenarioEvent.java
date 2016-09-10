package io.zucchiniui.backend.scenario.domain;

import io.zucchiniui.backend.support.ddd.events.AbstractDomainEvent;

public abstract class ScenarioEvent extends AbstractDomainEvent<Scenario, String> {

    public ScenarioEvent(String entityId) {
        super(Scenario.class, entityId);
    }

}
