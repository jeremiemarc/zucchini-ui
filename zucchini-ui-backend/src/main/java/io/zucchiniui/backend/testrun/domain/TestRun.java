package io.zucchiniui.backend.testrun.domain;

import io.zucchiniui.backend.shared.domain.Lockable;
import io.zucchiniui.backend.shared.domain.LockedEntityException;
import io.zucchiniui.backend.support.ddd.BaseEntity;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("testRuns")
public class TestRun extends BaseEntity<String> implements Lockable {

    @Id
    private String id;

    private String type;

    private ZonedDateTime date;

    private List<Label> labels = new ArrayList<>();

    private boolean locked;

    /**
     * Private constructor for Morphia.
     */
    private TestRun() {
    }

    public TestRun(final String type) {
        id = UUID.randomUUID().toString();
        date = ZonedDateTime.now();
        this.type = Objects.requireNonNull(type);
    }

    public void ensureUnlocked() {
        if (locked) {
            throw new LockedEntityException(getClass(), id);
        }
    }

    public void setType(final String type) {
        ensureUnlocked();
        this.type = Objects.requireNonNull(type);
    }

    public void setLabels(final List<Label> labels) {
        ensureUnlocked();
        this.labels = new ArrayList<>(labels);
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public List<Label> getLabels() {
        return Collections.unmodifiableList(labels);
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    protected String getEntityId() {
        return id;
    }

}
