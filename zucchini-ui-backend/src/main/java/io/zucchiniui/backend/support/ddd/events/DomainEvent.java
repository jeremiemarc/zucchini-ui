package io.zucchiniui.backend.support.ddd.events;

import java.time.ZonedDateTime;

public interface DomainEvent {

    String getEventId();

    ZonedDateTime getEventDate();

    Class<?> getEntityType();

    Object getEntityId();

}
