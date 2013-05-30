package gsn.utils.services;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * This class provides an access to Email Notification services.
 * The implementation is based on the Apache commons Email library @see http://commons.apache.org/email/
 * Prior to use this service, you MUST configure the SMTP server (postfix, gmail, ...) which will send your emails.
 * The smtp parameters are configured in the conf/emailsnew.properties.
 */
public class EmailService {

    private static final transient Logger logger = Logger.getLogger(EmailService.class);

    private static final String SMTP_FILE = "conf/email.properties";
    private static Properties Readprops;
    
    static String smtpServer;
    static String suffix;
    private static final String userName;
    private static final String userPassword;

    

    static{
    	
    	String readString;
    	Readprops = new Properties();
    	
		//load a properties file	
		try {
			Readprops.load(new FileInputStream(SMTP_FILE));
		} catch (FileNotFoundException e) {
			logger.warn("Unable to read " + SMTP_FILE);
		} catch (IOException e) {
			logger.warn("Impossible to read " + SMTP_FILE);
		}
    		
		//parse file properties
		
		readString=Readprops.getProperty("smtpserver");
		if (readString==null){
			logger.warn("specify an SMTP server in " + SMTP_FILE);
		}
		else{
			smtpServer=readString;
			
			if (! smtpServer.equalsIgnoreCase("gmail")){
				logger.warn("Currently only GMAIL configuration is supported!");
			}
		}
		
		readString=Readprops.getProperty("username");
		if (readString==null){
			logger.warn("specify a username in " + SMTP_FILE);
			userName=null;
		}
		else{
			String[] tokens = readString.split("@");
			userName=tokens[0];
			if (tokens.length>1){
				suffix=tokens[1];
			}
			else{
				//fill the suffix according to the smtp server!
				if (smtpServer.equalsIgnoreCase("gmail")){
					suffix="@gmail.com";
				}
				else if (smtpServer.equalsIgnoreCase("create-net")){
					suffix="@create-net.org";
				}
				else{
					logger.warn("Currently only GMAIL configuration is supported!");
					suffix="@gmail.com";
				}
			}
		}
		
		userPassword=Readprops.getProperty("password");
		if (userPassword==null){
			logger.warn("specify a password in " + SMTP_FILE);
		}
    		
    }
    
    


    /**
     * This method cover most of the cases of sending a simple text email. If the email to be sent has to be configured
     * in a way which is not possible with the parameters provided (such as html email, or email with attachment, ..),
     * please use the {@link #sendCustomEmail(org.apache.commons.mail.Email)} method and refer the API
     * {@see http://commons.apache.org/email/}.
     *
     * @param to      A set of destination email address of the email. Must contain at least one address.
     * @param object  The subject of the email.
     * @param message The msg of the email.
     * @return true if the email has been sent successfully, false otherwise.
     */
  
    
    public static boolean sendEmail(ArrayList<String> to, String subject, String msg) {
    	
    	boolean success = true;

    	String userEmail = userName + suffix;
    	
    	Properties props = new Properties();
    	Session session;
    	
    	if (smtpServer.equalsIgnoreCase("gmail")){
    		
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			

			session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName, userPassword);
							

						}
					});
    	}
    	else{
    		logger.warn("Currently only GMAIL configuration is supported!");
			return false;
    	}
    	
    	
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userEmail));
			
			if (to != null){
				InternetAddress[] address = new InternetAddress[to.size()];
				for(int i =0; i< to.size(); i++){
					address[i] = new InternetAddress(to.get(i));
				}
				message.setRecipients(Message.RecipientType.TO, address);
			}
			else{
				logger.warn("specify at least a receiving address!");
				return false;
			}
			
			message.setSubject(subject);
			message.setText(msg);
 
			Transport.send(message);
 
			logger.info("message successfully sent to" + to.toString());
 
		} catch (MessagingException e) {
			logger.warn(e.getMessage());
			success = false;
		}
		return success;
	}
    
}
