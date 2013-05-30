package gsn.utils.services;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;


public class TwitterService {
	private static final transient Logger logger = Logger.getLogger(TwitterService.class);
	
	private static final String CONF_FILE = "conf/twitter.properties";
	
	private static int MAXDIM=140;
    
	private static String DEFAULT_DATEFORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
	
	private static String DEFAULT_CONSUMERKEY = "w0d7Gsm2J0n0AYa6sMs7w";
	private static String DEFAULT_CONSUMERSECRET = "C3idDawpECe5BB6KQX7yVWnaGTDcUT4CEeB7d6eb7c";
	private static String DEFAULT_ACCESSKEY = "1399654459-a1PLizcskquV6Mwaa6fya97tYDtJKUEaK5mmKUr";
	private static String DEFAULT_ACCESSSECRET = "TF0DBclvDYAExcRYPY8MvKf4zr4TWet3xJhwyCrI";
	
	
	private static String dateFormat = DEFAULT_DATEFORMAT;
	
    private static String consumerKey = DEFAULT_CONSUMERKEY;
    private static String consumerSecret = DEFAULT_CONSUMERSECRET;
    private static String accessKey = DEFAULT_ACCESSKEY;
    private static String accessSecret = DEFAULT_ACCESSSECRET;
    
    private static DateFormat df;
    
    
    private static ConfigurationBuilder cb;
    private static Twitter twitter;
    
    
    
	static{
	    	String readString;
	    	Properties Readprops = new Properties();
	    	
			//load a properties file	
			try {
				Readprops.load(new FileInputStream(CONF_FILE));
			} catch (FileNotFoundException e) {
				logger.warn("Unable to read " + CONF_FILE);
			} catch (IOException e) {
				logger.warn("Impossible to read " + CONF_FILE);
			}
	    		
			//parse file properties
			
			readString=Readprops.getProperty("consumerKey");
			if (readString==null){
				logger.warn("Using default consumerKey");
			}
			else{
				consumerKey = readString;
			}
			
			readString=Readprops.getProperty("consumerSecret");
			if (readString==null){
				logger.warn("Using default consumerSecret");
			}
			else{
				consumerSecret = readString;
			}
			
			readString=Readprops.getProperty("accessKey");
			if (readString==null){
				logger.warn("Using default accessKey");
			}
			else{
				accessKey = readString;
			}
			
			readString=Readprops.getProperty("accessSecret");
			if (readString==null){
				logger.warn("Using default accessSecret");
			}
			else{
				accessSecret = readString;
			}
			
			readString=Readprops.getProperty("dateFormat");
			if (readString==null){
				logger.warn("Using default dateFormat");
			}
			else{
				dateFormat = readString;
			}
			df = new SimpleDateFormat(dateFormat);
			
			
			//init
			cb= new ConfigurationBuilder();
			
			//fill configuration builder
			cb.setOAuthConsumerKey(consumerKey);
    		cb.setOAuthConsumerSecret(consumerSecret);
    	   
    		cb.setOAuthAccessToken(accessKey);
    		cb.setOAuthAccessTokenSecret(accessSecret);
    		
    		//get twitter instance
    		twitter = new TwitterFactory(cb.build()).getInstance();
	}
    
	
   
    
    public static boolean UpdateTwitterStatus(String message, boolean prependDate) throws TwitterException{
    	
    	StringBuilder sb=new StringBuilder();
    	sb.append(message);
    	message=sb.toString();
    	Object status= null;
    	boolean success = false;
    	if (twitter!=null){
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
    		status = twitter.updateStatus(message);
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
    
    
	public static boolean SendTwitterMessage(String to, String message, boolean prependDate) throws TwitterException{
	    	
	    	StringBuilder sb=new StringBuilder();
	    	sb.append(message);
	    	message=sb.toString();
	    	Object status= null;
	    	
	    	DirectMessage msg=null;
	    	boolean success = false;
	    	if (twitter!=null){
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
	    		
	            msg = twitter.sendDirectMessage(to, message);
	            System.out.println();
	    		success = true;    
	    	}
	    	if (success){
	    		logger.warn("Direct message successfully sent to " + msg.getRecipientScreenName());
	    	}
	    	else{
	    		logger.warn("something went bad, check the status...: "+ status);
	    	}
	    	return success;
	    }
    
    
    
    
    
    
    
   


}
