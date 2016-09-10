package io.zucchiniui.backend.support.ddd.events;

import java.util.List;

public interface DomainEventBus {

    void submit(List<DomainEvent> events);

}
