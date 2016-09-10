package io.zucchiniui.backend.support.ddd.events;

import com.google.common.base.MoreObjects;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class AbstractDomainEvent<T, I> implements DomainEvent {

    private final String eventId;

    private final ZonedDateTime eventDate;

    private final Class<T> entityType;

    private final I entityId;

    public AbstractDomainEvent(Class<T> entityType, I entityId) {
        eventId = UUID.randomUUID().toString();
        eventDate = ZonedDateTime.now();
        this.entityType = entityType;
        this.entityId = entityId;
    }

    @Override
    public final String toString() {
        final MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this)
            .add("eventId", eventId)
            .add("eventDate", eventDate)
            .add("entityType", entityType)
            .add("entityId", entityId);

        enrichToString(toStringHelper);
        return toStringHelper.toString();
    }

    @Override
    public final String getEventId() {
        return eventId;
    }

    @Override
    public final ZonedDateTime getEventDate() {
        return eventDate;
    }

    @Override
    public final Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public final I getEntityId() {
        return entityId;
    }

    protected void enrichToString(MoreObjects.ToStringHelper toStringHelper) {
        // Rien à enrichir pour le moment
    }

}
