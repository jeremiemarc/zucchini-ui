package io.zucchiniui.backend.shared.domain;

public class LockedEntityException extends IllegalStateException {

    public LockedEntityException(Class<?> entityClass, String testRunId) {
        super("Entity " + entityClass + " with ID " + testRunId + " is locked");
    }

}
