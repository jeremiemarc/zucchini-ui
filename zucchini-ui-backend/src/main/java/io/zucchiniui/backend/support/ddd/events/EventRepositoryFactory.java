package io.zucchiniui.backend.support.ddd.events;

import com.google.common.base.Joiner;
import com.google.common.primitives.Primitives;
import io.zucchiniui.backend.support.ddd.Repository;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventRepositoryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRepositoryFactory.class);

    private final DomainEventDispatcher domainEventDispatcher;

    public EventRepositoryFactory(DomainEventDispatcher domainEventDispatcher) {
        this.domainEventDispatcher = domainEventDispatcher;
    }

    public <T extends Repository<? extends EventSourcedEntity, ?>> T createRepository(Class<? extends T> repositoryImplClass, Object... repositoryArgs) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(repositoryImplClass.getInterfaces());
        enhancer.setSuperclass(repositoryImplClass);

        enhancer.setCallback(new MethodInterceptor() {

            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

                final MethodCall call = () -> proxy.invokeSuper(obj, args);

                // TODO Detect that save() and delete() methods are from Repository base interface
                ///// if (method.getDeclaringClass() == Repository.class) {
                switch (method.getName()) {
                    case "save": {
                        final EventSourcedEntity entity = (EventSourcedEntity) args[0];
                        return doSave(entity, call);
                    }

                    case "delete": {
                        final EventSourcedEntity entity = (EventSourcedEntity) args[0];
                        return doDelete(entity, call);
                    }

                    default:
                        // Call will be done later
                        break;
                }
                ///// }

                return call.call();

            }

        });

        Constructor<?> constructor = findConstructorForArgs(repositoryImplClass, repositoryArgs);
        return repositoryImplClass.cast(enhancer.create(constructor.getParameterTypes(), repositoryArgs));
    }

    private Object doSave(EventSourcedEntity entity, MethodCall saveCall) throws Throwable {
        LOGGER.debug("Saving a event sourced entity: {}", entity);

        final Object result = saveCall.call();
        flushEntityEventsToBus(entity);
        return result;
    }

    private Object doDelete(EventSourcedEntity entity, MethodCall deleteCall) throws Throwable {
        LOGGER.debug("Saving a event sourced entity: {}", entity);

        final Object result = deleteCall.call();

        if (entity instanceof DeletableEventSourcedEntity) {
            ((DeletableEventSourcedEntity) entity).afterEntityDelete();
        }

        flushEntityEventsToBus(entity);
        return result;
    }

    private void flushEntityEventsToBus(EventSourcedEntity entity) {
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

    @FunctionalInterface
    private interface MethodCall {

        Object call() throws Throwable;

    }

}
