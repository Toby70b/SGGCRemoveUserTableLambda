package sggc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class HandlerStream implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        MongoClientSettings settings = getMongoClientSettings();
        System.out.println("Attempting connection to Mongo Db Cluster");
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            System.out.println("Connection Established");
            MongoDatabase database = mongoClient.getDatabase("SteamGroupGameChecker");
            MongoCollection<Document> user = database.getCollection("User");
            user.drop();
            System.out.println("User table successfully dropped");
        } catch (Exception e) {
            System.out.println("Error while dropping user table");
            e.printStackTrace();
        }
    }

    private static MongoClientSettings getMongoClientSettings() {
        ConnectionString connString = new ConnectionString(
                "mongodb+srv://Toby:YOAmX5QyaBYLhgIf@sggc-cluster-g1f6o.mongodb.net/test?w=majority"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        return settings;
    }
}