package io.zucchiniui.backend.auth.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByNameAndPassword(String name, String password);

}
