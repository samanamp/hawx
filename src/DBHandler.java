import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class DBHandler {
	CouchDbClient dbClient;
	public DBHandler(){

		dbClient = new CouchDbClient("twitterdata", true, "http", "127.0.0.1", 5984, null, null);
	}
	
	public void addNewTweet(String tweet, long id){
		
		JsonObject jsonobj = dbClient.getGson().fromJson(tweet, JsonObject.class);
		jsonobj.addProperty("_id", Long.toString(id));
		dbClient.save(jsonobj);
	}

}
