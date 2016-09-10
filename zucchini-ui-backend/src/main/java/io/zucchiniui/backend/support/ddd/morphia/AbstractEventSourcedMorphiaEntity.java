package io.zucchiniui.backend.support.ddd.morphia;

import io.zucchiniui.backend.support.ddd.BaseEntity;
import io.zucchiniui.backend.support.ddd.events.DomainEvent;
import io.zucchiniui.backend.support.ddd.events.DomainEventStore;
import io.zucchiniui.backend.support.ddd.events.EventSourcedEntity;
import org.mongodb.morphia.annotations.Transient;

import java.util.List;

public abstract class AbstractEventSourcedMorphiaEntity<I> extends BaseEntity<I> implements EventSourcedEntity {

    @Transient
    private final DomainEventStore eventStore = new DomainEventStore();

    @Override
    public final List<DomainEvent> flushDomainEvents() {
        return eventStore.flush();
    }

    protected DomainEventStore getEventStore() {
        return eventStore;
    }

}
