package io.zucchiniui.backend.feature.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public final class FeatureTagsUpdatedEvent extends FeatureEvent {

    private final Set<String> tags;

    public FeatureTagsUpdatedEvent(String entityId, Set<String> tags) {
        super(entityId);
        this.tags = Sets.newHashSet(tags);
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    protected void enrichToString(MoreObjects.ToStringHelper toStringHelper) {
        toStringHelper.add("tags", tags);
    }

}
