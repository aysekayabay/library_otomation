package com.mycompany.mavenproject2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Insert {
    public static void main(String[] args) {
        String uri = "mongodb+srv://***:***@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Library");
            MongoCollection<Document> collection = database.getCollection("rooms");

            // Create a new document to be inserted
            List<Document> deskList = new ArrayList<>();
            int id = 10000;
            int ownerId = 3000;
            int deskId = 5000;
            for (int i = 0; i < 20; i++) {
                Document deskObj = new Document();
                deskObj.append("isAvailable", true)
                        .append("id", id)
                        .append("ownerId", ownerId)
                        .append("deskId", deskId);
                deskList.add(deskObj);
            }

            Document newUser = new Document();
            newUser.append("current_num", 0)
                    .append("desk_num", 20)
                    .append("name", "Yazı tura")
                    .append("desk", deskList);

            // Insert the document into the collection
            collection.insertOne(newUser);

            System.out.println("New user inserted successfully.");
        }
    }
}
