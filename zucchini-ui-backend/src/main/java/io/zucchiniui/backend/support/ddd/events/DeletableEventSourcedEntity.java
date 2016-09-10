package io.zucchiniui.backend.support.ddd.events;

public interface DeletableEventSourcedEntity extends EventSourcedEntity {

    void afterEntityDelete();

}
