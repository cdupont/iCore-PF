package gsn.utils.services;


import java.text.SimpleDateFormat;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.text.DateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


public class TwitterServiceBackup {
	private static final transient Logger logger = Logger.getLogger(TwitterService.class);
	
	private static int MAXDIM=140;
    
    //private static DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    private static DateFormat df = new SimpleDateFormat("[dd-MMM-yy HH:mm:ss]");
    private ConfigurationBuilder cb;
    private Twitter tweeterInstance;
    
    
    
    public TwitterServiceBackup(){
    	cb= new ConfigurationBuilder();
    	
    }
    
    
    public void setKeys(String ConsumerKey, String ConsumerSecret, String AccessToken, String TokenSecret){
    	if (cb!=null){
    		cb.setOAuthConsumerKey(ConsumerKey);
    		cb.setOAuthConsumerSecret(ConsumerSecret);
    	   
    		cb.setOAuthAccessToken(AccessToken);
    		cb.setOAuthAccessTokenSecret(TokenSecret);
    	}
    }
    
    public void InstanciateTwitter(){
    	if (cb!=null){
    		tweeterInstance = new TwitterFactory(cb.build()).getInstance();
    	}
    }
    
    public boolean UpdateTwitterStatus(String message, boolean prependDate) throws TwitterException{
    	StringBuilder sb=new StringBuilder();
    	sb.append(message);
    	message=sb.toString();
    	Object status= null;
    	boolean success = false;
    	if (tweeterInstance!=null){
    		if (prependDate){
    			Date now = new Date();
    			String timeStamp = df.format(now);
    			message=timeStamp  + message;
    		}
    		if (message.length()>MAXDIM){
    			logger.warn("original message too long!: "+ message);
    			message=message.substring(0, MAXDIM-1);
    			logger.warn("i will try to post only this part: "+ message);
    		}
    		status = tweeterInstance.updateStatus(message);
    		success = true;    
    	}
    	if (success){
    		logger.warn("the message should have been posted correctly "+ status);
    	}
    	else{
    		logger.warn("something went bad, check the status...: "+ status);
    	}
    	return success;
    }
    
    //everything together!
    public static boolean updateTwitterStatus(String ConsumerKey, String ConsumerSecret, String AccessToken, String TokenSecret, String message, boolean appendDate) {
    	boolean success=true;
    	Object status= null;
    	TwitterServiceBackup TS=new TwitterServiceBackup();
    	TS.setKeys(
    			ConsumerKey,
    			ConsumerSecret,
    			AccessToken,
    			TokenSecret
    			);
    	TS.InstanciateTwitter();
    	
    	
    	try {
    		status = TS.UpdateTwitterStatus(message, appendDate);
		} catch (TwitterException e) {
			logger.warn(e.getMessage());
			logger.warn("update status returned Status "+ status);
			return false;
		}
		
		logger.warn("update status returned Status "+ status);
    	return success;
    }
    
    
    /*//everything together!
    public static boolean sendDirectMessage(String ConsumerKey, String ConsumerSecret, String AccessToken, String TokenSecret, String message, String friend) {
    	boolean success=true;
    	Object status= null;
    	TwitterServiceNew TS=new TwitterServiceNew();
    	TS.setKeys(
    			ConsumerKey,
    			ConsumerSecret,
    			AccessToken,
    			TokenSecret
    			);
    	
  
    	try {
    		DirectMessage messageSent = tweeterInstance.sendDirectMessage(friend, message);
            System.out.println("Direct message successfully sent to " + messageSent.getRecipientScreenName());
    		
		} catch (TwitterException e) {
			logger.warn(e.getMessage());
			logger.warn("update status returned Status "+ status);
			return false;
		}
    	logger.debug("update status returned Status "+ status);
    	return success;
    }*/
    
    
    
    

    
    
    public static void main(String[] args) throws InterruptedException {
    	
    	//catharsis79
    	TwitterServiceBackup TA=new TwitterServiceBackup();
    	TA.setKeys(
    			"w0d7Gsm2J0n0AYa6sMs7w",
    			"C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c",
    			"1283477593-Shq85qZynT2FpEeq9ojtjhPa6mZfdqPGPg3U0Qm",
    			"RVIVi5CcErHyi27TjTfXcbzH7vnOkyWGVNvbrYbWSrE"
    			);
    	TA.InstanciateTwitter();
    	
    	for (int i=0;i<2;i++){
    		try {
				TA.UpdateTwitterStatus("message "+String.valueOf(i+1), true);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Thread.sleep(5000);
    	}
    	
    	//GSNUser
    	TwitterServiceBackup TB=new TwitterServiceBackup();
    	TB.setKeys(
    			"w0d7Gsm2J0n0AYa6sMs7w",
    			"C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c",
    			"1399654459-a1PLizcskquV6Mwaa6fya97tYDtJKUEaK5mmKUr",
    			"TF0DBclvDYAExcRYPY8MvKf4zr4TWet3xJhwyCrI"
    			);
    	TB.InstanciateTwitter();
    	
    	for (int i=0;i<2;i++){
    		try {
				TB.UpdateTwitterStatus("message "+String.valueOf(i*100+1), true);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Thread.sleep(5000);
    	}
    	
    	boolean a=updateTwitterStatus(
    			"w0d7Gsm2J0n0AYa6sMs7w",
    			"C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c",
    			"1283477593-Shq85qZynT2FpEeq9ojtjhPa6mZfdqPGPg3U0Qm",
    			"RVIVi5CcErHyi27TjTfXcbzH7vnOkyWGVNvbrYbWSrE",
    			"strange message...",true);
    	
    	a=updateTwitterStatus(
    			"w0d7Gsm2J0n0AYa6sMs7w",
    			"C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c",
    			"1399654459-a1PLizcskquV6Mwaa6fya97tYDtJKUEaK5mmKUr",
    			"TF0DBclvDYAExcRYPY8MvKf4zr4TWet3xJhwyCrI",
    			"strange message...",true);
    	
    	
    	
    	/*a=sendDirectMessage("w0d7Gsm2J0n0AYa6sMs7w",
    			"C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c",
    			"1283477593-Shq85qZynT2FpEeq9ojtjhPa6mZfdqPGPg3U0Qm",
    			"RVIVi5CcErHyi27TjTfXcbzH7vnOkyWGVNvbrYbWSrE",
    			"how are you GSNuser???", "GSNaccount");*/
    	
    }


}
