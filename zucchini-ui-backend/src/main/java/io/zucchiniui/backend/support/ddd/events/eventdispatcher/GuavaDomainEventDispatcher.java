package io.zucchiniui.backend.support.ddd.events.eventdispatcher;

import com.google.common.eventbus.EventBus;
import io.zucchiniui.backend.support.ddd.events.DomainEvent;
import io.zucchiniui.backend.support.ddd.events.DomainEventDispatcher;

import java.util.Collection;

public class GuavaDomainEventDispatcher implements DomainEventDispatcher {

    private final EventBus eventBus;

    public GuavaDomainEventDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void dispatch(Collection<DomainEvent> events) {
        events.forEach(eventBus::post);
    }

}
