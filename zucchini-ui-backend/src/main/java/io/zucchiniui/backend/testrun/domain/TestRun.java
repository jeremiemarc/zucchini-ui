package io.zucchiniui.backend.testrun.domain;

import io.zucchiniui.backend.support.ddd.events.DeletableEventSourcedEntity;
import io.zucchiniui.backend.support.ddd.morphia.AbstractEventSourcedMorphiaEntity;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity("testRuns")
public class TestRun extends AbstractEventSourcedMorphiaEntity<String> implements DeletableEventSourcedEntity {

    @Id
    private String id;

    private String type;

    private ZonedDateTime date;

    private List<Label> labels = new ArrayList<>();

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

    public void setType(final String type) {
        this.type = Objects.requireNonNull(type);
    }

    public void setLabels(final List<Label> labels) {
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
    public void afterEntityDelete() {
        getEventStore().addEvent(new TestRunDeletedEvent(id));
    }

    @Override
    protected String getEntityId() {
        return id;
    }

}
