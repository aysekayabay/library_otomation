/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mycompany.mavenproject2.Menu.myUser;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author ayse
 */
public class push {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://Ibrahim:ibrahimU123@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority")) {
            MongoDatabase database = mongoClient.getDatabase("Library");
            MongoCollection<Document> roomsCollection = database.getCollection("rooms");
            // Collection'ı seçin
            MongoCollection<Document> collection = database.getCollection("rooms");

            // Room objesini oluşturun
            Document roomDocument = new Document("_id", new ObjectId())
                    .append("room_no", 2)
                    .append("current_num", 0)
                    .append("desk_num", 21)
                    .append("name", "Davo");

            // Desks listesini oluşturun
            List<Document> desks = new ArrayList<>();
            for (int i = 1; i <= 21; i++) {
                Document desk = new Document("deskNo", i)
                        .append("isAvailable", true)
                        .append("ownerId", "");
                desks.add(desk);
            }
            roomDocument.append("desks", desks);

            // Belgeyi collection'a ekle
            collection.insertOne(roomDocument);

            // MongoDB istemcisini kapat
            mongoClient.close();

        }
    }
}
