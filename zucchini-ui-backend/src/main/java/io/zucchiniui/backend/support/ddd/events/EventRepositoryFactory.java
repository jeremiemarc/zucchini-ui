package io.zucchiniui.backend.support.ddd.events;

import com.google.common.base.Joiner;
import com.google.common.primitives.Primitives;
import io.zucchiniui.backend.support.ddd.Repository;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class EventRepositoryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRepositoryFactory.class);

    private final DomainEventDispatcher domainEventDispatcher;

    public EventRepositoryFactory(DomainEventDispatcher domainEventDispatcher) {
        this.domainEventDispatcher = domainEventDispatcher;
    }

    public <T extends Repository<? extends EventSourcedEntity, ?>> T createRepository(Class<? extends T> repositoryImplClass, Object... repositoryArgs) {
        final Interceptor interceptor = new Interceptor();

        final Class<? extends T> clazz = new ByteBuddy()
            .subclass(repositoryImplClass)
            .method(named("save")).intercept(MethodDelegation.to(interceptor))
            .method(named("delete")).intercept(MethodDelegation.to(interceptor))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();

        LOGGER.debug("Generated wrapper class {} for repository class {}", clazz, repositoryImplClass);

        final Constructor<?> constructor = findConstructorForArgs(clazz, repositoryArgs);
        try {
            return clazz.cast(constructor.newInstance(repositoryArgs));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Can't build the wrapper for class " + repositoryImplClass, e);
        }
    }

    private void flushEntityEventsToDispatcher(EventSourcedEntity entity) {
        final List<DomainEvent> events = entity.flushDomainEvents();
        if (!events.isEmpty()) {
            LOGGER.debug("Submitting {} events", events.size());
            domainEventDispatcher.dispatch(events);
        }
    }

    private static Constructor<?> findConstructorForArgs(Class<?> clazz, Object... args) {
        final List<Constructor<?>> constructors = Arrays.stream(clazz.getConstructors())
            .filter(constructor -> constructor.getParameterCount() == args.length)
            .filter(constructor -> {
                boolean matched = true;

                for (int i = 0; i < args.length; i++) {
                    Class<?> expectedArgType = Primitives.wrap(constructor.getParameterTypes()[i]);
                    Object value = args[i];

                    if (value != null) {
                        matched &= expectedArgType.isAssignableFrom(value.getClass());
                    }
                }

                return matched;
            })
            .collect(Collectors.toList());

        switch (constructors.size()) {
            case 1:
                final Constructor<?> foundConstructor = constructors.get(0);
                LOGGER.debug("Found constructor {} for args {}", foundConstructor);
                return foundConstructor;
            case 0:
                throw new IllegalArgumentException("No constructor matching args: " + Joiner.on(", ").join(args));
            default:
                throw new IllegalArgumentException("Too many constructors matching args: " + Joiner.on(", ").join(args) + "; constructors are " + constructors);
        }
    }

    public class Interceptor {

        public void save(@SuperCall Callable<Void> saveCall, @Argument(0) EventSourcedEntity entity) throws Exception {
            LOGGER.debug("Saving a event sourced entity: {}", entity);

            saveCall.call();

            flushEntityEventsToDispatcher(entity);
        }

        public void delete(@SuperCall Callable<Void> deleteCall, @Argument(0) EventSourcedEntity entity) throws Exception {
            LOGGER.debug("Saving a event sourced entity: {}", entity);

            deleteCall.call();

            if (entity instanceof DeletableEventSourcedEntity) {
                ((DeletableEventSourcedEntity) entity).afterEntityDelete();
            }

            flushEntityEventsToDispatcher(entity);
        }

    }

}
