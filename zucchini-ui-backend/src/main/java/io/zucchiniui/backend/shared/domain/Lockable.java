package io.zucchiniui.backend.shared.domain;

public interface Lockable {

    boolean isLocked();

    void setLocked(boolean locked);

}
