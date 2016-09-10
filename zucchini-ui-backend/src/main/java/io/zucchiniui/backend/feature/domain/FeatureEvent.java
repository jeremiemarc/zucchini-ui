package io.zucchiniui.backend.feature.domain;

import io.zucchiniui.backend.support.ddd.events.AbstractDomainEvent;

public abstract class FeatureEvent extends AbstractDomainEvent<Feature, String> {

    public FeatureEvent(String entityId) {
        super(Feature.class, entityId);
    }

}
