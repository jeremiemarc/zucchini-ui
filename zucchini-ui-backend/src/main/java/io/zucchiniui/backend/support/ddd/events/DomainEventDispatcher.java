package io.zucchiniui.backend.support.ddd.events;

import java.util.Collection;

public interface DomainEventDispatcher {

    void dispatch(Collection<DomainEvent> events);

}
