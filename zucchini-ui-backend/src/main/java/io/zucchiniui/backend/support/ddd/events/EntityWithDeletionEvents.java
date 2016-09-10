package io.zucchiniui.backend.support.ddd.events;

public interface EntityWithDeletionEvents extends EntityWithEvents {

    void onDelete();

}
