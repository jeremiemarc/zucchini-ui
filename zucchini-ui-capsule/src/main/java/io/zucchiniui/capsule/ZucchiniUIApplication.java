package io.zucchiniui.capsule;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zucchiniui.backend.BackendApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;

public class ZucchiniUIApplication extends BackendApplication {

    @Autowired
    private JerseyProperties jerseyProperties;

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(final String... args) {
        SpringApplication.run(ZucchiniUIApplication.class, args);
    }

}
