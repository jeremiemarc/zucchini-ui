package io.zucchiniui.backend.support.ddd.events;

import com.google.common.eventbus.EventBus;

import java.util.List;

public class GuavaDomainEventBus implements DomainEventBus {

    private final EventBus eventBus;

    public GuavaDomainEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void submit(List<DomainEvent> events) {
        events.forEach(eventBus::post);
    }

}
