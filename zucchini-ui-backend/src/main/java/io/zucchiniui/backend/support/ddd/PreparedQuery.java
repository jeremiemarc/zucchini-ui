package io.zucchiniui.backend.support.ddd;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Prepared repository query.
 *
 * @param <T> Entity type
 */
public interface PreparedQuery<T> {

    /**
     * Execute query and return entities as a list.
     *
     * @return List of found entities
     */
    List<T> find();

    /**
     * Execute query and return entities as a stream.
     *
     * @return Stream of found entities
     */
    Stream<T> stream();

    /**
     * Get the first entity found by query.
     *
     * @return Found entity
     * @throws EntityNotFoundException No entity has been found with this query
     */
    T findOne();

    /**
     * Try to find an entity.
     *
     * @return First entity found by query, or empty
     */
    default Optional<T> tryToFindOne() {
        try {
            return Optional.of(findOne());
        } catch (final EntityNotFoundException e) {
            // Error is ignored
            return Optional.empty();
        }
    }

    default void forEach(Consumer<T> action) {
        stream().forEach(action);
    }

}
