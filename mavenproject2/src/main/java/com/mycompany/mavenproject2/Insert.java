/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Insert {
    public static void main(String[] args) {
        String uri = "mongodb+srv://Ibrahim:ibrahimU123@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Registered");
            MongoCollection<Document> collection = database.getCollection("users2");

            // Create a new document to be inserted
            Document newUser = new Document();
            newUser.append("name", "Ceren Çolak")
                    .append("email", "ceren35colak@gmail.com")
                    .append("school", "Ege Universiry")
                    .append("ceze", "100");

            // Insert the document into the collection
            collection.insertOne(newUser);

            System.out.println("New user inserted successfully.");
        }
    }
}