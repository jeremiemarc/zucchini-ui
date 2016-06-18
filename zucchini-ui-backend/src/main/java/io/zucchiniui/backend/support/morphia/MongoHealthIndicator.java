package io.zucchiniui.backend.support.morphia;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.Document;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

public class MongoHealthIndicator extends AbstractHealthIndicator {

    private final MongoClient mongoClient;

    private final MongoClientURI mongoClientURI;

    public MongoHealthIndicator(MongoClient mongoClient, MongoClientURI mongoClientURI) {
        this.mongoClient = mongoClient;
        this.mongoClientURI = mongoClientURI;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        final BasicDBObject command = new BasicDBObject("buildInfo", 1);

        final Document document = mongoClient.getDatabase(mongoClientURI.getDatabase()).runCommand(command);
        final String version = document.getString("version");

        builder.up()
            .withDetail("version", version)
            .withDetail("uri", Joiner.on(',').join(mongoClientURI.getHosts()) + "/" + mongoClientURI.getDatabase())
            .withDetail("master", mongoClient.getAddress())
            .withDetail("allNodes", mongoClient.getAllAddress());
    }

}
