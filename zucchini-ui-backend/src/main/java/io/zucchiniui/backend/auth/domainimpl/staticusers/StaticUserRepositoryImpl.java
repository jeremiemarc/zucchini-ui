package io.zucchiniui.backend.auth.domainimpl.staticusers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.zucchiniui.backend.auth.domain.User;
import io.zucchiniui.backend.auth.domain.UserRepository;

import java.util.Objects;
import java.util.Optional;

class StaticUserRepositoryImpl implements UserRepository {

    private final ImmutableMap<String, UserDefinition> users;

    public StaticUserRepositoryImpl(final ImmutableList<UserDefinition> staticUserRepositoryFactory) {
        final ImmutableMap.Builder<String, UserDefinition> usersBuilder = ImmutableMap.builder();
        staticUserRepositoryFactory.forEach(userDefinition -> usersBuilder.put(userDefinition.getName(), userDefinition));
        users = usersBuilder.build();
    }

    @Override
    public Optional<User> findByNameAndPassword(final String name, final String password) {
        return getUserDefinition(name)
            .flatMap(userDefinition -> {
                if (Objects.equals(password, userDefinition.getPassword())) {
                    return Optional.of(userDefinition);
                }
                return Optional.empty();
            })
            .map(StaticUserRepositoryImpl::buildFromDefinition);
    }

    private Optional<UserDefinition> getUserDefinition(final String name) {
        return Optional.ofNullable(users.get(name));
    }

    private static User buildFromDefinition(final UserDefinition userDefinition) {
        return new User(userDefinition.getName(), userDefinition.getDisplayName());
    }

}
