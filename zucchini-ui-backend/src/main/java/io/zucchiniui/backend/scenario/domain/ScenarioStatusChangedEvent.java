package io.zucchiniui.backend.scenario.domain;

import com.google.common.base.MoreObjects;

public final class ScenarioStatusChangedEvent extends ScenarioEvent {

    private final String featureId;

    private final ScenarioStatus oldValue;

    private final ScenarioStatus newValue;

    public ScenarioStatusChangedEvent(String entityId, String featureId, ScenarioStatus oldValue, ScenarioStatus newValue) {
        super(entityId);
        this.featureId = featureId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getFeatureId() {
        return featureId;
    }

    public ScenarioStatus getOldValue() {
        return oldValue;
    }

    public ScenarioStatus getNewValue() {
        return newValue;
    }

    @Override
    protected void enrichToString(MoreObjects.ToStringHelper toStringHelper) {
        toStringHelper
            .add("featureId", featureId)
            .add("oldValue", oldValue)
            .add("newValue", newValue);
    }

}
