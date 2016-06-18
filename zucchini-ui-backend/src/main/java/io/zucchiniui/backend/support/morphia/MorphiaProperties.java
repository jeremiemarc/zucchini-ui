package io.zucchiniui.backend.support.morphia;

import com.mongodb.MongoClientURI;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "mongo")
public class MorphiaProperties {

    @NotNull
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MongoClientURI getClientURI() {
        return new MongoClientURI(uri);
    }

}
