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

	static String ConsumerKey = "t7zZY1v1NUIjbaX27dGw";
	static String ConsumerSecret = "6bbcC84YlVBMzGJh9fOyVctrYnbOuLzOG4BkOVEyZ4";
	static String oauth_token = "1364169276-648ioFHIqQuPyNcA5tFqstRXCN0sU5DICpTL5dD";
	static String oauth_token_secret = "Fgwjwa38annsBueTmxSPXvUwNknvvw4ztYwCyyYj9k";
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StatusListener listener = new StatusListener() {
			DBHandler dbHandler = new DBHandler();
			long count = 0;
			@Override
            public void onStatus(Status status) {
            	String rawTweet = DataObjectFactory.getRawJSON(status);
            	dbHandler.addNewTweet(rawTweet, status.getId());
            	GeoLocation gl = status.getGeoLocation();
            	System.out.println("!!"+(++count)+"------"+gl.getLatitude()+","+gl.getLongitude());
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
		
		double lat = -34;
	    double longitude = 150.9;
	    double lat1 = lat - 0.5;
	    double longitude1 = longitude - 0.5;
	    double lat2 = lat + 0.5;
	    double longitude2 = longitude + 0.5;
	    double[][] locations= {{longitude1, lat1}, {longitude2, lat2}};
	    
		fq.locations(locations);
		twitterStream.filter(fq);
	}

}
