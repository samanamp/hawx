import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class GeoTracker {

	static private String ConsumerKey = "";
	static private String ConsumerSecret = "";
	static private String oauth_token = "";
	static private String oauth_token_secret = "";
	private String ip;
	private String dbName;
	private double [][] locations;
	
	public GeoTracker(String ip, String dbName, double [][] locations){
		this.ip = ip;
		this.dbName = dbName;
		this.locations = locations;		
	}
	
	public void startTracking(){
		StatusListener listener = new StatusListener() {
			DBHandler dbHandler = new DBHandler(ip, dbName);
			WebMaker wm = new WebMaker(locations);
			int count = 0;
			@Override
            public void onStatus(Status status) {
            	String rawTweet = DataObjectFactory.getRawJSON(status);
            	dbHandler.addNewTweet(rawTweet, status.getId());
            	GeoLocation gl = status.getGeoLocation();
            	System.out.println("!!"+(++count)+"------"+gl.getLatitude()+","+gl.getLongitude());
            	wm.update(count);
                System.out.println(status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(ConsumerKey)
				// sets the Consumer Key String
				.setOAuthConsumerSecret(ConsumerSecret)
				// sets the Consumer Secret String
				.setOAuthAccessToken(oauth_token)
				.setOAuthAccessTokenSecret(oauth_token_secret);
		cb.setJSONStoreEnabled(true);
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);
		FilterQuery fq = new FilterQuery();
	    
		fq.locations(locations);
		twitterStream.filter(fq);
	}

	public static void main(String[] args) {

	    
	    if(args.length < 4){
	    	System.out.println("Use the program as follow:\n java -jar GeoTracker.jar ip databaseName lat1 long1 lat2 long2");
	    	System.exit(0);
	    }
	    String ip = args[0];
	    String databaseName = args[1];
	    
	    double lat1 = Double.parseDouble(args[2]);
	    double longitude1 = Double.parseDouble(args[3]);
	    double lat2 = Double.parseDouble(args[4]);
	    double longitude2 = Double.parseDouble(args[5]);
	    double[][] locations= {{longitude1, lat1}, {longitude2, lat2}};

	    GeoTracker gt = new GeoTracker(ip, databaseName, locations);
	    gt.startTracking();
	    
	    

	}

}
