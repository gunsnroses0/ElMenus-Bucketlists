package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import Commands.Command;

public class Bucketlist {

	private static final String COLLECTION_NAME = "bucketlists";

	private static MongoCollection<Document> collection = null;
	private static int DbPoolCount = 4;
	static String host = System.getenv("MONGO_URI");

	public static int getDbPoolCount() {
		return DbPoolCount;
	}
	public static void setDbPoolCount(int dbPoolCount) {
		DbPoolCount = dbPoolCount;
	}

	public static HashMap<String, Object> create(HashMap<String, Object> atrributes) throws ParseException {

		MongoClientURI uri = new MongoClientURI(
				"mongodb://admin:admin@cluster0-shard-00-00-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-01-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-02-nvkqp.gcp.mongodb.net:27017/El-Menus?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("bucketlists");
		Document newBucketlist = new Document();

		for (String key : atrributes.keySet()) {
			newBucketlist.append(key, atrributes.get(key));
		}
		collection.insertOne(newBucketlist);

		JSONParser parser = new JSONParser();
		HashMap<String, Object> returnValue = Command.jsonToMap((JSONObject) parser.parse(newBucketlist.toJson()));
		return returnValue;
	}

	public static HashMap<String, Object> get(String messageId) {

		MongoClientURI uri = new MongoClientURI(
				host);

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);
		
		System.out.println(messageId.getClass().getName());
		System.out.println(messageId);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("bucketlists");
		System.out.println("Inside Get");
		BasicDBObject query = new BasicDBObject();
		query.put("user_id", messageId);

		System.out.println(query.toString());
		HashMap<String, Object> message = null;
		Document doc = collection.find(query).first();
		JSONParser parser = new JSONParser(); 
		try {
			JSONObject json = (JSONObject) parser.parse(doc.toJson());
		
			message = Command.jsonToMap(json);
			
			System.out.println(message.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return message;
	}
}
