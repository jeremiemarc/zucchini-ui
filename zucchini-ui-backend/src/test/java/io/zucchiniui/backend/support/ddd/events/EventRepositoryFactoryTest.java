package io.zucchiniui.backend.support.ddd.events;

import io.zucchiniui.backend.support.ddd.BaseEntity;
import io.zucchiniui.backend.support.ddd.Repository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class EventRepositoryFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRepositoryFactoryTest.class);

    @Mock
    private DomainEventDispatcher domainEventDispatcher;

    @InjectMocks
    private EventRepositoryFactory eventRepositoryFactory;

    @Captor
    private ArgumentCaptor<List<DomainEvent>> domainEventsCaptor;

    private SampleRepository enhancedRepository;

    @Before
    public void setUp() throws Exception {
        enhancedRepository = eventRepositoryFactory.createRepository(SampleRepositoryImpl.class, 2, 5L, "toto");
    }

    @Test
    public void should_submit_events_to_event_bus_on_save_entity() throws Exception {
        // given
        final SampleDeletableEventSourcedEntity entity = new SampleDeletableEventSourcedEntity();

        // when
        entity.doSomething();
        enhancedRepository.save(entity);

        // then
        then(domainEventDispatcher).should().dispatch(domainEventsCaptor.capture());

        assertThat(domainEventsCaptor.getValue())
            .isNotEmpty()
            .allMatch(event -> event instanceof SomethingDoneEvent);
    }

    @Test
    public void should_submit_events_to_event_bus_on_delete_entity() throws Exception {
        // given
        final SampleDeletableEventSourcedEntity entity = new SampleDeletableEventSourcedEntity();

        // when
        enhancedRepository.delete(entity);

        // then
        then(domainEventDispatcher).should().dispatch(domainEventsCaptor.capture());

        assertThat(domainEventsCaptor.getValue())
            .isNotEmpty()
            .allMatch(event -> event instanceof SomethingDoneEvent);
    }

    @Test
    public void should_submit_events_to_event_bus_on_calling_repository_implem_method() throws Exception {
        // given
        final SampleDeletableEventSourcedEntity entity1 = new SampleDeletableEventSourcedEntity();
        final SampleDeletableEventSourcedEntity entity2 = new SampleDeletableEventSourcedEntity();

        // when
        entity1.doSomething();
        entity2.doSomething();
        enhancedRepository.saveTwoEntities(entity1, entity2);

        // then
        then(domainEventDispatcher).should(times(2)).dispatch(domainEventsCaptor.capture());

        assertThat(domainEventsCaptor.getAllValues())
            .flatExtracting(domainEvents -> domainEvents)
            .hasSize(2)
            .allMatch(event -> event instanceof SomethingDoneEvent);
    }


    public static class SampleDeletableEventSourcedEntity extends BaseEntity<String> implements EventSourcedEntity, DeletableEventSourcedEntity {

        private final DomainEventStore domainEventStore = new DomainEventStore();

        private final String id;

        public SampleDeletableEventSourcedEntity() {
            id = UUID.randomUUID().toString();
        }

        public void doSomething() {
            domainEventStore.addEvent(new SomethingDoneEvent(id, "Action 1"));
        }

        @Override
        public List<DomainEvent> flushDomainEvents() {
            return domainEventStore.flush();
        }

        @Override
        public void afterEntityDelete() {
            domainEventStore.addEvent(new SomethingDoneEvent(id, "Deleted"));
        }

        @Override
        protected String getEntityId() {
            return id;
        }

    }

    public interface SampleRepository extends Repository<SampleDeletableEventSourcedEntity, String> {

        void saveTwoEntities(SampleDeletableEventSourcedEntity sample1, SampleDeletableEventSourcedEntity sample2);

    }

    public static class SampleRepositoryImpl implements SampleRepository {

        public SampleRepositoryImpl(int arg1, long arg2, Object arg3) {

        }

        public SampleRepositoryImpl(int arg1, long arg2) {

        }

        public SampleRepositoryImpl(String arg1, String arg2, String arg3) {

        }

        @Override
        public SampleDeletableEventSourcedEntity getById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void save(SampleDeletableEventSourcedEntity entity) {
            LOGGER.info("IMPL - Saving one entity {}", entity);
        }

        @Override
        public void delete(SampleDeletableEventSourcedEntity entity) {

        }

        @Override
        public void saveTwoEntities(SampleDeletableEventSourcedEntity sample1, SampleDeletableEventSourcedEntity sample2) {
            LOGGER.debug("IMPL - Saving entities {} and {}", sample1, sample2);
            Stream.of(sample1, sample2).forEach(sample -> {
                save(sample);
            });
        }

    }

    public static abstract class SampleDomainEvent extends AbstractDomainEvent<SampleDeletableEventSourcedEntity, String> {

        public SampleDomainEvent(String entityId) {
            super(SampleDeletableEventSourcedEntity.class, entityId);
        }

    }

    public static final class SomethingDoneEvent extends SampleDomainEvent {

        private final String details;

        public SomethingDoneEvent(String entityId, String details) {
            super(entityId);
            this.details = details;
        }

        public String getDetails() {
            return details;
        }

    }

}
