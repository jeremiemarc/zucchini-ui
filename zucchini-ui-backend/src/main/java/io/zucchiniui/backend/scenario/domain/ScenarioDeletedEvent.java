package io.zucchiniui.backend.scenario.domain;

import com.google.common.base.MoreObjects;

public final class ScenarioDeletedEvent extends ScenarioEvent {

    private final String featureId;

    public ScenarioDeletedEvent(String entityId, String featureId) {
        super(entityId);
        this.featureId = featureId;
    }

    public String getFeatureId() {
        return featureId;
    }

    @Override
    protected void enrichToString(MoreObjects.ToStringHelper toStringHelper) {
        toStringHelper.add("featureId", featureId);
    }

}
