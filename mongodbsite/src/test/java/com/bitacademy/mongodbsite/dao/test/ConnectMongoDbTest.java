package com.bitacademy.mongodbsite.dao.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class ConnectMongoDbTest {

	public static void main(String[] args) {
		mongoQuickStart();
	}

	public static void mongoQuickStart() {

		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("webdb");
		MongoCollection<Document> collection = database.getCollection("user");

//		insertCounter(database);
//		insertGuestbookCounter(database);
//		insertBoardCounter(database);
//		updateCounter(database);
//		insertDoc(collection);
//		insertManyDoc(collection);
		// count document
//		findDoc(collection);
//		findEqDoc(collection,0);
//		findGtDoc(collection,70);
//		findRangeDoc(collection,30,50);
//		updateDoc(collection,0,5959);
//		updateManyDoc(collection,120,100);
//		deleteDoc(collection, 5959);
//		findEqDoc(collection,5959);
//		deleteManyDoc(collection, 200);
//		createIndex(collection, true);
		System.out.println(collection.countDocuments());
		findAllDoc(collection);
	}
	private static Long updateCounter() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("webdb");
		MongoCollection<Document> collection = database.getCollection("counter");
		Document docRevised = collection.findOneAndUpdate(Filters.eq("_id","userid"),Updates.inc("seq",1));
//		System.out.println((Long)docRevised.get("seq"));
		return (Long)docRevised.get("seq");
	}
	
	// 각 Collection들의 auto increment index 역할 해주는 collection의 생성, 초기화

	private static void insertCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("counter");
		collection.insertOne(new Document("_id","userid").append("seq",0));		
	}
	private static void insertGuestbookCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("gbcounter");
		collection.insertOne(new Document("_id","userid").append("seq",0));		
	}
	private static void insertBoardCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("bcounter");
		collection.insertOne(new Document("_id","userid").append("seq",0));		
	}

	// document 추가
	public static boolean insertDoc(MongoCollection<Document> collection) {
//		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
//				.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//				.append("info", new Document("x", 203).append("y", 102));
		// user
//		Long no = updateCounter();
		boolean result = false;
		Document doc = new Document()
//				.append("no",no)				
				.append("name","rick")				
				.append("email","rick@gmail.com")
				.append("password","asdf")
				.append("gender","male")
				.append("join_date", new Date());
		try {
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	// 많은 document 추가
	public static void insertManyDoc(MongoCollection<Document> collection) {
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
		    documents.add(new Document("i", i));
		}
		collection.insertMany(documents);
	}
	// 첫번째 document print
	public static void findDoc(MongoCollection<Document> collection) {
//		Document myDoc = collection.find().first();
//		System.out.println(myDoc.toJson());
		Document myDoc = collection.find(Filters.eq("name","rck")).first();
		System.out.println(myDoc.get("_id"));
		System.out.println(myDoc.get("name"));
		System.out.println(myDoc.get("email"));
		System.out.println(myDoc.get("password"));
		System.out.println(myDoc.get("gender"));
		System.out.println(myDoc.get("join_date"));
		System.out.println(myDoc.get("join_date").getClass().getName());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(myDoc.get("join_date")));
	}
	// 값이 일치하는  document print
	public static void findEqDoc(MongoCollection<Document> collection,int val) {
		Document myDoc = collection.find(Filters.eq("i", val)).first();
		if(null != myDoc) {
			System.out.println(myDoc.toJson());			
		}else {
			System.out.println("document not found");
		}
	}
	// 값보다큰 document print
	public static void findGtDoc(MongoCollection<Document> collection,int val) {
		collection.find(Filters.gt("i", val))
        .forEach(doc -> System.out.println(doc.toJson()));
	}
	// 지정한 범위 값의 document print(초과, 이하)
	public static void findRangeDoc(MongoCollection<Document> collection,int start,int end) {
		collection.find(Filters.and(Filters.gt("i", start), Filters.lte("i", end)))
        .forEach(doc -> System.out.println(doc.toJson()));
	}
	// print all document 
	public static void findAllDoc(MongoCollection<Document> collection) {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
	}
	// update document 
	public static void updateDoc(MongoCollection<Document> collection,int target,int value) {
//		collection.updateOne(Filters.eq("i", target),Updates.set("i", value));
		collection.findOneAndUpdate(Filters.eq("name", "zzz"),Updates.combine(
				Updates.set("name", "jenkins"),
				Updates.set("email", "jenkins@gmail.com"),
				Updates.set("password", "asdf"),
				Updates.set("gender", "female")
		    ));
		
	}
	// update multiple document 
	public static void updateManyDoc(MongoCollection<Document> collection,int condition,int value) {
		UpdateResult updateResult = collection.updateMany(Filters.lt("i", condition), Updates.inc("i", value));
		System.out.println(updateResult.getModifiedCount());
	}
	// delete document 
	public static void deleteDoc(MongoCollection<Document> collection,int value) {
		collection.deleteOne(Filters.eq("i", value));
	}
	// delete multiple document 
	public static void deleteManyDoc(MongoCollection<Document> collection,int condition) {
		DeleteResult deleteResult = collection.deleteMany(Filters.gte("i", condition));
		System.out.println(deleteResult.getDeletedCount());
	}
	// create index()
	public static void createIndex(MongoCollection<Document> collection,boolean isAscend) {
		int indexOrder = isAscend ? 1 : -1;
		collection.createIndex(new Document("i", indexOrder));
	}
	
	
	
}
