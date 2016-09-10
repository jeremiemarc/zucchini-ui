package io.zucchiniui.backend.feature.domain;

public final class FeatureDeletedEvent extends FeatureEvent {

    public FeatureDeletedEvent(String entityId) {
        super(entityId);
    }

}
