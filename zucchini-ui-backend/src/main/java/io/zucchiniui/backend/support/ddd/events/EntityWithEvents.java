package io.zucchiniui.backend.support.ddd.events;

import java.util.List;

public interface EntityWithEvents {

    List<DomainEvent> flush();

}
