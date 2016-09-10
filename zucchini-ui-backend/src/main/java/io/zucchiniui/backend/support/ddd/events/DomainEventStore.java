package io.zucchiniui.backend.support.ddd.events;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DomainEventStore {

    private List<DomainEvent> events = new ArrayList<>();

    public void addEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> flush() {
        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        final List<DomainEvent> flushed = events;
        events = new ArrayList<>();
        return Collections.unmodifiableList(flushed);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("events", events)
            .toString();
    }

}
