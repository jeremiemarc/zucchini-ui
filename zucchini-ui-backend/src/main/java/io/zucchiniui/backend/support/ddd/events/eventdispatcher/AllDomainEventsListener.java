package io.zucchiniui.backend.support.ddd.events.eventdispatcher;

import com.google.common.eventbus.Subscribe;
import io.zucchiniui.backend.support.ddd.events.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllDomainEventsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllDomainEventsListener.class);

    @Subscribe
    public void onDomainEvent(DomainEvent event) {
        LOGGER.info("Received domain event: {}", event);
    }

}
