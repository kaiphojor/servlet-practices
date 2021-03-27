package com.bitacademy.mongosite.dao.test;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnectMongoDbTest {

	public static void main(String[] args) {
		mongoQuickStart();
	}

	public static void mongoQuickStart() {
//		MongoClient mongoClient = MongoClients.create();
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("admin");
		MongoCollection<Document> collection = database.getCollection("sample");

		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
				.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
				.append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);

	}

}
